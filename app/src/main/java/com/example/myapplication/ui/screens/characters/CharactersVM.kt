package com.example.myapplication.ui.screens.characters

import com.example.myapplication.data.Character
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.CharacterDb
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
data class CharactersState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false
)

class CharactersViewModel : ViewModel() {

    private val _state = MutableStateFlow(CharactersState())

    private var loadJob: Job? = null
    val state: StateFlow<CharactersState> = _state.asStateFlow()

    init {
        loadCharacters()
    }

    private fun loadCharacters() {
        loadJob = viewModelScope.launch {
            delay(4000)

            if (_state.value.hasError) {
                return@launch
            }

            val characters = CharacterDb().getAllCharacters()

            _state.value = CharactersState(
                isLoading = false,
                data = characters,
                hasError = false
            )
        }
    }

    fun setError() {
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
}