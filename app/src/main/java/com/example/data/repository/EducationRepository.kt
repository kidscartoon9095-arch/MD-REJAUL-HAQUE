package com.example.data.repository

import com.example.data.curriculum.CurriculumCatalog
import com.example.data.local.EducationDao
import com.example.data.local.LearnerBadgeCount
import com.example.data.local.LearnerEntity
import com.example.data.local.LessonProgressEntity
import com.example.data.local.UnlockedBadgeEntity
import com.example.data.model.BadgeDefinition
import com.example.data.model.BadgeMetric
import com.example.data.model.LessonCompletionResult
import com.example.data.model.SubjectCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID

class EducationRepository(private val dao: EducationDao) {

    private val seedMutex = Mutex()

    suspend fun ensureSeeded() {
        seedMutex.withLock {
            if (dao.countLearners() > 0) return

            val initialLearners = listOf(
                LearnerEntity(
                    id = "user_primary",
                    displayName = "Aarav (You)",
                    avatarIndex = 0,
                    totalXp = 0,
                    lessonsCompletedCount = 0,
                    perfectQuizCount = 0,
                    currentStreak = 1,
                    isUserControlled = true,
                    isActive = true
                ),
                LearnerEntity(
                    id = "peer_ananya",
                    displayName = "Ananya Verma",
                    avatarIndex = 1,
                    totalXp = 640,
                    lessonsCompletedCount = 6,
                    perfectQuizCount = 4,
                    currentStreak = 5,
                    isUserControlled = false,
                    isActive = false
                ),
                LearnerEntity(
                    id = "peer_rohan",
                    displayName = "Rohan Kulkarni",
                    avatarIndex = 2,
                    totalXp = 465,
                    lessonsCompletedCount = 5,
                    perfectQuizCount = 2,
                    currentStreak = 4,
                    isUserControlled = false,
                    isActive = false
                ),
                LearnerEntity(
                    id = "peer_meera",
                    displayName = "Meera Iyer",
                    avatarIndex = 3,
                    totalXp = 320,
                    lessonsCompletedCount = 3,
                    perfectQuizCount = 2,
                    currentStreak = 3,
                    isUserControlled = false,
                    isActive = false
                ),
                LearnerEntity(
                    id = "peer_kabir",
                    displayName = "Kabir Sharma",
                    avatarIndex = 4,
                    totalXp = 195,
                    lessonsCompletedCount = 2,
                    perfectQuizCount = 1,
                    currentStreak = 2,
                    isUserControlled = false,
                    isActive = false
                ),
                LearnerEntity(
                    id = "peer_diya",
                    displayName = "Diya Nair",
                    avatarIndex = 5,
                    totalXp = 95,
                    lessonsCompletedCount = 1,
                    perfectQuizCount = 0,
                    currentStreak = 1,
                    isUserControlled = false,
                    isActive = false
                )
            )
            dao.upsertLearners(initialLearners)

            val now = System.currentTimeMillis()
            val peerBadges = listOf(
                UnlockedBadgeEntity("peer_ananya", "badge_first_step", now - 86_400_000L * 4),
                UnlockedBadgeEntity("peer_ananya", "badge_sharp_mind", now - 86_400_000L * 4),
                UnlockedBadgeEntity("peer_ananya", "badge_hat_trick", now - 86_400_000L * 3),
                UnlockedBadgeEntity("peer_ananya", "badge_polymath", now - 86_400_000L * 2),
                UnlockedBadgeEntity("peer_ananya", "badge_xp_hunter", now - 86_400_000L),
                UnlockedBadgeEntity("peer_rohan", "badge_first_step", now - 86_400_000L * 3),
                UnlockedBadgeEntity("peer_rohan", "badge_sharp_mind", now - 86_400_000L * 3),
                UnlockedBadgeEntity("peer_rohan", "badge_hat_trick", now - 86_400_000L * 2),
                UnlockedBadgeEntity("peer_rohan", "badge_xp_hunter", now - 86_400_000L),
                UnlockedBadgeEntity("peer_meera", "badge_first_step", now - 86_400_000L * 2),
                UnlockedBadgeEntity("peer_meera", "badge_sharp_mind", now - 86_400_000L * 2),
                UnlockedBadgeEntity("peer_meera", "badge_hat_trick", now - 86_400_000L),
                UnlockedBadgeEntity("peer_kabir", "badge_first_step", now - 86_400_000L),
                UnlockedBadgeEntity("peer_kabir", "badge_sharp_mind", now - 86_400_000L),
                UnlockedBadgeEntity("peer_diya", "badge_first_step", now - 43_200_000L)
            )
            dao.insertUnlockedBadges(peerBadges)
        }
    }

    fun observeActiveLearner(): Flow<LearnerEntity?> = dao.observeActiveLearner()

    fun observeAllLearnersRanked(): Flow<List<LearnerEntity>> = dao.observeAllLearnersRanked()

