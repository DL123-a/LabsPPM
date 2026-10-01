package com.example.myapplication.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNavigationBar(
    selectedItem: String,
    onCharactersClick: () -> Unit,
    onLocationsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar {

        NavigationBarItem(
            selected = selectedItem == "characters",
            onClick = onCharactersClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = "Characters"
                )
            },
            label = {
                Text("Characters")
            }
        )

        NavigationBarItem(
            selected = selectedItem == "locations",
            onClick = onLocationsClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Locations"
                )
            },
            label = {
                Text("Locations")
            }
        )

        NavigationBarItem(
            selected = selectedItem == "profile",
            onClick = onProfileClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile"
                )
            },
            label = {
                Text("Profile")
            }
        )
    }
}