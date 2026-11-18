package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.curriculum.CurriculumCatalog
import com.example.data.local.LearnerEntity
import com.example.data.local.LessonProgressEntity
import com.example.data.local.UnlockedBadgeEntity
import com.example.data.model.BadgeDefinition
import com.example.data.model.BadgeMetric
import com.example.data.model.Lesson
import com.example.data.model.LessonCompletionResult
import com.example.data.model.LevelInfo
import com.example.data.model.SubjectCategory
import com.example.data.repository.EducationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BadgeProgressItem(
    val definition: BadgeDefinition,
    val isUnlocked: Boolean,
    val currentProgress: Int,
    val unlockedAtMillis: Long?
) {
    val progressFraction: Float
        get() = (currentProgress.toFloat() / definition.targetValue.coerceAtLeast(1)).coerceIn(0f, 1f)
}

data class LeaderboardEntry(
    val rank: Int,
    val learner: LearnerEntity,
    val levelInfo: LevelInfo,
    val badgeCount: Int
)

sealed interface VidyaQuestUiState {
    data object Loading : VidyaQuestUiState
    data class Ready(
        val activeLearner: LearnerEntity,
        val levelInfo: LevelInfo,
        val selectedSubjectFilter: SubjectCategory?,
        val progressByLessonId: Map<String, LessonProgressEntity>,
        val badges: List<BadgeProgressItem>,
        val leaderboard: List<LeaderboardEntry>,
        val deviceProfiles: List<LearnerEntity>,
        val recommendedLesson: Lesson,
        val allLessonsCompleted: Boolean
    ) : VidyaQuestUiState
}

enum class LessonPhase { STUDY, QUIZ, RESULT }

data class LessonSessionState(
    val lesson: Lesson,
    val phase: LessonPhase = LessonPhase.STUDY,
    val studyCardIndex: Int = 0,
    val quizQuestionIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val correctCount: Int = 0,
    val comboCount: Int = 0,
    val completionResult: LessonCompletionResult? = null,
    val isSavingResult: Boolean = false
)

