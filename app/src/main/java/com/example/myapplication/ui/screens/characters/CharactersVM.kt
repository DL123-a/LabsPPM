package com.example.myapplication.ui.screens.characters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Character
import com.example.myapplication.data.mappers.toModel
import com.example.myapplication.data.room.DatabaseProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class CharactersState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false
)

class CharactersViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val characterDao =
        DatabaseProvider.getDatabase(application).characterDao()

    private val _state = MutableStateFlow(CharactersState())

    val state: StateFlow<CharactersState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadCharacters()
    }

    private fun loadCharacters() {
        loadJob = viewModelScope.launch {
            try {
                delay(4000)

                characterDao.getAllCharacters().collect { entities ->

                    val characters = entities.map { it.toModel() }

                    _state.value = CharactersState(
                        isLoading = false,
                        data = characters,
                        hasError = false
                    )
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = CharactersState(
                    isLoading = false,
                    data = emptyList(),
                    hasError = true
                )
            }
        }
    }

    fun setError() {
        if (!_state.value.isLoading) return

        loadJob?.cancel()

        _state.value = CharactersState(
            isLoading = false,
            data = emptyList(),
            hasError = true
        )
    }

    fun retry() {
        loadJob?.cancel()

        _state.value = CharactersState()

        loadCharacters()
    }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch {
            try {
                characterDao.updateFavorite(
                    id = character.id,
                    isFavorite = !character.isFavorite
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Manejo de errores pendiente
            }
        }
    }
}
