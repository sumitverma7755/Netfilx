package com.example.netfix.api

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface LMStudioService {
    @POST("v1/chat/completions")
    suspend fun chatCompletion(@Body request: ChatRequest): Response<ChatResponse>
}

data class ChatRequest(
    val messages: List<ChatMessage>,
    val model: String = "local-model",
    val temperature: Double = 0.7,
    val max_tokens: Int = 800
)

data class ChatMessage(
    val role: String,
    val content: String
)

data class ChatResponse(
    val choices: List<Choice>,
    val created: Long,
    val model: String,
    val usage: Usage
)

data class Choice(
    val finish_reason: String,
    val index: Int,
    val message: ChatMessage
)

data class Usage(
    val completion_tokens: Int,
    val prompt_tokens: Int,
    val total_tokens: Int
)

object LMStudioApi {
    private var baseUrl = "http://localhost:1234/"  // Default LM Studio URL
    
    fun setBaseUrl(url: String) {
        baseUrl = url
    }
    
    val instance: LMStudioService by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LMStudioService::class.java)
    }
}
