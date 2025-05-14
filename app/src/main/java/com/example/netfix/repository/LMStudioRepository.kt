package com.example.netfix.repository

import com.example.netfix.api.ChatMessage
import com.example.netfix.api.ChatRequest
import com.example.netfix.api.ChatResponse
import com.example.netfix.api.LMStudioApi
import retrofit2.Response

class LMStudioRepository {
    private val api = LMStudioApi.instance
    
    suspend fun sendMessage(message: String): Response<ChatResponse> {
        val request = ChatRequest(
            messages = listOf(
                ChatMessage(
                    role = "user",
                    content = message
                )
            )
        )
        return api.chatCompletion(request)
    }
    
    fun setBaseUrl(url: String) {
        LMStudioApi.setBaseUrl(url)
    }
}
