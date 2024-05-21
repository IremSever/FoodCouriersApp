package com.irem.foodcouriersapp.viewmodel

import com.irem.foodcouriersapp.viewmodel.event.ChatUIEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {


    private lateinit var viewModel: ChatViewModel

    @Before
    fun setUp() {
        viewModel = ChatViewModel()

    }

    @Test
    fun testInitialMessage() {
        val chatState = viewModel.chatState.value
        assertNotNull(chatState)
        assertFalse(chatState!!.chatList.isEmpty())
        assertEquals("Hello! I am Food Couriers Application. Before we start, what is your name?", chatState.chatList[0].prompt)
    }

    @Test
    fun testSendPrompt() = runTest {
        val prompt = "John"
        viewModel.onEvent(ChatUIEvent.SendPrompt(prompt, null))

        val chatState = viewModel.chatState.value
        assertNotNull(chatState)
        assertFalse(chatState!!.chatList.isEmpty())
        assertEquals(prompt, chatState.chatList[1].prompt)
    }

    @Test
    fun testUpdatePrompt() {
        val newPrompt = "Hello!"
        viewModel.onEvent(ChatUIEvent.UpdatePrompt(newPrompt))

        val chatState = viewModel.chatState.value
        assertNotNull(chatState)
        assertEquals(newPrompt, chatState!!.prompt)
    }

}
