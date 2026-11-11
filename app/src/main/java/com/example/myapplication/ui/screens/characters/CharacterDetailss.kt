
package com.example.myapplication.ui.screens.characters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsScreen(
    onBackClick: () -> Unit,
    viewModel: CharacterDetailsViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showEdit by remember { mutableStateOf(false) }
    var showDelete by remember { mutableStateOf(false) }
    val current = state.data
    if (showEdit && current != null) {
        CharacterFormDialog(current, onDismiss = { showEdit = false }, onSave = {
            viewModel.updateCharacter(it)
            showEdit = false
        })
    }
    if (showDelete) {
        AlertDialog(onDismissRequest = { showDelete = false }, title = { Text("Eliminar personaje") },
            text = { Text("¿Eliminar este personaje de la base de datos local?") },
            confirmButton = { TextButton(onClick = { showDelete = false; viewModel.deleteCharacter(onBackClick) }) { Text("Eliminar") } },
            dismissButton = { TextButton(onClick = { showDelete = false }) { Text("Cancelar") } })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Characters details")
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

        if (state.isLoading) {

            // LOADING
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .clickable {
                        viewModel.setError()
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator()
                    Text("Cargando")
                }
            }

        } else if (state.hasError) {

            // ERROR
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = "Error",
                        modifier = Modifier.size(40.dp)
                    )

                    Text("Error al obtener personaje.")
                    Text("Intenta de nuevo")

                    Button(
                        onClick = {
                            viewModel.retry()
                        }
                    ) {
                        Text("Reintentar")
                    }
                }
            }

        } else {

            // DATA
            val character = state.data

            if (character != null) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    AsyncImage(
                        model = character.image,
                        contentDescription = character.name,
                        modifier = Modifier
                            .size(180.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = character.name,
                        fontWeight = FontWeight.Bold
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(onClick = { showEdit = true }) { Text("Editar") }
                        Button(onClick = { showDelete = true }) { Text("Eliminar") }
                    }

                    // BOTON DE FAVORITOS
                    IconButton(
                        onClick = {
                            viewModel.toggleFavorite()
                        }
                    ) {
                        Icon(
                            imageVector = if (character.isFavorite) {
                                Icons.Default.Favorite
                            } else {
                                Icons.Default.FavoriteBorder
                            },
                            contentDescription = if (character.isFavorite) {
                                "Quitar de favoritos"
                            } else {
                                "Agregar a favoritos"
                            },
                            tint = if (character.isFavorite) {
                                Color.Red
                            } else {
                                Color.Gray
                            },
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        CharacterDetailRow(
                            label = "Species:",
                            value = character.species
                        )

                        CharacterDetailRow(
                            label = "Status:",
                            value = character.status
                        )

                        CharacterDetailRow(
                            label = "Gender:",
                            value = character.gender
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterDetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Text(text = value)
    }
}
