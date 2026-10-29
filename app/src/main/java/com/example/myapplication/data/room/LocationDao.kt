package com.example.myapplication.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocations(
        locations: List<LocationEntity>
    )

    @Query("SELECT * FROM locations")
    fun getAllLocations(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE id = :id")
    fun getLocationById(id: Int): Flow<LocationEntity?>

    @Query("DELETE FROM locations")
    suspend fun deleteAllLocations()
}
