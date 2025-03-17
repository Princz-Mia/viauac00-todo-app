package com.princz_mia.android.mylittletodoapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskItemDao {
    @Query("SELECT * FROM task_item")
    fun getAll(): List<TaskItem>

    @Insert
    fun insert(taskItem: TaskItem): Long

    @Update
    fun update(taskItem: TaskItem)

    @Delete
    fun deleteItem(taskItem: TaskItem)

    @Query("DELETE FROM task_item WHERE isCompleted == 1")
    fun deleteAllCompletedItems()

    @Query("DELETE FROM task_item WHERE listId = :listId")
    fun deleteItemsByListId(listId: Long)

    @Query("SELECT * FROM task_item WHERE listId = :listId")
    fun getByListId(listId: Long) : List<TaskItem>
}