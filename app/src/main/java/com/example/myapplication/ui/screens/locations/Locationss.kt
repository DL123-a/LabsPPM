package com.example.myapplication.ui.screens.locations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.Location
import com.example.myapplication.data.LocationDb

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    onLocationClick: (Int) -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val locations = LocationDb().getAllLocations()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Locations")
                }
            )
        },
        bottomBar = bottomBar
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(locations) { location ->

                LocationItem(
                    location = location,
                    onClick = {
                        onLocationClick(location.id)
                    }
                )

                HorizontalDivider()
            }
        }
    }
}

@Composable
fun LocationItem(
    location: Location,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(16.dp)
    ) {
        Text(
            text = location.name
        )

        Text(
            text = location.type
        )
    }
}