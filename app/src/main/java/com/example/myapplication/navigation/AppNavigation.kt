package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.room.withTransaction
import com.example.myapplication.data.UserPreferences
import com.example.myapplication.data.initialSync
import com.example.myapplication.data.room.DatabaseProvider
import com.example.myapplication.ui.components.BottomNavigationBar
import com.example.myapplication.ui.screens.characters.CharacterDetailsScreen
import com.example.myapplication.ui.screens.characters.CharactersScreen
import com.example.myapplication.ui.screens.locations.LocationDetailsScreen
import com.example.myapplication.ui.screens.locations.LocationsScreen
import com.example.myapplication.ui.screens.login.LoginScreen
import com.example.myapplication.ui.screens.profile.ProfileScreen
import com.example.myapplication.ui.screens.session.SessionCheckScreen
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val context = LocalContext.current.applicationContext

    val userPreferences = remember(context) {
        UserPreferences(context)
    }

    val database = remember(context) {
        DatabaseProvider.getDatabase(context)
    }

    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = SessionCheck
    ) {

        // SESSION CHECK
        composable<SessionCheck> {

            LaunchedEffect(Unit) {
                val savedName = userPreferences.userName.first()

                if (savedName.isNullOrBlank()) {

                    navController.navigate(Login) {
                        popUpTo<SessionCheck> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }

                } else {

                    navController.navigate(CharactersGraph) {
                        popUpTo<SessionCheck> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }

            SessionCheckScreen()
        }

        // LOGIN
        composable<Login> {

            var isLoading by remember {
                mutableStateOf(false)
            }

            LoginScreen(
                isLoading = isLoading,

                onStartClick = { name ->

                    if (!isLoading) {

                        isLoading = true

                        coroutineScope.launch {

                            try {
                                delay(4000)

                                initialSync(database)

                                userPreferences.saveName(name)

                                navController.navigate(CharactersGraph) {
                                    popUpTo<Login> {
                                        inclusive = true
                                    }
                                }

                            } catch (e: CancellationException) {
                                throw e
                            } catch (e: Exception) {
                                isLoading = false
                            }
                        }
                    }
                }
            )
        }

        // CHARACTERS GRAPH
        navigation<CharactersGraph>(
            startDestination = Characters
        ) {

            // CHARACTERS
            composable<Characters> {

                CharactersScreen(
                    onCharacterClick = { characterId ->

                        navController.navigate(
                            CharacterDetails(
                                id = characterId
                            )
                        )
                    },

                    bottomBar = {

                        BottomNavigationBar(
                            selectedItem = "characters",

                            onCharactersClick = {
                                // Ya estamos en Characters
                            },

                            onLocationsClick = {

                                navController.navigate(
                                    LocationsGraph
                                ) {
                                    popUpTo(CharactersGraph) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },

                            onProfileClick = {

                                navController.navigate(
                                    Profile
                                ) {
                                    popUpTo(CharactersGraph) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                )
            }

            // CHARACTER DETAILS
            composable<CharacterDetails> {

                CharacterDetailsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }

        // LOCATIONS GRAPH
        navigation<LocationsGraph>(
            startDestination = Locations
        ) {

            // LOCATIONS
            composable<Locations> {

                LocationsScreen(
                    onLocationClick = { locationId ->

                        navController.navigate(
                            LocationDetails(
                                id = locationId
                            )
                        )
                    },

                    bottomBar = {

                        BottomNavigationBar(
                            selectedItem = "locations",

                            onCharactersClick = {

                                navController.navigate(
                                    CharactersGraph
                                ) {
                                    popUpTo(CharactersGraph) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },

                            onLocationsClick = {
                                // Ya estamos en Locations
                            },

                            onProfileClick = {

                                navController.navigate(
                                    Profile
                                ) {
                                    popUpTo(CharactersGraph) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                )
            }

            // LOCATION DETAILS
            composable<LocationDetails> {

                LocationDetailsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }

        // PROFILE
        composable<Profile> {

            val userName by userPreferences.userName.collectAsState(
                initial = null
            )

            ProfileScreen(
                userName = userName.orEmpty(),

                onLogoutClick = {

                    coroutineScope.launch {

                        try {
                            // ELIMINAR DATOS DE ROOM
                            database.withTransaction {

                                database.characterDao()
                                    .deleteAllCharacters()

                                database.locationDao()
                                    .deleteAllLocations()
                            }

                            // ELIMINAR NOMBRE DE DATASTORE
                            userPreferences.clearName()

                            // REGRESAR AL LOGIN
                            navController.navigate(Login) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }

                        } catch (e: CancellationException) {
                            throw e
                        } catch (e: Exception) {
                            // Si falla la limpieza, no navegamos
                            // para evitar un cierre de sesión incompleto.
                        }
                    }
                },

                bottomBar = {

                    BottomNavigationBar(
                        selectedItem = "profile",

                        onCharactersClick = {

                            navController.navigate(
                                CharactersGraph
                            ) {
                                popUpTo(CharactersGraph) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },

                        onLocationsClick = {

                            navController.navigate(
                                LocationsGraph
                            ) {
                                popUpTo(CharactersGraph) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },

                        onProfileClick = {
                            // Ya estamos en Profile
                        }
                    )
                }
            )
        }
    }
}
