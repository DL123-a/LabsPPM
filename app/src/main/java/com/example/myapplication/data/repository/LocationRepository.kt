package com.example.myapplication.data.repository

import com.example.myapplication.data.Location
import com.example.myapplication.data.mappers.toModel
import com.example.myapplication.data.remote.RickMortyApi
import com.example.myapplication.data.room.LocationDao
import com.example.myapplication.data.room.LocationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface LocationRepository {
    fun getAll(): Flow<List<Location>>
    fun getById(id: Int): Flow<Location?>
    suspend fun sync()
    suspend fun setFavorite(id: Int, favorite: Boolean)
}

class LocationRepositoryImpl(
    private val api: RickMortyApi,
    private val dao: LocationDao
) : LocationRepository {
    override fun getAll(): Flow<List<Location>> = dao.getAllLocations().map { list -> list.map { it.toModel() } }
    override fun getById(id: Int): Flow<Location?> = dao.getLocationById(id).map { it?.toModel() }
    override suspend fun sync() {
        dao.insertLocations(api.getLocations().map {
            LocationEntity(id = it.id, name = it.name, type = it.type, dimension = it.dimension)
        })
    }
    override suspend fun setFavorite(id: Int, favorite: Boolean) { dao.updateFavorite(id, favorite) }
}
