
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

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.myapplication.data.Character

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    onCharacterClick: (Int) -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val viewModel: CharactersViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showFavoritesOnly by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Characters")
                }
            )
        },
        bottomBar = bottomBar
    ) { innerPadding ->

        if (state.isLoading) {

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

                    Text("Error al obtener listado de personajes.")

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

            val filteredCharacters = if (showFavoritesOnly) {
                state.data.filter { it.isFavorite }
            } else {
                state.data
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    FilterChip(
                        selected = !showFavoritesOnly,
                        onClick = {
                            showFavoritesOnly = false
                        },
                        label = {
                            Text("Todos")
                        }
                    )

                    FilterChip(
                        selected = showFavoritesOnly,
                        onClick = {
                            showFavoritesOnly = true
                        },
                        label = {
                            Text("Favoritos")
                        }
                    )
                }

                if (filteredCharacters.isEmpty()) {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay elementos a mostrar")
                    }

                } else {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = filteredCharacters,
                            key = { it.id }
                        ) { character ->

                            CharacterItem(
                                character = character,

                                onClick = {
                                    onCharacterClick(character.id)
                                },

                                onFavoriteClick = {
                                    viewModel.toggleFavorite(character)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterItem(
    character: Character,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = character.name
            )

            Text(
                text = character.species
            )

            Text(
                text = character.status
            )
        }

        IconButton(
            onClick = onFavoriteClick
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
                }
            )
        }
    }
}
