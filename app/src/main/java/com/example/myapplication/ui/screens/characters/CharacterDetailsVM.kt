package com.example.myapplication.ui.screens.characters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.myapplication.data.Character
import com.example.myapplication.data.CharacterDb
import com.example.myapplication.navigation.CharacterDetails
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CharacterDetailsState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)

class CharacterDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterId: Int =
        savedStateHandle.toRoute<CharacterDetails>().id

    private val _state = MutableStateFlow(CharacterDetailsState())

    val state: StateFlow<CharacterDetailsState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadCharacter()
    }

    private fun loadCharacter() {
        loadJob = viewModelScope.launch {
            delay(2000)

            val character = CharacterDb().getCharacterById(characterId)

            _state.value = CharacterDetailsState(
                isLoading = false,
                data = character,
                hasError = character == null
            )
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
}