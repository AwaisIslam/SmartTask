package com.ak.smarttask.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ak.smarttask.R
import com.ak.smarttask.ui.theme.LightWhite
import com.ak.smarttask.ui.theme.green
import com.ak.smarttask.ui.theme.orange
import com.ak.smarttask.ui.theme.red
import com.ak.smarttask.ui.theme.yellow
import com.ak.smarttask.utils.Constants
import com.ak.smarttask.utils.Constants.STATUS_RESOLVED
import com.ak.smarttask.utils.Constants.STATUS_UNRESOLVED
import com.ak.smarttask.viewmodel.TaskViewModel

@Composable
fun TaskDetailScreenToolbar(
    onLeftArrowClick: () -> Unit,
) {
  Box(modifier = Modifier.fillMaxWidth().padding(top = 32.dp).height(50.dp).background(yellow)) {
    Row(
        modifier = Modifier.fillMaxSize(),
    ) {
      IconButton(onClick = onLeftArrowClick) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = stringResource(R.string.previous),
            tint = White)
      }

      Text(
          text = stringResource(R.string.task_detail),
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = White,
          fontFamily = Constants.amsiproRegularFont,
          modifier = Modifier.align(Alignment.CenterVertically))
    }
  }
}

@Composable
fun TaskDetailScreen(
    viewModel: TaskViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {

  val task = viewModel.selectedTask.value
  val taskStatus = viewModel.taskStatus.value

  val statusColor =
      when (taskStatus) {
        STATUS_RESOLVED -> green
        STATUS_UNRESOLVED -> red
        else -> orange
      }

  task?.let {
    val shape = RoundedCornerShape(8.dp)
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 16.dp).background(Color(0xFFFFDE61)),
    ) {
      TaskDetailScreenToolbar(onLeftArrowClick = { navController.popBackStack() })

      Box(
          modifier =
              modifier
                  .fillMaxWidth()
                  .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                  .background(Color(0xFFFFDE61), shape = shape),
      ) {
        Image(
            painter = painterResource(id = R.drawable.task_details),
            contentDescription = stringResource(id = R.string.no_tasks_image_content_description),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxWidth().clip(shape))
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        ) {
          it.title?.let { title ->
            Text(
                modifier = modifier.padding(top = 16.dp, start = 8.dp),
                text = title,
                fontSize = 22.sp,
                fontFamily = Constants.amsiproBoldFont,
                fontWeight = FontWeight.Bold,
                color = if (taskStatus == STATUS_RESOLVED) green else red)
          }

          Divider(color = LightWhite, modifier = Modifier.padding(8.dp).height(1.dp))

          Row(
              modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp),
              horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                  Text(
                      text = stringResource(id = R.string.due_date),
                      fontSize = 14.sp,
                      color = Black)
                  it.dueDate?.let { dueDate ->
                    Text(
                        text = dueDate, color = red, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                  }
                }

                Column(horizontalAlignment = Alignment.End) {
                  Text(
                      text = stringResource(id = R.string.days_left),
                      fontSize = 14.sp,
                      color = Black)
                  it.dueDate?.let { dueDate ->
                    Text(
                        text = dueDate, color = red, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                  }
                }
              }
          Divider(color = LightWhite, modifier = Modifier.padding(8.dp).height(1.dp))

          it.description?.let { description ->
            Text(
                text = description,
                color = Black,
                fontSize = 14.sp,
                fontFamily = Constants.amsiproRegularFont,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp))
          }

          Divider(
              color = LightWhite,
              modifier =
                  Modifier.padding(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 16.dp)
                      .height(1.dp))

          Text(
              text = taskStatus,
              color = statusColor,
              fontSize = 14.sp,
              fontFamily = Constants.amsiproBoldFont,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(start = 8.dp, end = 8.dp))
        }
      }

      when (taskStatus) {
        STATUS_RESOLVED,
        STATUS_UNRESOLVED -> ShowStatusImage(taskStatus)
        "" -> ShowStatusButtons(modifier, viewModel)
      }
    }
  }
}

@Composable
private fun ShowStatusImage(taskStatus: String) {
  val taskResolveImage =
      if (taskStatus == STATUS_RESOLVED) {
        painterResource(id = R.drawable.sign_resolved)
      } else {
        painterResource(id = R.drawable.unresolved_sign)
      }

  Image(
      painter = taskResolveImage,
      contentDescription = stringResource(id = R.string.no_tasks_image_content_description),
      modifier = Modifier.fillMaxWidth().padding(100.dp))
}

@Composable
private fun ShowStatusButtons(modifier: Modifier, viewModel: TaskViewModel) {
  Row(
      modifier = modifier.fillMaxWidth().padding(top = 8.dp),
      horizontalArrangement = Arrangement.SpaceEvenly) {
        Button(
            onClick = { viewModel.updateTaskStatus(STATUS_RESOLVED) },
            colors = ButtonDefaults.buttonColors(containerColor = green),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1f).padding(start = 16.dp, end = 8.dp)) {
              Text(
                  stringResource(R.string.resolve),
                  color = White,
                  fontSize = 18.sp,
                  fontFamily = Constants.amsiproRegularFont,
                  fontWeight = FontWeight.Bold)
            }

        Button(
            onClick = { viewModel.updateTaskStatus(STATUS_UNRESOLVED) },
            colors = ButtonDefaults.buttonColors(containerColor = red),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1f).padding(start = 8.dp, end = 16.dp)) {
              Text(
                  stringResource(R.string.cant_resolve),
                  color = White,
                  fontSize = 18.sp,
                  fontFamily = Constants.amsiproRegularFont,
                  fontWeight = FontWeight.Bold)
            }
      }
}
