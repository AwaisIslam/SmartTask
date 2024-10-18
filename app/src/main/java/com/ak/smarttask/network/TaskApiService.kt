package com.ak.smarttask.network

import com.ak.smarttask.model.TaskResponse
import retrofit2.http.GET

interface TaskApiService {
  @GET("/") suspend fun getTodayTasks(): TaskResponse
}
