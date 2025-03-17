package com.princz_mia.android.mylittletodoapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskListDao {
    @Query("SELECT * FROM task_list")
    fun getAll(): List<TaskList>

    @Insert
    fun insert(taskList: TaskList): Long

    @Update
    fun update(taskList: TaskList)

    @Delete
    fun deleteItem(taskList: TaskList)

    @Query("SELECT * FROM task_list WHERE name = :name")
    fun getByName(name: String) : TaskList
}