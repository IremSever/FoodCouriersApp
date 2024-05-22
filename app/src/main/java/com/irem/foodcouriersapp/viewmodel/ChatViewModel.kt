package com.irem.foodcouriersapp.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irem.foodcouriersapp.model.data.Chat
import com.irem.foodcouriersapp.model.data.ChatData
import com.irem.foodcouriersapp.viewmodel.event.ChatUIEvent
import com.irem.foodcouriersapp.viewmodel.state.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class ChatViewModel : ViewModel() {

    private val _chatState = MutableStateFlow(DataState()) // Internal mutable state for chat data
    val chatState = _chatState.asStateFlow() // Public state as StateFlow
    private val _showTyping = MutableStateFlow(false) // Internal mutable state for typing animation
    val showTyping: StateFlow<Boolean> = _showTyping // Public state as StateFlow

    init {
        // AI introduction message
        _chatState.update { currentState ->
            currentState.copy(
                chatList = currentState.chatList.toMutableList().apply {
                    add(Chat("Hello! I am Food Couriers Application. Before we start, what is your name?", null, Date(), false))
                }
            )
        }
    }

    fun onEvent(event: ChatUIEvent) {
        when (event) {
            is ChatUIEvent.SendPrompt -> {
                if (event.prompt.isNotEmpty()) {
                    addPrompt(event.prompt, event.bitmap)
                    _showTyping.value = true // Show typing animation

                    if (_chatState.value.isAwaitingName) {
                        val name = event.prompt.split(" ").first()
                        val responseMessage = "Hello! $name. I can converse in your preferred language. How may I help you today?"
                        addResponse(responseMessage)
                        _chatState.update { it.copy(isAwaitingName = false) }
                        _showTyping.value = false // Hide typing animation
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
                    add(0, Chat(prompt, bitmap, Date(), true)) // Add user's message to chat list
                },
                prompt = "", // Clear the prompt after sending
                bitmap = null // Clear the bitmap after sending
            )
        }
    }

    private fun addResponse(response: String) {
        _chatState.update {
            it.copy(
                chatList = it.chatList.toMutableList().apply {
                    add(0, Chat(response, null, Date(), false)) // Add AI's response to chat list
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
                        add(0, chat) // Add AI's response to chat list
                    }
                )
            }
            _showTyping.value = false // Hide typing animation
        }
    }

    private fun getResponseWithImage(prompt: String, bitmap: Bitmap) {
        viewModelScope.launch {
            val chat = ChatData.getResponseWithImage(prompt, bitmap)
            _chatState.update {
                it.copy(
                    chatList = it.chatList.toMutableList().apply {
                        add(0, chat) // Add AI's response with image to chat list
                    }
                )
            }
            _showTyping.value = false // Hide typing animation
        }
    }
}
