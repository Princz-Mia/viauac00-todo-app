package com.princz_mia.android.mylittletodoapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TaskList::class], version = 1)
abstract class TaskListDatabase : RoomDatabase() {
    abstract fun taskListDao(): TaskListDao

    companion object {
        private var instance: TaskListDatabase? = null

        fun getDatabase(applicationContext: Context): TaskListDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    applicationContext,
                    TaskListDatabase::class.java,
                    "task-lists"
                ).build()
                instance = newInstance
                newInstance
            }
        }
    }

    fun initializeDefaultList() {
        if (taskListDao().getAll().isEmpty()) {
            val defaultList = TaskList("Default List")
            taskListDao().insert(defaultList)
        }
    }
}