package com.example.myapplication.navigation
import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Characters

@Serializable
data class CharacterDetails(
    val id: Int
)
