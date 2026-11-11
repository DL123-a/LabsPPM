package com.example.myapplication.data.repository

import com.example.myapplication.data.Character
import com.example.myapplication.data.mappers.toEntity
import com.example.myapplication.data.mappers.toModel
import com.example.myapplication.data.remote.RickMortyApi
import com.example.myapplication.data.room.CharacterDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface CharacterRepository {
    fun getAll(): Flow<List<Character>>
    fun getById(id: Int): Flow<Character?>
    suspend fun sync()
    suspend fun create(character: Character)
    suspend fun update(character: Character)
    suspend fun delete(id: Int)
    suspend fun setFavorite(id: Int, favorite: Boolean)
}

class CharacterRepositoryImpl(
    private val api: RickMortyApi,
    private val dao: CharacterDao
) : CharacterRepository {
    override fun getAll(): Flow<List<Character>> = dao.getAllCharacters().map { list -> list.map { it.toModel() } }
    override fun getById(id: Int): Flow<Character?> = dao.getCharacterById(id).map { it?.toModel() }
    override suspend fun sync() {
        val data = api.getCharacters().map {
            com.example.myapplication.data.room.CharacterEntity(
                id = it.id, name = it.name, status = it.status,
                species = it.species, gender = it.gender, image = it.image
            )
        }
        dao.insertCharacters(data)
    }
    override suspend fun create(character: Character) { dao.insertCharacter(character.toEntity()) }
    override suspend fun update(character: Character) { dao.updateCharacter(character.toEntity()) }
    override suspend fun delete(id: Int) { dao.deleteCharacter(id) }
    override suspend fun setFavorite(id: Int, favorite: Boolean) { dao.updateFavorite(id, favorite) }
}