class VidyaQuestViewModel(
    private val repository: EducationRepository
) : ViewModel() {

    private val selectedSubjectFilter = MutableStateFlow<SubjectCategory?>(null)
    private val _lessonSession = MutableStateFlow<LessonSessionState?>(null)
    val lessonSession: StateFlow<LessonSessionState?> = _lessonSession.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
        }
    }

    val uiState: StateFlow<VidyaQuestUiState> = combine(
        repository.observeActiveLearner(),
        repository.observeAllLearnersRanked(),
        repository.observeActiveLearnerProgress(),
        repository.observeActiveLearnerBadges(),
        repository.observeBadgeCountsByLearner(),
        selectedSubjectFilter
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        val activeLearner = values[0] as LearnerEntity?
        @Suppress("UNCHECKED_CAST")
        val allLearners = values[1] as List<LearnerEntity>
        @Suppress("UNCHECKED_CAST")
        val progressList = values[2] as List<LessonProgressEntity>
        @Suppress("UNCHECKED_CAST")
        val unlockedBadges = values[3] as List<UnlockedBadgeEntity>
        @Suppress("UNCHECKED_CAST")
        val badgeCounts = values[4] as List<com.example.data.local.LearnerBadgeCount>
        val subjectFilter = values[5] as SubjectCategory?

        if (activeLearner == null) {
            VidyaQuestUiState.Loading
        } else {
            val progressMap = progressList.associateBy { it.lessonId }
            val unlockedMap = unlockedBadges.associateBy { it.badgeId }
            val completedProgress = progressList.filter { it.completed }
            val completedIds = completedProgress.map { it.lessonId }.toSet()
            val subjectsExplored = completedProgress.map { it.subjectId }.distinct().size
            val tracksMastered = SubjectCategory.entries.count { category ->
                val trackLessons = CurriculumCatalog.lessonsForSubject(category)
                trackLessons.isNotEmpty() && trackLessons.all { it.id in completedIds }
            }

            val badgeItems = CurriculumCatalog.badges.map { def ->
                val unlockedEntity = unlockedMap[def.id]
                val metricValue = when (def.metric) {
                    BadgeMetric.LESSONS_COMPLETED -> activeLearner.lessonsCompletedCount
                    BadgeMetric.PERFECT_QUIZZES -> activeLearner.perfectQuizCount
                    BadgeMetric.TOTAL_XP -> activeLearner.totalXp
                    BadgeMetric.SUBJECTS_EXPLORED -> subjectsExplored
                    BadgeMetric.TRACK_MASTERED -> tracksMastered
                }
                BadgeProgressItem(
                    definition = def,
                    isUnlocked = unlockedEntity != null,
                    currentProgress = if (unlockedEntity != null) def.targetValue else metricValue.coerceAtMost(def.targetValue),
                    unlockedAtMillis = unlockedEntity?.unlockedAtMillis
                )
            }

            val badgeCountMap = badgeCounts.associate { it.learnerId to it.badgeCount }
            val leaderboard = allLearners.mapIndexed { index, learner ->
                LeaderboardEntry(
                    rank = index + 1,
                    learner = learner,
                    levelInfo = CurriculumCatalog.computeLevel(learner.totalXp),
                    badgeCount = badgeCountMap[learner.id] ?: 0
                )
            }

            val nextUnfinished = CurriculumCatalog.lessons.firstOrNull { it.id !in completedIds }
            val recommended = nextUnfinished ?: CurriculumCatalog.lessons.first()

            VidyaQuestUiState.Ready(
                activeLearner = activeLearner,
                levelInfo = CurriculumCatalog.computeLevel(activeLearner.totalXp),
                selectedSubjectFilter = subjectFilter,
                progressByLessonId = progressMap,
                badges = badgeItems,
                leaderboard = leaderboard,
                deviceProfiles = allLearners.filter { it.isUserControlled },
                recommendedLesson = recommended,
                allLessonsCompleted = nextUnfinished == null
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = VidyaQuestUiState.Loading
    )

    fun selectSubjectFilter(subject: SubjectCategory?) {
        selectedSubjectFilter.value = subject
    }

    fun switchProfile(learnerId: String) {
        viewModelScope.launch {
            repository.switchActiveProfile(learnerId)
        }
    }

    fun createProfile(name: String, avatarIndex: Int) {
        viewModelScope.launch {
            repository.createUserProfile(name, avatarIndex)
        }
    }

    fun renameActiveProfile(newName: String) {
        viewModelScope.launch {
            repository.renameActiveProfile(newName)
        }
    }

    fun startLesson(lessonId: String) {
        val lesson = CurriculumCatalog.lessonById(lessonId) ?: return
        _lessonSession.value = LessonSessionState(lesson = lesson)
    }

    fun clearLessonSession() {
        _lessonSession.value = null
    }

    fun previousStudyCard() {
        _lessonSession.update { state ->
            if (state == null || state.studyCardIndex <= 0) state
            else state.copy(studyCardIndex = state.studyCardIndex - 1)
        }
    }

    fun nextStudyCardOrStartQuiz() {
        _lessonSession.update { state ->
            if (state == null) return@update null
            if (state.studyCardIndex < state.lesson.cards.lastIndex) {
                state.copy(studyCardIndex = state.studyCardIndex + 1)
            } else {
                state.copy(
                    phase = LessonPhase.QUIZ,
                    quizQuestionIndex = 0,
                    selectedOptionIndex = null,
                    isAnswerSubmitted = false
                )
            }
        }
    }

    fun selectQuizOption(optionIndex: Int) {
        _lessonSession.update { state ->
            if (state == null || state.isAnswerSubmitted) state
            else state.copy(selectedOptionIndex = optionIndex)
        }
    }

    fun submitCurrentAnswer() {
        _lessonSession.update { state ->
            val selected = state?.selectedOptionIndex ?: return@update state
            if (state.isAnswerSubmitted) return@update state
            val currentQuestion = state.lesson.quiz[state.quizQuestionIndex]
            val isCorrect = selected == currentQuestion.correctIndex
            state.copy(
                isAnswerSubmitted = true,
                correctCount = if (isCorrect) state.correctCount + 1 else state.correctCount,
                comboCount = if (isCorrect) state.comboCount + 1 else 0
            )
        }
    }

    fun advanceQuizOrFinish() {
        val current = _lessonSession.value ?: return
        if (!current.isAnswerSubmitted) return

        if (current.quizQuestionIndex < current.lesson.quiz.lastIndex) {
            _lessonSession.value = current.copy(
                quizQuestionIndex = current.quizQuestionIndex + 1,
                selectedOptionIndex = null,
                isAnswerSubmitted = false
            )
        } else {
            _lessonSession.value = current.copy(isSavingResult = true)
            viewModelScope.launch {
                val result = repository.completeLesson(
                    lessonId = current.lesson.id,
                    correctAnswers = current.correctCount,
                    totalQuestions = current.lesson.quiz.size
                )
                _lessonSession.update { latest ->
                    latest?.copy(
                        phase = LessonPhase.RESULT,
                        completionResult = result,
                        isSavingResult = false
                    )
                }
            }
        }
    }

    class Factory(private val repository: EducationRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return VidyaQuestViewModel(repository) as T
        }
    }
}
