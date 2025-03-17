package com.princz_mia.android.mylittletodoapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_list")
class TaskList(
    @ColumnInfo(name = "name") var name: String,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) {
    override fun toString(): String {
        return name
    }
}