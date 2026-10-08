
package com.example.myapplication.ui.screens.locations

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.myapplication.data.Location
import com.example.myapplication.data.LocationDb
import com.example.myapplication.navigation.LocationDetails
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LocationDetailsState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)

class LocationDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val locationId: Int =
        savedStateHandle.toRoute<LocationDetails>().id

    private val _state = MutableStateFlow(LocationDetailsState())

    val state: StateFlow<LocationDetailsState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadLocation()
    }

    private fun loadLocation() {
        loadJob = viewModelScope.launch {
            delay(2000)

            val location = LocationDb().getLocationById(locationId)

            _state.value = LocationDetailsState(
                isLoading = false,
                data = location,
                hasError = location == null
            )
        }
    }

    fun setError() {
        if (!_state.value.isLoading) return

        loadJob?.cancel()

        _state.value = LocationDetailsState(
            isLoading = false,
            data = null,
            hasError = true
        )
    }

    fun retry() {
        loadJob?.cancel()

        _state.value = LocationDetailsState()

        loadLocation()
    }
}
