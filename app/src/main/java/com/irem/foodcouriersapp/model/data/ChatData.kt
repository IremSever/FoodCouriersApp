package com.irem.foodcouriersapp.model.data

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

object ChatData {
    //API anahtarı
    //Eğer API anatarınız bulunmuyorsa, https://aistudio.google.com/app/apikey linkinden bir API anahtarı oluşturup aşağıdaki "YOUR_API_KEY" kısmına yapıştırarak uygulamayı kullanabilirsiniz.
    val API_KEY = "AIzaSyCxVs98z_QS7F28FFr0irIoa5-DejRufI4"

    // Prompt'a dayalı olarak AI modelinden yanıt almak için bir suspend fonksiyonu
    suspend fun getResponse(prompt: String): Chat {

        // "gemini-pro" modeliyle GenerativeModel oluşturma
        val generativeModel = GenerativeModel(
            modelName = "gemini-pro", apiKey = API_KEY
        )
        try {
            // AI modelini çağırarak yanıtı almak için IO bağlamında coroutine çalıştırma
            val response = withContext(Dispatchers.IO) {
                generativeModel.generateContent(prompt)
            }

            // Yanıtı Chat nesnesi olarak döndürme
            return Chat(
                prompt = response.text ?: "Error", // Yanıt metni veya hata mesajı
                bitmap = null,
                isFromUser = false,
                sentTime = Date() // Mevcut tarih ve saat

            )

        } catch (e: Exception) {
            // Hata durumunda, hata mesajını içeren Chat nesnesi döndürme
            return Chat(
                prompt = e.message ?: "Error",
                bitmap = null,
                isFromUser = false,
                sentTime = Date()
            )
        }
    }

    // Görüntü ve prompt'a dayalı olarak AI modelinden yanıt almak için bir suspend fonksiyonu
    suspend fun getResponseWithImage(prompt: String, bitmap: Bitmap): Chat {

        // "gemini-pro-vision" modeliyle GenerativeModel oluşturma
        val generativeModel = GenerativeModel(
            modelName = "gemini-pro-vision", apiKey = API_KEY
        )
        try {
            // Görüntü ve metni içeren içerik oluşturma
            val inputContent = content {
                image(bitmap) // Görüntü içeriği
                text(prompt) // Metin içeriği
            }

            // AI modelini çağırarak yanıtı almak için IO bağlamında coroutine çalıştırma
            val response = withContext(Dispatchers.IO) {
                generativeModel.generateContent(inputContent)
            }

            // Yanıtı Chat nesnesi olarak döndürme
            return Chat(
                prompt = response.text ?: "Error", // Yanıt metni veya hata mesajı
                bitmap = null,
                isFromUser = false,
                sentTime = Date() // Mevcut tarih ve saat
            )

        } catch (e: Exception) {
            // Hata durumunda, hata mesajını içeren Chat nesnesi döndürme
            return Chat(
                prompt = e.message ?: "Error",
                bitmap = null,
                isFromUser = false,
                sentTime = Date()
            )
        }
    }
}
