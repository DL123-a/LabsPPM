package com.example.myapplication.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class ApiPage<T>(val info: PageInfo, val results: List<T>)

@Serializable
data class PageInfo(val next: String? = null)

@Serializable
data class CharacterDTO(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String
)

@Serializable
data class LocationDTO(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String
)
