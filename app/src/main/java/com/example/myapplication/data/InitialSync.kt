package com.example.myapplication.data

import androidx.room.withTransaction
import com.example.myapplication.data.mappers.toEntity
import com.example.myapplication.data.room.AppDatabase

suspend fun initialSync(database: AppDatabase) {

    val characters = CharacterDb()
        .getAllCharacters()
        .map { it.toEntity() }

    val locations = LocationDb()
        .getAllLocations()
        .map { it.toEntity() }

    database.withTransaction {

        database.characterDao()
            .insertCharacters(characters)

        database.locationDao()
            .insertLocations(locations)
    }
}
