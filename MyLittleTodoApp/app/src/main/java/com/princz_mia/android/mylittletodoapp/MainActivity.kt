package com.princz_mia.android.mylittletodoapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.princz_mia.android.mylittletodoapp.adapter.TaskItemAdapter
import com.princz_mia.android.mylittletodoapp.adapter.TaskListAdapter
import com.princz_mia.android.mylittletodoapp.data.TaskItem
import com.princz_mia.android.mylittletodoapp.data.TaskItemDatabase
import com.princz_mia.android.mylittletodoapp.data.TaskList
import com.princz_mia.android.mylittletodoapp.data.TaskListDatabase
import com.princz_mia.android.mylittletodoapp.databinding.ActivityMainBinding
import com.princz_mia.android.mylittletodoapp.fragments.MoreOptionsDialogFragment
import com.princz_mia.android.mylittletodoapp.fragments.NewTaskItemDialogFragment
import com.princz_mia.android.mylittletodoapp.fragments.NewTaskListDialogFragment
import java.io.Serializable
import kotlin.concurrent.thread

@Suppress("DEPRECATION")
class MainActivity() : AppCompatActivity(),
    TaskListAdapter.TaskListClickListener,
    TaskItemAdapter.TaskItemClickListener,
    NewTaskListDialogFragment.NewTaskListDialogListener,
    NewTaskItemDialogFragment.NewTaskItemDialogListener,
    MoreOptionsDialogFragment.MoreOptionsDialogListener,
    Serializable
{
    private lateinit var binding: ActivityMainBinding

    private lateinit var taskItemDatabase: TaskItemDatabase
    private lateinit var taskItemAdapter: TaskItemAdapter

    private lateinit var taskListDatabase: TaskListDatabase
    private lateinit var taskListAdapter: TaskListAdapter

    private lateinit var currentTaskList: TaskList

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskItemDatabase = TaskItemDatabase.getDatabase(applicationContext)
        taskListDatabase = TaskListDatabase.getDatabase(applicationContext)

        initRecyclerViewAndCurrentList()

        val sharedPreference = getSharedPreferences("PREFERENCE_SORT", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()

        binding.sortSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.sortBy)
        )

        binding.sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                val sortType = sharedPreference.getString("sort", "Sort by Creation Date")
                when (sortType) {
                    "Sort by Name" -> taskItemAdapter.sortByName()
                    "Sort by Priority" -> taskItemAdapter.sortByPriority()
                    "Sort by Deadline Date" -> taskItemAdapter.sortByDeadlineDate()
                    "Sort by Creation Date" -> taskItemAdapter.sortByCreationDate()
                    else -> {
                        print("Undefined behaviour")
                    }
                }
                runOnUiThread {
                    taskItemAdapter.notifyDataSetChanged()
                }
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val sortType = binding.sortSpinner.getItemAtPosition(position)
                when (sortType) {
                    "Sort by Name" -> taskItemAdapter.sortByName()
                    "Sort by Priority" -> taskItemAdapter.sortByPriority()
                    "Sort by Deadline Date" -> taskItemAdapter.sortByDeadlineDate()
                    "Sort by Creation Date" -> taskItemAdapter.sortByCreationDate()
                    else -> {
                        print("Undefined behaviour")
                    }
                }
                runOnUiThread {
                    taskItemAdapter.notifyDataSetChanged()
                }
                editor.putString("sort", sortType.toString())
                editor.commit()
            }

        }

        binding.addListButton.setOnClickListener {
            NewTaskListDialogFragment().show(
                supportFragmentManager,
                NewTaskListDialogFragment.TAG
            )
        }

        binding.addTaskButton.setOnClickListener{
            NewTaskItemDialogFragment(taskListAdapter).show(
                supportFragmentManager,
                NewTaskItemDialogFragment.TAG
            )
        }

        binding.moreOptionsButton.setOnClickListener{
            MoreOptionsDialogFragment(currentTaskList).show(
                supportFragmentManager,
                MoreOptionsDialogFragment.TAG
            )
        }
    }

    override fun onListCreated(newList: TaskList) {
        thread {
            val insertId = taskListDatabase.taskListDao().insert(newList)
            newList.id = insertId
            runOnUiThread {
                taskListAdapter.addList(newList)
                currentTaskList?.let { showItemsForSpecificList(it) }
            }
        }
    }

    override fun onListChanged(list: TaskList) {
        thread {
            taskListDatabase.taskListDao().update(list)
            Log.d("MainActivity", "TaskList update was successful")
            runOnUiThread {
                currentTaskList?.let { showItemsForSpecificList(it) }
            }
        }
    }

    override fun onListRemoved(list: TaskList) {
        thread {
            taskListDatabase.taskListDao().deleteItem(list)
            runOnUiThread {
                taskListAdapter.removeList(list)
                taskListAdapter.notifyDataSetChanged()
                currentTaskList?.let { showItemsForSpecificList(it) }
            }
        }
    }

    override fun onListClick(list: TaskList) {
        currentTaskList = list
        runOnUiThread{
            currentTaskList?.let { showItemsForSpecificList(it) }
        }
    }

    override fun onTaskItemCreated(newItem: TaskItem) {
        thread {
            val insertId = taskItemDatabase.taskItemDao().insert(newItem)
            newItem.id = insertId
            runOnUiThread {
                taskItemAdapter.addItem(newItem)
                currentTaskList?.let { showItemsForSpecificList(it) }
            }
        }
    }

    override fun onTaskItemChanged(item: TaskItem) {
        thread {
            taskItemDatabase.taskItemDao().update(item)
            Log.d("MainActivity", "TaskItem update was successful")
            runOnUiThread {
                taskItemAdapter.notifyDataSetChanged()
                currentTaskList?.let { showItemsForSpecificList(it) }
            }
        }
    }

    override fun onTaskItemRemoved(item: TaskItem) {
        thread {
            taskItemDatabase.taskItemDao().deleteItem(item)
            runOnUiThread {
                taskItemAdapter.removeItem(item)
                taskItemAdapter.notifyDataSetChanged()
                currentTaskList?.let { showItemsForSpecificList(it) }
            }
        }
    }

    override fun onTaskItemClick(item: TaskItem) {
        val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra("taskItem", item)
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val task = data?.getSerializableExtra("updatedTask") as TaskItem
        onTaskItemChanged(task)
        runOnUiThread{
            currentTaskList?.let { showItemsForSpecificList(it) }
        }
    }

    override fun onListNameEdited(currentTaskList: TaskList) {
        thread {
            taskListDatabase.taskListDao().update(currentTaskList)
            runOnUiThread {
                taskItemAdapter.notifyDataSetChanged()
                currentTaskList?.let { showItemsForSpecificList(it) }
            }
        }
    }

    override fun onCurrentListDeleted() {
        thread {
            taskItemDatabase.taskItemDao().deleteItemsByListId(currentTaskList.id)
            runOnUiThread {
                taskItemAdapter.removeItemsByListId(currentTaskList.id)
                taskItemAdapter.notifyDataSetChanged()
            }
        }

        onListRemoved(currentTaskList)
        initRecyclerViewAndCurrentList()
    }

    override fun onAllCompletedItemRemoved() {
        thread {
            taskItemDatabase.taskItemDao().deleteAllCompletedItems()
            runOnUiThread {
                taskItemAdapter.removeAllCompletedItem()
                taskItemAdapter.notifyDataSetChanged()
                currentTaskList?.let { showItemsForSpecificList(it) }
            }
        }
    }

    private fun initRecyclerViewAndCurrentList() {
        taskItemAdapter = TaskItemAdapter(this)
        taskListAdapter = TaskListAdapter(this)
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.tasksRecyclerView.adapter = taskItemAdapter
        binding.listRecyclerView.adapter = taskListAdapter

        thread {
            taskListDatabase.initializeDefaultList()
            currentTaskList = taskListDatabase.taskListDao().getByName("Default List")
            runOnUiThread {
                showItemsForSpecificList(currentTaskList)
            }
        }
    }

    private fun showItemsForSpecificList(taskList : TaskList) {
        thread {
            val tasks = taskItemDatabase.taskItemDao().getByListId(taskList.id)
            val lists = taskListDatabase.taskListDao().getAll()
            runOnUiThread {
                taskListAdapter.update(lists)
                taskItemAdapter.update(tasks)
            }
        }
    }
}