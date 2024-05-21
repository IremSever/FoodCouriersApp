package com.irem.foodcouriersapp.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irem.foodcouriersapp.model.data.Chat
import com.irem.foodcouriersapp.model.data.ChatData
import com.irem.foodcouriersapp.viewmodel.event.ChatUIEvent
import com.irem.foodcouriersapp.viewmodel.state.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date


class ChatViewModel : ViewModel(){

    private val _chatState = MutableStateFlow(DataState())
    val chatState = _chatState.asStateFlow()

    init {
        // AI'nin kendini tanıtması
        _chatState.update { currentState ->
            currentState.copy(
                chatList = currentState.chatList.toMutableList().apply {
                    add(Chat("Hello! I am Food Couriers Application. Before we start, what is your name?", null, Date(),false))
                }
            )
        }
    }

    fun onEvent(event: ChatUIEvent) {
        when (event) {
            is ChatUIEvent.SendPrompt -> {
                if (event.prompt.isNotEmpty()) {
                    addPrompt(event.prompt, event.bitmap)

                    if (_chatState.value.isAwaitingName) {
                        val name = event.prompt.split(" ").first()
                        val responseMessage = "Hello! $name. I can converse in your preferred language. How may I help you today?"
                        addResponse(responseMessage)
                        _chatState.update { it.copy(isAwaitingName = false) }
                    } else {
                        if (event.bitmap != null) {
                            getResponseWithImage(event.prompt, event.bitmap)
                        } else {
                            getResponse(event.prompt)
                        }
                    }
                }
            }
            is ChatUIEvent.UpdatePrompt -> {
                _chatState.update { it.copy(prompt = event.newPrompt) }
            }
        }
    }

    private fun addPrompt(prompt: String, bitmap: Bitmap?) {
        _chatState.update {
            it.copy(
                chatList = it.chatList.toMutableList().apply {
                    add(0, Chat(prompt, bitmap, Date(),true))
                },
                prompt = "",
                bitmap = null
            )
        }
    }

    private fun addResponse(response: String) {
        _chatState.update {
            it.copy(
                chatList = it.chatList.toMutableList().apply {
                    add(0, Chat(response, null, Date(),false))
                }
            )
        }
    }

    private fun getResponse(prompt: String) {
        viewModelScope.launch {
            val chat = ChatData.getResponse(prompt)
            _chatState.update {
                it.copy(
                    chatList = it.chatList.toMutableList().apply {
                        add(0, chat)
                    }
                )
            }
        }
    }

    private fun getResponseWithImage(prompt: String, bitmap: Bitmap) {
        viewModelScope.launch {
            val chat = ChatData.getResponseWithImage(prompt, bitmap)
            _chatState.update {
                it.copy(
                    chatList = it.chatList.toMutableList().apply {
                        add(0, chat)
                    }
                )
            }
        }
    }

    }