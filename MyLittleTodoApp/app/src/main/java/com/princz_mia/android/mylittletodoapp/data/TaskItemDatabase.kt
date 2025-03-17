package com.princz_mia.android.mylittletodoapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TaskItem::class], version = 2)
@TypeConverters(value = [TaskItem.Priority::class])
abstract class TaskItemDatabase : RoomDatabase() {
    abstract fun taskItemDao(): TaskItemDao

    companion object {
        fun getDatabase(applicationContext: Context): TaskItemDatabase {
            return Room.databaseBuilder(
                applicationContext,
                TaskItemDatabase::class.java,
                "task-items"
            ).fallbackToDestructiveMigration().build();
        }
    }
}