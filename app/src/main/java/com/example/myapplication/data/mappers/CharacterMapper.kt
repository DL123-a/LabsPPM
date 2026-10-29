package com.example.myapplication.data.mappers

import com.example.myapplication.data.Character
import com.example.myapplication.data.room.CharacterEntity

fun CharacterEntity.toModel(): Character {
    return Character(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        image = image,
        isFavorite = isFavorite
    )
}

fun Character.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        image = image,
        isFavorite = isFavorite
    )
}
