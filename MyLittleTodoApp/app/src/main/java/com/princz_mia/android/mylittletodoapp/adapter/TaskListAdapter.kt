package com.princz_mia.android.mylittletodoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.princz_mia.android.mylittletodoapp.data.TaskList
import com.princz_mia.android.mylittletodoapp.databinding.ListItemBinding

class TaskListAdapter(private val listener: TaskListClickListener) :
    RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder>() {

    val lists = mutableListOf<TaskList>()
    private val defaultListName = "Default List"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TaskListViewHolder(
        ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        val taskLists = lists[position]
        holder.binding.tvName.text = taskLists.name

        holder.binding.root.setOnClickListener {
            listener.onListChanged(taskLists)
        }

        holder.binding.tvName.setOnClickListener {
            val list = lists[position]
            listener.onListClick(list)
        }
    }

    fun getDefaultList() : TaskList? {
        return lists.find { it.name.equals(defaultListName) }
    }

    fun addList(list: TaskList) {
        lists.add(list)
        notifyItemInserted(lists.size - 1)
    }

    fun update(taskLists: List<TaskList>) {
        lists.clear()
        lists.addAll(taskLists)
        notifyDataSetChanged()
    }

    fun removeList(list: TaskList) {
        lists.remove(list)
        notifyDataSetChanged()
    }

    interface TaskListClickListener {
        fun onListChanged(list: TaskList)
        fun onListRemoved(list: TaskList)
        fun onListClick(list: TaskList)
    }

    override fun getItemCount(): Int = lists.size

    fun addDefaultList() {
        val defaultList = TaskList(defaultListName)
        lists.add(defaultList)
        notifyItemInserted(lists.size - 1)
        listener.onListChanged(defaultList)
    }

    fun removeListById(id: Long) {
        lists.removeAll { it.id == id }
        notifyDataSetChanged()
    }

    inner class TaskListViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)
}