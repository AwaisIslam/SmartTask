package com.ak.smarttask.model

import com.google.gson.annotations.SerializedName

data class Task(
    val id: String,
    @SerializedName("TargetDate") val targetDate: String?,
    @SerializedName("DueDate") val dueDate: String?,
    @SerializedName("Title") val title: String?,
    @SerializedName("Description") val description: String?,
    @SerializedName("Priority") val priority: Int
)
