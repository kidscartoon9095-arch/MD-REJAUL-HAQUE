package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "learners")
data class LearnerEntity(
    @PrimaryKey val id: String,
    val displayName: String,
    val avatarIndex: Int,
    val totalXp: Int,
    val lessonsCompletedCount: Int,
    val perfectQuizCount: Int,
    val currentStreak: Int,
    val isUserControlled: Boolean,
    val isActive: Boolean
)

@Entity(
    tableName = "lesson_progress",
    primaryKeys = ["learnerId", "lessonId"]
)
data class LessonProgressEntity(
    val learnerId: String,
    val lessonId: String,
    val subjectId: String,
    val completed: Boolean,
    val bestQuizScore: Int,
    val totalQuestions: Int,
    val xpEarned: Int,
    val completedAtMillis: Long
)

@Entity(
    tableName = "unlocked_badges",
    primaryKeys = ["learnerId", "badgeId"]
)
data class UnlockedBadgeEntity(
    val learnerId: String,
    val badgeId: String,
    val unlockedAtMillis: Long
)
