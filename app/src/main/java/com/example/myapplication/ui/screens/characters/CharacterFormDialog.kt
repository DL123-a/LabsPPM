
package com.example.myapplication.ui.screens.characters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.Character

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterFormDialog(
    character: Character?,
    onDismiss: () -> Unit,
    onSave: (Character) -> Unit
) {
    var name by remember(character) {
        mutableStateOf(character?.name.orEmpty())
    }

    var species by remember(character) {
        mutableStateOf(character?.species.orEmpty())
    }

    var status by remember(character) {
        mutableStateOf(character?.status ?: "Alive")
    }

    var gender by remember(character) {
        mutableStateOf(character?.gender ?: "Male")
    }

    var image by remember(character) {
        mutableStateOf(character?.image.orEmpty())
    }

    var statusExpanded by remember {
        mutableStateOf(false)
    }

    var genderExpanded by remember {
        mutableStateOf(false)
    }

    val statusOptions = listOf(
        "Alive",
        "Dead",
        "unknown"
    )

    val genderOptions = listOf(
        "Male",
        "Female",
        "unknown"
    )

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text(
                if (character == null) {
                    "Crear personaje"
                } else {
                    "Editar personaje"
                }
            )
        },

        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // NOMBRE
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // ESPECIE
                OutlinedTextField(
                    value = species,
                    onValueChange = { species = it },
                    label = { Text("Especie") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // ESTADO
                ExposedDropdownMenuBox(
                    expanded = statusExpanded,
                    onExpandedChange = {
                        statusExpanded = !statusExpanded
                    }
                ) {
                    OutlinedTextField(
                        value = status,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Estado") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = statusExpanded
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(
                                MenuAnchorType.PrimaryNotEditable
                            )
                    )

                    ExposedDropdownMenu(
                        expanded = statusExpanded,
                        onDismissRequest = {
                            statusExpanded = false
                        }
                    ) {
                        statusOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    status = option
                                    statusExpanded = false
                                }
                            )
                        }
                    }
                }

                // GENERO
                ExposedDropdownMenuBox(
                    expanded = genderExpanded,
                    onExpandedChange = {
                        genderExpanded = !genderExpanded
                    }
                ) {
                    OutlinedTextField(
                        value = gender,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Género") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = genderExpanded
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(
                                MenuAnchorType.PrimaryNotEditable
                            )
                    )

                    ExposedDropdownMenu(
                        expanded = genderExpanded,
                        onDismissRequest = {
                            genderExpanded = false
                        }
                    ) {
                        genderOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    gender = option
                                    genderExpanded = false
                                }
                            )
                        }
                    }
                }

                // URL DE IMAGEN (OPCIONAL)
                OutlinedTextField(
                    value = image,
                    onValueChange = { image = it },
                    label = {
                        Text("URL de imagen (opcional)")
                    },
                    placeholder = {
                        Text("https://ejemplo.com/imagen.jpg")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },

        confirmButton = {
            TextButton(
                enabled = name.isNotBlank() &&
                        species.isNotBlank(),
                onClick = {
                    onSave(
                        Character(
                            id = character?.id ?: 0,
                            name = name.trim(),
                            status = status,
                            species = species.trim(),
                            gender = gender,
                            image = image.trim().ifBlank {
                                character?.image
                                    ?.takeIf { it.isNotBlank() }
                                    ?: "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                            },
                            isFavorite = character?.isFavorite ?: false
                        )
                    )
                }
            ) {
                Text("Guardar")
            }
        },

        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
