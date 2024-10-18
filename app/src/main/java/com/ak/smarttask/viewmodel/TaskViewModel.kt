package com.ak.smarttask.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ak.smarttask.model.Task
import com.ak.smarttask.model.TaskStatus
import com.ak.smarttask.repository.TaskRepository
import com.ak.smarttask.utils.Constants.LOG_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskRepository: TaskRepository) : ViewModel() {

  private val _tasks = MutableStateFlow<List<Task>>(emptyList())
  val tasks: StateFlow<List<Task>> = _tasks

  private val _selectedTask = mutableStateOf<Task?>(null)
  val selectedTask: State<Task?> = _selectedTask

  private val _taskStatus = mutableStateOf(TaskStatus.UNRESOLVED)
  val taskStatus = _taskStatus

  init {
    fetchTodayTasks()
  }

  fun selectTask(taskId: String) {
    _selectedTask.value = tasks.value.find { it.id == taskId }
  }

  fun updateTaskStatus(taskStatus: TaskStatus) {
    _taskStatus.value = taskStatus
  }

  private fun updateTaskInList(updatedTask: Task) {
    _tasks.value = tasks.value.map { task -> if (task.id == updatedTask.id) updatedTask else task }
  }

  private fun fetchTodayTasks() {
    viewModelScope.launch {
      try {
        val response = taskRepository.fetchTodayTasks()
        _tasks.value = response.tasks
        Log.d(LOG_TAG, "fetchTodayTasks: ${tasks.value}")
      } catch (e: HttpException) {
        _tasks.value = emptyList()
        Log.d(LOG_TAG, "fetchTodayTasks: ${tasks.value}")
        Log.e(LOG_TAG, "fetchTodayTasks: HTTP Exception: ${e.message}")
      } catch (e: Exception) {
        _tasks.value = emptyList()
        Log.d(LOG_TAG, "fetchTodayTasks: ${tasks.value}")
        Log.d(LOG_TAG, "fetchTodayTasks: Exception: ${e.message}")
      }
    }
  }
}
