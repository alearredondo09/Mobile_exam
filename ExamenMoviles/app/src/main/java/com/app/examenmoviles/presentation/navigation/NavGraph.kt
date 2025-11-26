package com.app.examenmoviles.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
) {
    object Home : Screen("home")

    object Detail : Screen("") {
        fun createRoute(pokemonId: String) = "pokemon/$pokemonId"
    }
}

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                onPokemonClick = { pokemonId ->
                    navController.navigate(Screen.Detail.createRoute(pokemonId))
                },
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("pokemonId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getString("pokemonId") ?: "1"
            PokemonDetailScreen(
                pokemonId = pokemonId,
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}
