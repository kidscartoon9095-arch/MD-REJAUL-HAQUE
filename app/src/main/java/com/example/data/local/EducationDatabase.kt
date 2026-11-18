package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        LearnerEntity::class,
        LessonProgressEntity::class,
        UnlockedBadgeEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class EducationDatabase : RoomDatabase() {
    abstract fun educationDao(): EducationDao

    companion object {
        @Volatile
        private var INSTANCE: EducationDatabase? = null

        fun getInstance(context: Context): EducationDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    EducationDatabase::class.java,
                    "vidyaquest_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
