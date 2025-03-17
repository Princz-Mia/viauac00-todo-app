package com.princz_mia.android.mylittletodoapp.fragments

import android.R
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.princz_mia.android.mylittletodoapp.R.*
import com.princz_mia.android.mylittletodoapp.adapter.TaskListAdapter
import com.princz_mia.android.mylittletodoapp.data.TaskItem
import com.princz_mia.android.mylittletodoapp.data.TaskList
import com.princz_mia.android.mylittletodoapp.databinding.FragmentNewTaskDialogBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class NewTaskItemDialogFragment(private val taskListAdapter: TaskListAdapter) : DialogFragment() {

    interface NewTaskItemDialogListener {
        fun onTaskItemCreated(newItem: TaskItem)
    }

    private lateinit var listener: NewTaskItemDialogListener
    private lateinit var binding: FragmentNewTaskDialogBinding
    private var deadline: LocalDate? = null
    private var taskItem: TaskItem? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewTaskItemDialogListener
            ?: throw RuntimeException("Activity must implement the NewShoppingItemDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentNewTaskDialogBinding.inflate(LayoutInflater.from(context))
        binding.spPriority.adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(array.priority_items)
        )

        binding.spList.adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_dropdown_item,
            taskListAdapter.lists
        )

        binding.saveButton.setOnClickListener {
            if (isValid() == true)
                listener.onTaskItemCreated(saveAction())
        }

        binding.deadlinePickerButton.setOnClickListener {
            openDeadlinePicker()
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setNegativeButton(string.button_cancel, null)
            .create()
    }

    private fun isValid() = binding.name.text?.isNotEmpty()

    companion object {
        const val TAG = "NewTaskItemDialogFragment"
    }

    private fun openDeadlinePicker() {
        if (deadline == null) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog =
                DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                    deadline = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                    updateTimeButtonText()
                }, year, month, day)

            val today = Calendar.getInstance()
            datePickerDialog.datePicker.minDate = today.timeInMillis

            datePickerDialog.show()
        } else {
            val datePickerDialog =
                DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
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
            binding.deadlinePickerButton.text = getString(string.task_deadline_specified, deadline!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        }
    }

    private fun saveAction(): TaskItem {
        val name = binding.name.text.toString()
        val desc = binding.desc.text.toString()
        val priority = TaskItem.Priority.getByOrdinal(binding.spPriority.selectedItemPosition)
            ?: TaskItem.Priority.LOW
        val deadlineString = if (deadline == null) null else TaskItem.dateFormatter.format(deadline)
        val createdAtString = TaskItem.dateFormatter.format(LocalDate.now())

        val selectedList = binding.spList.selectedItem as TaskList
        val listId = selectedList.id

        binding.name.setText("")
        binding.desc.setText("")
        dismiss()
        return TaskItem(name, desc, priority, createdAtString, deadlineString, false, listId)
    }
}