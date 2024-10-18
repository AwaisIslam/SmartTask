package com.ak.smarttask.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ak.smarttask.R
import com.ak.smarttask.ui.theme.yellow

@Composable
fun SplashScreen(navController: NavController) {

  LaunchedEffect(Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        navController.navigate("taskList"){
            popUpTo("splash_screen"){inclusive = true}
        } }, 3000)
  }

  Box(modifier = Modifier.fillMaxSize().background(yellow)) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "App Logo",
        modifier = Modifier.align(Alignment.Center))

    Image(
        painter = painterResource(id = R.drawable.intro_illustration),
        contentDescription = "Intro Illustration",
        modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp))
  }
}
