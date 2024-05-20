package com.irem.foodcouriersapp.viewmodel.state

import android.graphics.Bitmap
import java.util.Date

data class DataState(
    val chatList: MutableList<Chat> = mutableListOf(),
    val prompt : String = "",
    val bitmap: Bitmap? = null,
    val isAwaitingName: Boolean = true

)

data class Chat(val message: String, val bitmap: Bitmap?, val isUser: Boolean, val sentTime: Date = Date())