package com.ak.smarttask.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ak.smarttask.R
import com.ak.smarttask.model.Task
import com.ak.smarttask.ui.theme.LightWhite
import com.ak.smarttask.ui.theme.red
import com.ak.smarttask.ui.theme.yellow
import com.ak.smarttask.utils.Constants.AMSI_PRO_BOLD
import com.ak.smarttask.utils.Constants.AMSI_PRO_REGULAR
import com.ak.smarttask.utils.Constants.DATE_PATTERN
import com.ak.smarttask.viewmodel.TaskViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    innerPadding: PaddingValues,
    navController: NavController
) {

  var currentDate by remember { mutableStateOf(LocalDate.now()) }

  val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
  val formattedDate = currentDate.format(formatter)

  val tasks by viewModel.tasks.collectAsState()

  val tasksForCurrentDate = tasks.filter { task -> task.targetDate == formattedDate }

  Column(modifier = Modifier.fillMaxSize().background(yellow)) {
    TaskToolbar(
        currentDate = currentDate,
        onLeftArrowClick = { currentDate = currentDate.minusDays(1) },
        onRightArrowClick = { currentDate = currentDate.plusDays(1) })
    if (tasksForCurrentDate.isEmpty()) {
      NoTaskMessage()
    } else {
      TaskList(viewModel, navController, tasksForCurrentDate)
    }
  }
}

@Composable
fun NoTaskMessage() {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
          Image(
              painter = painterResource(id = R.drawable.empty_screen),
              contentDescription = stringResource(R.string.no_tasks_image_content_description),
              contentScale = ContentScale.Fit)

          Spacer(modifier = Modifier.height(16.dp))

          Text(
              text = stringResource(R.string.no_tasks_for_today),
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White)
        }
  }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TaskList(viewModel: TaskViewModel, navController: NavController, tasks: List<Task>) {
  LazyColumn(
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(tasks.sortedWith(compareBy({ it.dueDate }, { it.priority }))) { task ->
          TaskItem(task = task) {
            viewModel.selectTask(task.id)
            navController.navigate("taskDetail/${task.id}")
          }
        }
      }
}

@Composable
fun TaskToolbar(
    currentDate: LocalDate,
    onLeftArrowClick: () -> Unit,
    onRightArrowClick: () -> Unit
) {
  Box(modifier = Modifier.fillMaxWidth().padding(top = 32.dp).height(56.dp).background(yellow)) {
    Row(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = onLeftArrowClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.previous),
                tint = Color.White)
          }

          Text(
              text = currentDate.toString(),
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White,
              modifier = Modifier.align(Alignment.CenterVertically))

          IconButton(onClick = onRightArrowClick) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = stringResource(R.string.next),
                tint = Color.White)
          }
        }
  }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskItem(task: Task, onClick: () -> Unit) {

  val priorityColor =
      when (task.priority) {
        0 -> Color.Green
        1 -> Color.Yellow
        2 -> Color.Magenta
        else -> Color.Red
      }

  val dueDate = task.dueDate?.let { LocalDate.parse(it, DateTimeFormatter.ISO_DATE) }
  val today = LocalDate.now()
  val daysLeft =
      dueDate?.let { ChronoUnit.DAYS.between(today, it) }
          ?: stringResource(id = R.string.no_due_date)

  Card(
      modifier = Modifier.fillMaxWidth().clickable { onClick() }.wrapContentHeight(),
      colors = CardDefaults.cardColors(containerColor = priorityColor),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().background(Color.White).padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title ?: stringResource(R.string.no_title),
                    fontFamily = AMSI_PRO_BOLD,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = red,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis)
                Divider(color = LightWhite, modifier = Modifier.padding(top = 8.dp).height(1.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().background(Color.White).padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                      Text(
                          text = stringResource(R.string.due_date),
                          fontSize = 12.sp,
                          fontFamily = AMSI_PRO_BOLD,
                          color = Color.Gray)
                      Text(
                          text = stringResource(R.string.days_left),
                          fontSize = 12.sp,
                          fontFamily = AMSI_PRO_BOLD,
                          color = Color.Gray)
                    }
                Row(
                    modifier = Modifier.fillMaxWidth().background(Color.White).padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                      Text(
                          text = task.dueDate ?: stringResource(R.string.no_due_date),
                          fontSize = 12.sp,
                          fontFamily = AMSI_PRO_REGULAR,
                          fontWeight = FontWeight.Bold,
                          color = red)
                      Text(
                          text = daysLeft.toString(),
                          fontSize = 12.sp,
                          fontFamily = AMSI_PRO_REGULAR,
                          fontWeight = FontWeight.Bold,
                          color = red)
                    }
              }
            }
      }
}
