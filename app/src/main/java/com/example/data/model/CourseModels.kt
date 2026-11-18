package com.example.data.model

enum class SubjectCategory(val id: String, val label: String) {
    MATH("math", "Math & Logic"),
    SCIENCE("science", "Science"),
    CODING("coding", "Computer Science"),
    HISTORY("history", "History & Civics")
}

data class StudyCard(
    val heading: String,
    val body: String,
    val keyTakeaway: String,
    val realWorldExample: String
)

data class QuizQuestion(
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class Lesson(
    val id: String,
    val subject: SubjectCategory,
    val order: Int,
    val title: String,
    val subtitle: String,
    val durationMinutes: Int,
    val baseXp: Int,
    val cards: List<StudyCard>,
    val quiz: List<QuizQuestion>
)

enum class BadgeMetric {
    LESSONS_COMPLETED,
    PERFECT_QUIZZES,
    TOTAL_XP,
    SUBJECTS_EXPLORED,
    TRACK_MASTERED
}

data class BadgeDefinition(
    val id: String,
    val title: String,
    val description: String,
    val metric: BadgeMetric,
    val targetValue: Int,
    val rewardXp: Int,
    val tierLabel: String
)

data class LevelInfo(
    val level: Int,
    val title: String,
    val currentXpInLevel: Int,
    val xpNeededForNextLevel: Int
) {
    val progressFraction: Float
        get() = if (xpNeededForNextLevel <= 0) 1f else (currentXpInLevel.toFloat() / xpNeededForNextLevel).coerceIn(0f, 1f)
}

data class LessonCompletionResult(
    val lessonTitle: String,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val baseXpAwarded: Int,
    val quizBonusXp: Int,
    val badgeBonusXp: Int,
    val newlyUnlockedBadges: List<BadgeDefinition>,
    val isFirstCompletion: Boolean
) {
    val totalXpGained: Int
        get() = baseXpAwarded + quizBonusXp + badgeBonusXp
    val accuracyPercent: Int
        get() = if (totalQuestions == 0) 100 else ((correctAnswers * 100f) / totalQuestions).toInt()
}