    fun observeBadgeCountsByLearner(): Flow<List<LearnerBadgeCount>> =
        dao.observeBadgeCountsByLearner()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeActiveLearnerProgress(): Flow<List<LessonProgressEntity>> =
        dao.observeActiveLearner().flatMapLatest { learner ->
            if (learner == null) flowOf(emptyList())
            else dao.observeProgressForLearner(learner.id)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeActiveLearnerBadges(): Flow<List<UnlockedBadgeEntity>> =
        dao.observeActiveLearner().flatMapLatest { learner ->
            if (learner == null) flowOf(emptyList())
            else dao.observeBadgesForLearner(learner.id)
        }

    suspend fun createUserProfile(name: String, avatarIndex: Int) {
        val trimmed = name.trim().take(28)
        if (trimmed.isEmpty()) return
        val newId = "user_${UUID.randomUUID().toString().take(8)}"
        val newLearner = LearnerEntity(
            id = newId,
            displayName = trimmed,
            avatarIndex = avatarIndex.coerceIn(0, 5),
            totalXp = 0,
            lessonsCompletedCount = 0,
            perfectQuizCount = 0,
            currentStreak = 1,
            isUserControlled = true,
            isActive = false
        )
        dao.upsertLearner(newLearner)
        dao.switchActiveLearner(newId)
    }

    suspend fun switchActiveProfile(learnerId: String) {
        val target = dao.getLearnerById(learnerId) ?: return
        if (target.isUserControlled) {
            dao.switchActiveLearner(learnerId)
        }
    }

    suspend fun renameActiveProfile(newName: String) {
        val trimmed = newName.trim().take(28)
        if (trimmed.isEmpty()) return
        val active = dao.getActiveLearnerOnce() ?: return
        dao.renameLearner(active.id, trimmed)
    }

    suspend fun completeLesson(
        lessonId: String,
        correctAnswers: Int,
        totalQuestions: Int
    ): LessonCompletionResult? {
        val lesson = CurriculumCatalog.lessonById(lessonId) ?: return null
        val learner = dao.getActiveLearnerOnce() ?: return null
        val existingProgress = dao.getLessonProgress(learner.id, lessonId)

        val isFirstCompletion = existingProgress == null || !existingProgress.completed
        val isPerfectNow = totalQuestions > 0 && correctAnswers == totalQuestions
        val wasPerfectBefore = existingProgress != null &&
            existingProgress.totalQuestions > 0 &&
            existingProgress.bestQuizScore == existingProgress.totalQuestions

        val baseXpAwarded = if (isFirstCompletion) lesson.baseXp else 15
        val perCorrectBonus = if (isFirstCompletion) correctAnswers * 15 else 0
        val perfectBonus = if (isPerfectNow && !wasPerfectBefore) 25 else 0
        val quizBonusXp = perCorrectBonus + perfectBonus
        val lessonSessionXp = baseXpAwarded + quizBonusXp

        val now = System.currentTimeMillis()
        val updatedProgress = LessonProgressEntity(
            learnerId = learner.id,
            lessonId = lesson.id,
            subjectId = lesson.subject.id,
            completed = true,
            bestQuizScore = maxOf(existingProgress?.bestQuizScore ?: 0, correctAnswers),
            totalQuestions = totalQuestions,
            xpEarned = (existingProgress?.xpEarned ?: 0) + lessonSessionXp,
            completedAtMillis = now
        )
        dao.upsertLessonProgress(updatedProgress)

        val allProgress = dao.getProgressForLearnerOnce(learner.id)
        val completedProgress = allProgress.filter { it.completed }
        val completedCount = completedProgress.size
        val perfectCount = completedProgress.count {
            it.totalQuestions > 0 && it.bestQuizScore == it.totalQuestions
        }
        val subjectsExplored = completedProgress.map { it.subjectId }.distinct().size
        val tracksMastered = SubjectCategory.entries.count { category ->
            val trackLessons = CurriculumCatalog.lessonsForSubject(category)
            val completedIds = completedProgress.map { it.lessonId }.toSet()
            trackLessons.isNotEmpty() && trackLessons.all { it.id in completedIds }
        }

        val existingBadgeIds = dao.getBadgesForLearnerOnce(learner.id).map { it.badgeId }.toMutableSet()
        val newlyUnlocked = mutableListOf<BadgeDefinition>()
        var runningTotalXp = learner.totalXp + lessonSessionXp
        var totalBadgeBonusXp = 0

        // Two passes ensure XP-milestone badges unlock cleanly if another badge pushes XP over threshold
        repeat(2) {
            for (badge in CurriculumCatalog.badges) {
                if (badge.id in existingBadgeIds) continue
                val currentMetricValue = when (badge.metric) {
                    BadgeMetric.LESSONS_COMPLETED -> completedCount
                    BadgeMetric.PERFECT_QUIZZES -> perfectCount
                    BadgeMetric.TOTAL_XP -> runningTotalXp
                    BadgeMetric.SUBJECTS_EXPLORED -> subjectsExplored
                    BadgeMetric.TRACK_MASTERED -> tracksMastered
                }
                if (currentMetricValue >= badge.targetValue) {
                    existingBadgeIds.add(badge.id)
                    newlyUnlocked.add(badge)
                    totalBadgeBonusXp += badge.rewardXp
                    runningTotalXp += badge.rewardXp
                }
            }
        }

        if (newlyUnlocked.isNotEmpty()) {
            dao.insertUnlockedBadges(
                newlyUnlocked.map {
                    UnlockedBadgeEntity(
                        learnerId = learner.id,
                        badgeId = it.id,
                        unlockedAtMillis = now
                    )
                }
            )
        }

        val updatedStreak = if (isFirstCompletion) learner.currentStreak + 1 else learner.currentStreak
        dao.upsertLearner(
            learner.copy(
                totalXp = runningTotalXp,
                lessonsCompletedCount = completedCount,
                perfectQuizCount = perfectCount,
                currentStreak = updatedStreak
            )
        )

        return LessonCompletionResult(
            lessonTitle = lesson.title,
            correctAnswers = correctAnswers,
            totalQuestions = totalQuestions,
            baseXpAwarded = baseXpAwarded,
            quizBonusXp = quizBonusXp,
            badgeBonusXp = totalBadgeBonusXp,
            newlyUnlockedBadges = newlyUnlocked,
            isFirstCompletion = isFirstCompletion
        )
    }
}
