package com.irem.foodcouriersapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.irem.foodcouriersapp.ui.theme.FoodCouriersAppTheme
import com.irem.foodcouriersapp.ui.theme.FoodCouriersAppThemeDark
import com.irem.foodcouriersapp.ui.theme.TopAppBarColor
import com.irem.foodcouriersapp.ui.theme.TopAppBarColorDark
import com.irem.foodcouriersapp.ui.theme.TopBarTextColor
import com.irem.foodcouriersapp.ui.theme.TopBarTextColorDark
import com.irem.foodcouriersapp.view.ChatScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MainActivity : ComponentActivity() {

    private val uriState = MutableStateFlow("")

    private val imagePicker =
        registerForActivityResult<PickVisualMediaRequest, Uri?>(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let {
                uriState.update { uri.toString() }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isSystemInDarkTheme = isSystemInDarkTheme()

            if (isSystemInDarkTheme) {
                FoodCouriersAppThemeDark(darkTheme = true) {
                    MainContent()
                }
            } else {
                FoodCouriersAppTheme(darkTheme = false) {
                    MainContent()
                }
            }
        }
    }

    @Composable
    private fun MainContent() {
        val darkTheme = isSystemInDarkTheme()

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = if(darkTheme) TopAppBarColorDark else TopAppBarColor)
                            .height(60.dp)
                            .padding(horizontal = 25.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            fontSize = 20.sp,
                            color = if(darkTheme) TopBarTextColorDark else TopBarTextColor
                        )
                    }
                }
            ) {
                ChatScreen(paddingValues = it, imagePicker = imagePicker, uriState = uriState)
            }
        }
    }
}
