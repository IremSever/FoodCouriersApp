package com.irem.foodcouriersapp.model.data

import android.graphics.Bitmap
import java.util.Date

data class Chat(
    val prompt: String,
    val bitmap: Bitmap?,
    val sentTime: Date = Date(),
    val isFromUser: Boolean
)