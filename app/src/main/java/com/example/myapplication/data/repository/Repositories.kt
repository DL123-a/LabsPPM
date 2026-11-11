package com.example.myapplication.data.repository

import android.content.Context
import com.example.myapplication.data.remote.ApiProvider
import com.example.myapplication.data.room.DatabaseProvider

object Repositories {
    fun characters(context: Context): CharacterRepository = CharacterRepositoryImpl(
        ApiProvider.api, DatabaseProvider.getDatabase(context).characterDao()
    )
    fun locations(context: Context): LocationRepository = LocationRepositoryImpl(
        ApiProvider.api, DatabaseProvider.getDatabase(context).locationDao()
    )
}
