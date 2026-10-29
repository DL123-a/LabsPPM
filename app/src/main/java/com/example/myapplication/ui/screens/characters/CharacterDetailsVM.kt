
package com.example.myapplication.ui.screens.characters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.myapplication.data.Character
import com.example.myapplication.data.mappers.toModel
import com.example.myapplication.data.room.DatabaseProvider
import com.example.myapplication.navigation.CharacterDetails
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class CharacterDetailsState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)

class CharacterDetailsViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val characterId: Int =
        savedStateHandle.toRoute<CharacterDetails>().id

    private val characterDao =
        DatabaseProvider.getDatabase(application).characterDao()

    private val _state =
        MutableStateFlow(CharacterDetailsState())

    val state: StateFlow<CharacterDetailsState> =
        _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadCharacter()
    }

    private fun loadCharacter() {
        loadJob = viewModelScope.launch {
            try {
                delay(2000)

                characterDao.getCharacterById(characterId)
                    .collect { entity ->

                        val character = entity?.toModel()

                        _state.value = CharacterDetailsState(
                            isLoading = false,
                            data = character,
                            hasError = character == null
                        )
                    }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = CharacterDetailsState(
                    isLoading = false,
                    data = null,
                    hasError = true
                )
            }
        }
    }

    fun setError() {
        if (!_state.value.isLoading) return

        loadJob?.cancel()

        _state.value = CharacterDetailsState(
            isLoading = false,
            data = null,
            hasError = true
        )
    }

    fun retry() {
        loadJob?.cancel()

        _state.value = CharacterDetailsState()

        loadCharacter()
    }




    fun toggleFavorite() {
        val character = _state.value.data ?: return

        viewModelScope.launch {
            try {
                characterDao.updateFavorite(
                    id = character.id,
                    isFavorite = !character.isFavorite
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {

            }
        }
    }
}


