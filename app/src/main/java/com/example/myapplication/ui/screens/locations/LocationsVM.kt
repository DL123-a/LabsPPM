package com.example.myapplication.ui.screens.locations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Location
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

data class LocationsState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)

class LocationsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val locationDao =
        DatabaseProvider.getDatabase(application).locationDao()

    private val _state = MutableStateFlow(LocationsState())

    val state: StateFlow<LocationsState> =
        _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadLocations()
    }

    private fun loadLocations() {
        loadJob = viewModelScope.launch {
            try {
                delay(4000)

                locationDao.getAllLocations().collect { entities ->

                    val locations = entities.map { it.toModel() }

                    _state.value = LocationsState(
                        isLoading = false,
                        data = locations,
                        hasError = false
                    )
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = LocationsState(
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
