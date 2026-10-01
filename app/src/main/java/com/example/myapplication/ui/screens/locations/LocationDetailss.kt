package com.example.myapplication.ui.screens.locations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.LocationDb

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailsScreen(
    locationId: Int,
    onBackClick: () -> Unit
) {
    val location = LocationDb().getLocationById(locationId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Location Details")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {

            Text(
                text = location.name,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            LocationInformationRow(
                label = "ID:",
                value = location.id.toString()
            )

            LocationInformationRow(
                label = "Type:",
                value = location.type
            )

            LocationInformationRow(
                label = "Dimension:",
                value = location.dimension
            )
        }
    }
}

@Composable
fun LocationInformationRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label
        )

        Text(
            text = value
        )
    }
}