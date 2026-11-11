package com.example.myapplication.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.Logger
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


object HttpClientFactory {

    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    fun create(): HttpClient {
        return HttpClient(CIO.create()) {

            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.DEFAULT
            }

            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }

            defaultRequest {
                url(BASE_URL)
                contentType(ContentType.Application.Json)
            }
        }
    }
}