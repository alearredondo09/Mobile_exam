package com.app.examenmoviles.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.examenmoviles.presentation.screens.home.HomeScreen
import com.app.examenmoviles.presentation.screens.home.textMessage

sealed class Screen(
    val route: String,
) {
    object Home : Screen("home")

    object TextScreen : Screen("textScreen")
}

@Suppress("ktlint:standard:function-naming")
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
            println("Entro al Nav composable")
            HomeScreen(
                onButtonClick = {
                    navController.navigate(Screen.TextScreen.route)
                },
            )
        }

        composable(route = Screen.TextScreen.route) {
            println("Entro al Text Screen")
            textMessage(
                onclickBack = {
                    navController.popBackStack() // regresa a la vista anterior
                },
            )
        }

        /** composable(
         route = Screen.Detail.route,
         arguments = listOf(navArgument("pokemonId") { type = NavType.StringType }),
         ) { backStackEntry ->
         val pokemonId = backStackEntry.arguments?.getString("pokemonId") ?: "1"
         PokemonDetailScreen(
         pokemonId = pokemonId,
         onBackClick = { navController.popBackStack() },
         )
         }**/
    }
}
