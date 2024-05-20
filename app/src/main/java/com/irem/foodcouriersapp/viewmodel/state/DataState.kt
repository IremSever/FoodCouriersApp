package com.irem.foodcouriersapp.viewmodel.state

import android.graphics.Bitmap
import com.irem.foodcouriersapp.model.data.Chat
import java.util.Date

data class DataState(
    val chatList: MutableList<Chat> = mutableListOf(),
    val prompt : String = "",
    val bitmap: Bitmap? = null,
    val isAwaitingName: Boolean = true

)

