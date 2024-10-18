package com.ak.smarttask

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ak.smarttask.screens.SplashScreen
import com.ak.smarttask.screens.TaskDetailScreen
import com.ak.smarttask.screens.TaskListScreen
import com.ak.smarttask.ui.theme.SmartTaskTheme
import com.ak.smarttask.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val taskViewModel: TaskViewModel by viewModels()
  private lateinit var navController: NavHostController

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    handleBackPressCallback()
    setContent {
      SmartTaskTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          navController = rememberNavController()
          NavHost(navController = navController, startDestination = "splash_screen") {
            composable("splash_screen") { SplashScreen(navController = navController) }
            composable("taskList") {
              TaskListScreen(
                  viewModel = taskViewModel,
                  innerPadding = innerPadding,
                  navController = navController)
            }

            composable(route = "taskDetail") {
              TaskDetailScreen(viewModel = taskViewModel, navController)
            }
          }
        }
      }
    }
  }

  private fun handleBackPressCallback() {
    onBackPressedDispatcher.addCallback(
        this,
        object : OnBackPressedCallback(true) {
          override fun handleOnBackPressed() {
            if (::navController.isInitialized &&
                navController.currentBackStackEntry?.destination?.route == "taskList") {
              finish()
            } else {
              navController.popBackStack()
            }
          }
        })
  }
}
