package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.EducationDatabase
import com.example.data.repository.EducationRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var db: EducationDatabase
    private lateinit var repository: EducationRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, EducationDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = EducationRepository(db.educationDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `read app name from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("VidyaQuest", appName)
    }

    @Test
    fun `completing first lesson with perfect quiz awards xp and unlocks milestone badges`() = runTest {
        repository.ensureSeeded()
        val result = repository.completeLesson(
            lessonId = "math_ratios",
            correctAnswers = 3,
            totalQuestions = 3
        )
        assertNotNull(result)
        val completion = result!!
        assertTrue(completion.isFirstCompletion)
        assertEquals(60, completion.baseXpAwarded)
        assertEquals(70, completion.quizBonusXp) // 3 * 15 + 25 perfect bonus
        assertTrue(completion.newlyUnlockedBadges.any { it.id == "badge_first_step" })
        assertTrue(completion.newlyUnlockedBadges.any { it.id == "badge_sharp_mind" })

        val activeLearner = repository.observeActiveLearner().first()!!
        assertEquals(completion.totalXpGained, activeLearner.totalXp)
        assertEquals(1, activeLearner.lessonsCompletedCount)
        assertEquals(1, activeLearner.perfectQuizCount)
    }
}
