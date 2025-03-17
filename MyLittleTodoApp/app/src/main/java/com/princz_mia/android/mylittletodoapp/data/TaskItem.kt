package com.princz_mia.android.mylittletodoapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity(tableName = "task_item")
class TaskItem(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "desc") var desc: String,
    @ColumnInfo(name = "priority") var priority: Priority,
    @ColumnInfo(name = "createdAtDateString") var createdAtDateString: String?,
    @ColumnInfo(name = "deadlineDateString") var deadlineDateString: String?,
    @ColumnInfo(name = "isCompleted") var isCompleted: Boolean,
    @ColumnInfo(name = "listId") var listId: Long,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
): Serializable {
    fun createdAt(): LocalDate? {
        if (createdAtDateString == null) {
            return null
        }
        return LocalDate.parse(createdAtDateString, dateFormatter)
    }

    fun deadline(): LocalDate? {
        if (deadlineDateString == null) {
            return null
        }
        return LocalDate.parse(deadlineDateString, dateFormatter)
    }

    companion object {
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE
    }

    enum class Priority {
        LOW, MEDIUM, HIGH;
        companion object {
            @JvmStatic
            @TypeConverter
            fun getByOrdinal(ordinal: Int): Priority? {
                var ret: Priority? = null
                for (cat in values()) {
                    if (cat.ordinal == ordinal) {
                        ret = cat
                        break
                    }
                }
                return ret
            }

            @JvmStatic
            @TypeConverter
            fun toInt(priority: Priority): Int {
                return priority.ordinal
            }
        }
    }
}