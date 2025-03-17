package com.princz_mia.android.mylittletodoapp.fragments

import android.R
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.princz_mia.android.mylittletodoapp.R.*
import com.princz_mia.android.mylittletodoapp.data.TaskList
import com.princz_mia.android.mylittletodoapp.databinding.FragmentNewTaskListDialogBinding

class NewTaskListDialogFragment : DialogFragment() {

    interface NewTaskListDialogListener {
        fun onListCreated(newList: TaskList)
    }

    private lateinit var listener: NewTaskListDialogListener
    private lateinit var binding: FragmentNewTaskListDialogBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewTaskListDialogListener
            ?: throw RuntimeException("Activity must implement the NewShoppingItemDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentNewTaskListDialogBinding.inflate(LayoutInflater.from(context))

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setPositiveButton(string.button_ok) { dialogInterface, i ->
                if (isValid() == true) {
                    if (binding.name.text.toString() != "Default List") {
                        listener.onListCreated(saveAction())
                    }
                }
            }
            .setNegativeButton(string.button_cancel, null)
            .create()
    }

    private fun isValid() = binding.name.text?.isNotEmpty()

    companion object {
        const val TAG = "NewTaskListDialogFragment"
    }

    private fun saveAction(): TaskList {
        val name = binding.name.text.toString()

        binding.name.setText("")
        dismiss()
        return TaskList(name)
    }
}