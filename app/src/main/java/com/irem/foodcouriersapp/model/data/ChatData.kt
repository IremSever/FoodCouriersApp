package com.irem.foodcouriersapp.model.data

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

object ChatData {

    val API_KEY = "NEW_API_KEY"

    suspend fun getResponse(prompt: String): Chat{

        val generativeModel = GenerativeModel(
            modelName = "gemini-pro", apiKey = API_KEY
        )
        try {
            val response = withContext(Dispatchers.IO){
                generativeModel.generateContent(prompt)
            }

            return Chat(
                prompt= response.text ?: "Error",
                bitmap = null,
                isFromUser = false,
                sentTime = Date()

            )

        }catch (e: Exception){

            return Chat(
                prompt= e.message ?: "Error",
                bitmap = null,
                isFromUser = false,
                sentTime = Date()

            )

        }
    }

    suspend fun getResponseWithImage(prompt: String, bitmap: Bitmap): Chat{

        val generativeModel = GenerativeModel(
            modelName = "gemini-pro-vision", apiKey = API_KEY
        )
        try {
            val inputContent =  content {
                image(bitmap) // I just
                text(prompt)
            }

            val response = withContext(Dispatchers.IO){
                generativeModel.generateContent(inputContent)
            }

            return Chat(
                prompt= response.text ?: "Error",
                bitmap = null,
                isFromUser = false,
                sentTime = Date()

            )

        }catch (e: Exception){
            return Chat(
                prompt= e.message ?: "Error",
                bitmap = null,
                isFromUser = false,
                sentTime = Date()

            )
        }
    }
}