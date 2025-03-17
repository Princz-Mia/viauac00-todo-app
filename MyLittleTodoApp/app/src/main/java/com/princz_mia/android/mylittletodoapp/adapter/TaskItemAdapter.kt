package com.princz_mia.android.mylittletodoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.princz_mia.android.mylittletodoapp.data.TaskItem
import com.princz_mia.android.mylittletodoapp.databinding.TaskItemBinding

class TaskItemAdapter(private val listener: TaskItemClickListener) :
    RecyclerView.Adapter<TaskItemAdapter.TaskItemViewHolder>() {

    private val items = mutableListOf<TaskItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TaskItemViewHolder(
        TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        val taskItem = items[position]

        holder.binding.priorityImageView.setImageResource(getPriorityMarker(taskItem.priority))
        holder.binding.tvName.text = taskItem.name
        holder.binding.tvDescription.text = taskItem.desc
        holder.binding.tvPriority.text = taskItem.priority.name
        holder.binding.tvDeadline.text = if (taskItem.deadline() == null) "Deadline: Not specified" else "Deadline: " + taskItem.deadline().toString()
        holder.binding.cbIsDone.isChecked = taskItem.isCompleted

        holder.binding.cbIsDone.setOnCheckedChangeListener { buttonView, isChecked ->
            taskItem.isCompleted = isChecked
            listener.onTaskItemChanged(taskItem)
        }

        holder.binding.deleteButton.setOnClickListener {
            val item = items[position]
            listener.onTaskItemRemoved(item)
        }

        holder.binding.taskBody.setOnClickListener {
            val item = items[position]
            listener.onTaskItemClick(item)
        }
    }


    @DrawableRes()
    private fun getPriorityMarker(priority: TaskItem.Priority): Int {
        return when (priority) {
            TaskItem.Priority.LOW -> com.princz_mia.android.mylittletodoapp.R.mipmap.ic_priority_low
            TaskItem.Priority.MEDIUM -> com.princz_mia.android.mylittletodoapp.R.mipmap.ic_priority_medium
            TaskItem.Priority.HIGH -> com.princz_mia.android.mylittletodoapp.R.mipmap.ic_priority_high
        }
    }

    fun addItem(item: TaskItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(taskItems: List<TaskItem>) {
        items.clear()
        items.addAll(taskItems)
        notifyDataSetChanged()
    }

    fun removeItem(item: TaskItem) {
        items.remove(item)
        notifyDataSetChanged()
    }

    fun removeItemsByListId(listId: Long) {
        items.removeAll { it.listId == listId}
        notifyDataSetChanged()
    }

    fun removeAllCompletedItem() {
        items.removeAll { it.isCompleted }
        notifyDataSetChanged()
    }

    fun sortByName() {
        items.sortBy { it.name }
        notifyDataSetChanged()
    }

    fun sortByPriority() {
        items.sortBy { it.priority }
        items.reverse()
        notifyDataSetChanged()
    }

    fun sortByDeadlineDate() {
        items.sortBy { it.deadline() }
        notifyDataSetChanged()
    }

    fun sortByCreationDate() {
        items.sortBy { it.createdAt() }
        notifyDataSetChanged()
    }

    interface TaskItemClickListener {
        fun onTaskItemChanged(item: TaskItem)
        fun onTaskItemRemoved(item: TaskItem)
        fun onTaskItemClick(item: TaskItem)
    }

    override fun getItemCount(): Int = items.size


    inner class TaskItemViewHolder(val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root)
}