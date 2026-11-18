package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface EducationDao {

    @Query("SELECT * FROM learners ORDER BY totalXp DESC, lessonsCompletedCount DESC, displayName ASC")
    fun observeAllLearnersRanked(): Flow<List<LearnerEntity>>

    @Query("SELECT * FROM learners WHERE isActive = 1 LIMIT 1")
    fun observeActiveLearner(): Flow<LearnerEntity?>

    @Query("SELECT * FROM learners WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveLearnerOnce(): LearnerEntity?

    @Query("SELECT * FROM learners WHERE id = :learnerId LIMIT 1")
    suspend fun getLearnerById(learnerId: String): LearnerEntity?

    @Query("SELECT COUNT(*) FROM learners")
    suspend fun countLearners(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLearners(learners: List<LearnerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLearner(learner: LearnerEntity)

    @Query("UPDATE learners SET isActive = 0")
    suspend fun clearAllActiveFlags()

    @Query("UPDATE learners SET isActive = 1 WHERE id = :learnerId")
    suspend fun markLearnerActive(learnerId: String)

    @Transaction
    suspend fun switchActiveLearner(learnerId: String) {
        clearAllActiveFlags()
        markLearnerActive(learnerId)
    }

    @Query("UPDATE learners SET displayName = :newName WHERE id = :learnerId")
    suspend fun renameLearner(learnerId: String, newName: String)

    @Query("SELECT * FROM lesson_progress WHERE learnerId = :learnerId")
    fun observeProgressForLearner(learnerId: String): Flow<List<LessonProgressEntity>>

    @Query("SELECT * FROM lesson_progress WHERE learnerId = :learnerId")
    suspend fun getProgressForLearnerOnce(learnerId: String): List<LessonProgressEntity>

    @Query("SELECT * FROM lesson_progress WHERE learnerId = :learnerId AND lessonId = :lessonId LIMIT 1")
    suspend fun getLessonProgress(learnerId: String, lessonId: String): LessonProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLessonProgress(progress: LessonProgressEntity)

    @Query("SELECT * FROM unlocked_badges WHERE learnerId = :learnerId")
    fun observeBadgesForLearner(learnerId: String): Flow<List<UnlockedBadgeEntity>>

    @Query("SELECT * FROM unlocked_badges WHERE learnerId = :learnerId")
    suspend fun getBadgesForLearnerOnce(learnerId: String): List<UnlockedBadgeEntity>

    @Query("SELECT learnerId, COUNT(*) as badgeCount FROM unlocked_badges GROUP BY learnerId")
    fun observeBadgeCountsByLearner(): Flow<List<LearnerBadgeCount>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUnlockedBadges(badges: List<UnlockedBadgeEntity>)
}

data class LearnerBadgeCount(
    val learnerId: String,
    val badgeCount: Int
)
