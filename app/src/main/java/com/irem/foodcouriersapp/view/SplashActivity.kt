package com.irem.foodcouriersapp.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.irem.foodcouriersapp.MainActivity
import com.irem.foodcouriersapp.R
import com.irem.foodcouriersapp.ui.theme.FoodCouriersAppTheme
import com.irem.foodcouriersapp.ui.theme.TopAppBarColor
import com.irem.foodcouriersapp.ui.theme.TopAppBarColorDark
import com.irem.foodcouriersapp.ui.theme.TopBarTextColor
import com.irem.foodcouriersapp.ui.theme.TopBarTextColorDark
import kotlinx.coroutines.*

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FoodCouriersAppTheme {
                SplashScreen()
            }
        }

        // Delay using Coroutine
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000) // 2 seconds delay
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}


@Composable
fun SplashScreen() {
    val darkTheme = isSystemInDarkTheme()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if(darkTheme) TopAppBarColorDark else TopAppBarColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome",
            style = TextStyle(
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Normal,
                fontSize = 35.sp,
                color = if(darkTheme) TopBarTextColorDark else TopBarTextColor,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "to",
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp,
                color = if(darkTheme) TopBarTextColorDark else TopBarTextColor,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Food Couriers",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = if(darkTheme) TopBarTextColorDark else TopBarTextColor,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )

    }
}
@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    FoodCouriersAppTheme {
        SplashScreen()
    }
}