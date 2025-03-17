package com.princz_mia.android.mylittletodoapp

import android.app.DatePickerDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import com.princz_mia.android.mylittletodoapp.adapter.TaskItemAdapter
import com.princz_mia.android.mylittletodoapp.data.TaskItem
import com.princz_mia.android.mylittletodoapp.data.TaskItemDatabase
import com.princz_mia.android.mylittletodoapp.databinding.ActivityTaskBinding
import nl.dionsegijn.konfetti.emitters.StreamEmitter
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class TaskActivity : AppCompatActivity(),
    TaskItemAdapter.TaskItemClickListener
{
    private lateinit var binding: ActivityTaskBinding

    private lateinit var taskItemDatabase: TaskItemDatabase
    private lateinit var taskItemAdapter: TaskItemAdapter

    private lateinit var task: TaskItem
    private var deadline: LocalDate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskItemDatabase = TaskItemDatabase.getDatabase(applicationContext)
        taskItemAdapter = TaskItemAdapter(this)

        task = intent.getSerializableExtra("taskItem") as TaskItem
        deadline = task.deadline()

        binding.spPriority.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.priority_items)
        )

        binding.name.setText(task.name)
        binding.desc.setText(task.desc)
        binding.spPriority.setSelection(task.priority.ordinal)
        updateTimeButtonText()

        binding.backButton.setOnClickListener {
            val isValid = (binding.name.text != null) && (binding.name.text.isNotBlank()) && (binding.name.text.isNotEmpty())
            if (isValid) {
                val updatedTask = saveAction()
                updatedTask.id = task.id

                val intent = intent
                intent.putExtra("updatedTask", updatedTask)
                setResult(1000, intent)
                finish()
            } else {
                Snackbar.make(View(this@TaskActivity),"Name for task is missing.",Snackbar.LENGTH_LONG).show()
            }
        }

        binding.deadlinePickerButton.setOnClickListener {
            openDeadlinePicker()
        }

        binding.completeButton.setOnClickListener {
            binding.viewKonfetti.build()
                .addColors(Color.parseColor("#FFD700"), Color.parseColor("#FD7F20"))
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1000)
                .addShapes(Shape.Square, Shape.Circle)
                .addSizes(Size(12))
                .streamFor(500, StreamEmitter.INDEFINITE)
            task.isCompleted = true
        }
    }

    private fun saveAction(): TaskItem {
        val name = binding.name.text.toString()
        val desc = binding.desc.text.toString()
        val priority = TaskItem.Priority.getByOrdinal(binding.spPriority.selectedItemPosition)
            ?: TaskItem.Priority.LOW
        val deadlineString = if (deadline == null) null else TaskItem.dateFormatter.format(deadline)

        return TaskItem(name, desc, priority, task.createdAtDateString, deadlineString, task.isCompleted, task.listId)
    }

    private fun openDeadlinePicker() {
        if (deadline == null) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog =
                DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                    deadline = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                    updateTimeButtonText()
                }, year, month, day)

            val today = Calendar.getInstance()
            datePickerDialog.datePicker.minDate = today.timeInMillis

            datePickerDialog.show()
        } else {
            val datePickerDialog =
                DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                    deadline = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                    updateTimeButtonText()
                }, deadline!!.year, deadline!!.monthValue - 1, deadline!!.dayOfMonth)

            val today = Calendar.getInstance()
            datePickerDialog.datePicker.minDate = today.timeInMillis

            datePickerDialog.show()
        }
    }

    private fun updateTimeButtonText() {
        if (deadline != null) {
            binding.deadlinePickerButton.text = "Deadline: " + deadline!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
    }

    override fun onTaskItemChanged(item: TaskItem) {
        TODO("Not yet implemented")
    }

    override fun onTaskItemRemoved(item: TaskItem) {
        TODO("Not yet implemented")
    }

    override fun onTaskItemClick(item: TaskItem) {
        TODO("Not yet implemented")
    }
}