package com.ak.smarttask.utils

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.ak.smarttask.R

object Constants {
  const val LOG_TAG = "SmartTask"
  const val BASE_URL = "https://demo1414406.mockable.io/"
  val amsiproBoldFont = FontFamily(Font(R.font.amsiprobold, FontWeight.Bold))
  val amsiproRegularFont = FontFamily(Font(R.font.amsiproregular, FontWeight.Normal))
  const val STATUS_RESOLVED = "Resolved"
  const val STATUS_UNRESOLVED = "Unresolved"
  const val DATE_PATTERN = "yyyy-MM-dd"
}
