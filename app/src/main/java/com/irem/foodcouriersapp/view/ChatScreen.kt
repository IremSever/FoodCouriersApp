package com.irem.foodcouriersapp.view


import android.graphics.Bitmap
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrowseGallery
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.irem.foodcouriersapp.ui.theme.ChatBotResponseColor
import com.irem.foodcouriersapp.ui.theme.ChatBotResponseColorDark
import com.irem.foodcouriersapp.ui.theme.PlaceHolderColor
import com.irem.foodcouriersapp.ui.theme.PlaceHolderColorDark
import com.irem.foodcouriersapp.ui.theme.TextFieldBackgroundColor
import com.irem.foodcouriersapp.ui.theme.TextFieldBackgroundColorDark
import com.irem.foodcouriersapp.ui.theme.TopAppBarColor
import com.irem.foodcouriersapp.ui.theme.TopAppBarColorDark
import com.irem.foodcouriersapp.ui.theme.UserRequestColor
import com.irem.foodcouriersapp.viewmodel.ChatViewModel
import com.irem.foodcouriersapp.viewmodel.event.ChatUIEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(paddingValues: PaddingValues, imagePicker: ActivityResultLauncher<PickVisualMediaRequest>, uriState: MutableStateFlow<String>) {

    val chatViewModel = viewModel<ChatViewModel>()  // ViewModel instance

    val chatState = chatViewModel.chatState.collectAsState().value  // Collect chat state

    val keyboardController = LocalSoftwareKeyboardController.current  // Keyboard controller

    val showTyping by chatViewModel.showTyping.collectAsState()  // Show typing state

    val bitmap = getBitmap(uriState)  // Get bitmap from URI

    val listState = rememberLazyListState()  // Remember lazy list state

    val darkTheme = isSystemInDarkTheme()

    LaunchedEffect(chatState.chatList.size) {
        if (chatState.chatList.isNotEmpty()) {
            listState.animateScrollToItem(0) // Scroll to top when new message arrives
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding()),
        verticalArrangement = Arrangement.Bottom
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Top,
            reverseLayout = true
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))  // Spacer for padding
            }
            itemsIndexed(chatState.chatList) { index, chat ->
                if (chat.isFromUser) {
                    UserChatItem(
                        prompt = chat.prompt, bitmap = chat.bitmap, sentTime = chat.sentTime
                    )
                } else {
                    ModelChatItem(response = chat.prompt, sentTime = chat.sentTime)
                }
            }
        }

        if (showTyping) {
            TypingAnimation() // Show typing animation
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 6.dp, start = 5.dp, end = 5.dp, top = 6.dp),
                value = chatState.prompt,
                onValueChange = {
                    chatViewModel.onEvent(ChatUIEvent.UpdatePrompt(it)) // Update prompt
                },
                placeholder = {
                    Text(text = "Type a prompt")
                },
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                imagePicker.launch(
                                    PickVisualMediaRequest
                                        .Builder()
                                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        .build()
                                )
                            },
                        contentAlignment = Alignment.Center,

                    ) {
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "picked image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.clip(RoundedCornerShape(6.dp))
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.BrowseGallery,
                                contentDescription = "Add Photo",
                                tint = if (darkTheme) ChatBotResponseColorDark else ChatBotResponseColor
                            )
                        }
                    }
                },
                trailingIcon = {
                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                chatViewModel.onEvent(
                                    ChatUIEvent.SendPrompt(
                                        chatState.prompt,
                                        bitmap
                                    )
                                )
                                uriState.update { "" }  // Clear URI state
                                keyboardController?.hide()  // Hide keyboard
                            },
                        imageVector = Icons.Rounded.Send,
                        contentDescription = "Send prompt",
                        tint = if (darkTheme) ChatBotResponseColorDark else ChatBotResponseColor
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        chatViewModel.onEvent(
                            ChatUIEvent.SendPrompt(
                                chatState.prompt,
                                bitmap
                            )
                        )
                        uriState.update { "" }  // Clear URI state
                        keyboardController?.hide()  // Hide keyboard
                    }
                ),
                singleLine = true,
                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(
                    focusedTextColor = if(darkTheme) TopAppBarColorDark else TopAppBarColor,
                    unfocusedTextColor = if(darkTheme) TopAppBarColorDark else TopAppBarColor,
                    disabledTextColor = if(darkTheme) TopAppBarColorDark else TopAppBarColor,
                    disabledIndicatorColor = if(darkTheme) TopAppBarColorDark else TopAppBarColor,
                    focusedIndicatorColor = if(darkTheme) TopAppBarColorDark else TopAppBarColor,
                    unfocusedIndicatorColor = if(darkTheme) TopAppBarColorDark else TopAppBarColor,
                    containerColor = if(darkTheme) TextFieldBackgroundColorDark else TextFieldBackgroundColor,
                    disabledPlaceholderColor = if(darkTheme) PlaceHolderColorDark else PlaceHolderColor,
                    focusedPlaceholderColor = if(darkTheme) PlaceHolderColorDark else PlaceHolderColor,
                    unfocusedPlaceholderColor = if(darkTheme) PlaceHolderColorDark else PlaceHolderColor,
                    cursorColor = if(darkTheme) TopAppBarColorDark else TopAppBarColor,
                    selectionColors = TextSelectionColors(
                        handleColor = if(darkTheme) TopAppBarColorDark else TopAppBarColor,
                        backgroundColor = if(darkTheme) PlaceHolderColorDark else PlaceHolderColor,
                    )
                )
            )
        }
    }
}

@Composable
private fun getBitmap(uriState: MutableStateFlow<String>): Bitmap? {
    val uri = uriState.collectAsState().value   // Collect URI state

    val imageState: AsyncImagePainter.State = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(uri)
            .size(Size.ORIGINAL)
            .build()
    ).state

    if (imageState is AsyncImagePainter.State.Success) {
        return imageState.result.drawable.toBitmap()  // Return bitmap if image load is successful
    }

    return null
}


