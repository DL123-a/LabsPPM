package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.myapplication.ui.screens.CharacterDetailsScreen
import com.example.myapplication.ui.screens.CharactersScreen
import com.example.myapplication.ui.screens.LoginScreen

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
                    navController.navigate(Characters) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // CHARACTERS
        composable<Characters> {
            CharactersScreen(
                onCharacterClick = { characterId ->
                    navController.navigate(
                        CharacterDetails(id = characterId)
                    )
                }
            )
        }

        // CHARACTER DETAILS
        composable<CharacterDetails> { backStackEntry ->

            val characterDetails =
                backStackEntry.toRoute<CharacterDetails>()

            CharacterDetailsScreen(
                characterId = characterDetails.id,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}