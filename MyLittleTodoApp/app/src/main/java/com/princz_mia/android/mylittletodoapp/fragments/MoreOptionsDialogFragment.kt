package com.princz_mia.android.mylittletodoapp.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.princz_mia.android.mylittletodoapp.R.*
import com.princz_mia.android.mylittletodoapp.data.TaskList
import com.princz_mia.android.mylittletodoapp.databinding.FragmentMoreOptionsDialogBinding

class MoreOptionsDialogFragment(private val currentTaskList: TaskList) : DialogFragment() {
    interface MoreOptionsDialogListener {
        fun onListNameEdited(currentTaskList: TaskList)
        fun onCurrentListDeleted()
        fun onAllCompletedItemRemoved()
    }

    private lateinit var listener: MoreOptionsDialogListener
    private lateinit var binding: FragmentMoreOptionsDialogBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? MoreOptionsDialogFragment.MoreOptionsDialogListener
            ?: throw RuntimeException("Activity must implement the NewShoppingItemDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentMoreOptionsDialogBinding.inflate(LayoutInflater.from(context))
        binding.name.setText(currentTaskList.name)

        binding.editListNameButton.setOnClickListener {
            if (binding.name.text.toString() != "" && binding.name.text.toString() != "Default List" && currentTaskList.name != "Default List") {
                currentTaskList.name = binding.name.text.toString()
                listener.onListNameEdited(currentTaskList)
            }
        }

        binding.deleteListButton.setOnClickListener {
            if (currentTaskList.name != "Default List") {
                listener.onCurrentListDeleted()
            }
        }

        binding.deleteAllCompletedTaskButton.setOnClickListener {
            listener.onAllCompletedItemRemoved()
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setNegativeButton(string.button_cancel, null)
            .create()
    }

    companion object {
        const val TAG = "MoreOptionsDialogFragment"
    }
}