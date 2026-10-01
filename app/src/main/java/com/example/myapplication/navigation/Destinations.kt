package com.example.myapplication.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

// Graph de Characters
@Serializable
object CharactersGraph

@Serializable
object Characters

@Serializable
data class CharacterDetails(
    val id: Int
)

// Graph de Locations
@Serializable
object LocationsGraph

@Serializable
object Locations

@Serializable
data class LocationDetails(
    val id: Int
)

@Serializable
object Profile