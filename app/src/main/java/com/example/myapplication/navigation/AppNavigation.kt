package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.myapplication.ui.components.BottomNavigationBar
import com.example.myapplication.ui.screens.characters.CharacterDetailsScreen
import com.example.myapplication.ui.screens.characters.CharactersScreen
import com.example.myapplication.ui.screens.locations.LocationDetailsScreen
import com.example.myapplication.ui.screens.locations.LocationsScreen
import com.example.myapplication.ui.screens.login.LoginScreen
import com.example.myapplication.ui.screens.profile.ProfileScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Login
    ) {

        // LOGIN
        composable<Login> {

            LoginScreen(
                onStartClick = {

                    navController.navigate(CharactersGraph) {

                        popUpTo(
                            navController.graph.findStartDestination().id
                        ) {
                            inclusive = true
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

            ProfileScreen(

                onLogoutClick = {

                    navController.navigate(Login) {

                        popUpTo(
                            navController.graph.id
                        ) {
                            inclusive = true
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

                        }
                    )
                }
            )
        }
    }
}