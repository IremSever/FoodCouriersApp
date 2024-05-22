package com.irem.foodcouriersapp.view

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.irem.foodcouriersapp.ui.theme.ChatBotResponseColor
import com.irem.foodcouriersapp.ui.theme.UserRequestColor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun UserChatItem(prompt: String, bitmap: Bitmap?, sentTime: Date) {
    Column(
        modifier = Modifier.padding(start = 100.dp, bottom = 16.dp)  // Padding for user message
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()  // Fill the width of the parent
                .clip(RoundedCornerShape(12.dp))  // Rounded corners
                .background(color = UserRequestColor)  // Background color for user messages
                .padding(14.dp),  // Padding inside the message container
        ) {
            bitmap?.let {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)  // Image height
                        .padding(bottom = 3.dp)
                        .clip(RoundedCornerShape(12.dp)),  // Rounded corners for image
                    contentDescription = "image",  // Content description for accessibility
                    contentScale = ContentScale.Crop,  // Image scaling
                    bitmap = it.asImageBitmap()  // Convert Bitmap to ImageBitmap
                )
            }

            Text(
                text = prompt,  // User's message text
                fontSize = 17.sp,  // Font size
                color = MaterialTheme.colorScheme.onPrimary  // Text color
            )
            Spacer(modifier = Modifier.padding(3.dp))  // Space between message and time
            Text(
                text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(sentTime),  // Message sent time
                fontSize = 12.sp,  // Font size for time
                color = Color.White,  // Text color for time
                modifier = Modifier
                    .align(Alignment.End)  // Align text to the end (right)
                    .padding(end = 2.dp, bottom = 2.dp)  // Padding for the time text
            )
        }
    }
}

@Composable
fun ModelChatItem(response: String, sentTime: Date) {
    Column(
        modifier = Modifier.padding(end = 100.dp, bottom = 16.dp)  // Padding for model message
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()  // Fill the width of the parent
                .clip(RoundedCornerShape(12.dp))  // Rounded corners
                .background(color = ChatBotResponseColor)  // Background color for model responses
                .padding(14.dp)  // Padding inside the message container
        ) {
            Text(
                text = response,  // Model's response text
                fontSize = 17.sp,  // Font size
                color = MaterialTheme.colorScheme.onPrimary  // Text color
            )
            Spacer(modifier = Modifier.padding(3.dp))  // Space between message and time
            Text(
                text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(sentTime),  // Message sent time
                fontSize = 12.sp,  // Font size for time
                color = Color.White,  // Text color for time
                modifier = Modifier
                    .align(Alignment.End)  // Align text to the end (right)
                    .padding(end = 2.dp, bottom = 2.dp)  // Padding for the time text
            )
        }
    }
}
