package com.example.myapplication.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess

interface RickMortyApi {
    suspend fun getCharacters(): List<CharacterDTO>
    suspend fun getLocations(): List<LocationDTO>
}

class KtorRickMortyApi(private val client: HttpClient) : RickMortyApi {
    override suspend fun getCharacters(): List<CharacterDTO> = loadPages("character")
    override suspend fun getLocations(): List<LocationDTO> = loadPages("location")

    private suspend inline fun <reified T> loadPages(path: String): List<T> {
        val result = mutableListOf<T>()
        var next: String? = path
        while (next != null) {
            val response = client.get(next)
            check(response.status.isSuccess()) { "Error HTTP: ${response.status}" }
            val page: ApiPage<T> = response.body()
            result.addAll(page.results)
            next = page.info.next
        }
        return result
    }
}

object ApiProvider {
    private val client by lazy { HttpClientFactory.create() }
    val api: RickMortyApi by lazy { KtorRickMortyApi(client) }
}
