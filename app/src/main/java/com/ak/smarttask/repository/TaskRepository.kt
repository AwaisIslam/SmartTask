package com.ak.smarttask.repository

import com.ak.smarttask.model.TaskResponse
import com.ak.smarttask.network.TaskApiService
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskApiService: TaskApiService) {
  suspend fun fetchTodayTasks(): TaskResponse = taskApiService.getTodayTasks()
}
