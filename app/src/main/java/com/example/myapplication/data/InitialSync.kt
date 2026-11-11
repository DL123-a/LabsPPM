package com.example.myapplication.data

import androidx.room.withTransaction
import com.example.myapplication.data.remote.ApiProvider
import com.example.myapplication.data.room.AppDatabase
import com.example.myapplication.data.room.CharacterEntity
import com.example.myapplication.data.room.LocationEntity

suspend fun initialSync(database: AppDatabase) {
    // Both downloads must finish before committing anything to Room.
    val api = ApiProvider.api
    val characters = api.getCharacters().map {
        CharacterEntity(it.id, it.name, it.status, it.species, it.gender, it.image)
    }
    val locations = api.getLocations().map {
        LocationEntity(it.id, it.name, it.type, it.dimension)
    }
    database.withTransaction {
        database.characterDao().insertCharacters(characters)
        database.locationDao().insertLocations(locations)
    }
}
