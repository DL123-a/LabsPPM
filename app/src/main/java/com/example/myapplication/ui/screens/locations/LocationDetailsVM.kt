package com.example.myapplication.ui.screens.locations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.myapplication.data.Location
import com.example.myapplication.data.mappers.toModel
import com.example.myapplication.data.room.DatabaseProvider
import com.example.myapplication.navigation.LocationDetails
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class LocationDetailsState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)

class LocationDetailsViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val locationId: Int =
        savedStateHandle.toRoute<LocationDetails>().id

    private val locationDao =
        DatabaseProvider.getDatabase(application).locationDao()

    private val _state =
        MutableStateFlow(LocationDetailsState())

    val state: StateFlow<LocationDetailsState> =
        _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadLocation()
    }

    private fun loadLocation() {
        loadJob = viewModelScope.launch {
            try {
                delay(2000)

                locationDao.getLocationById(locationId)
                    .collect { entity ->

                        val location = entity?.toModel()

                        _state.value = LocationDetailsState(
                            isLoading = false,
                            data = location,
                            hasError = location == null
                        )
                    }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = LocationDetailsState(
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
