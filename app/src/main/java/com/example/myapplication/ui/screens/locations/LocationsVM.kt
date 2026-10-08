
package com.example.myapplication.ui.screens.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Location
import com.example.myapplication.data.LocationDb
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LocationsState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)

class LocationsViewModel : ViewModel() {

    private val _state = MutableStateFlow(LocationsState())

    val state: StateFlow<LocationsState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadLocations()
    }

    private fun loadLocations() {
        loadJob = viewModelScope.launch {
            delay(4000)

            val locations = LocationDb().getAllLocations()

            _state.value = LocationsState(
                isLoading = false,
                data = locations,
                hasError = false
            )
        }
    }

    fun setError() {
        if (!_state.value.isLoading) return

        loadJob?.cancel()

        _state.value = LocationsState(
            isLoading = false,
            data = emptyList(),
            hasError = true
        )
    }

    fun retry() {
        loadJob?.cancel()

        _state.value = LocationsState()

        loadLocations()
    }
}
