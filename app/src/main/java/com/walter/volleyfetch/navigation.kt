package com.walter.volleyfetch

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


enum class Destination(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val contentDestination: String
) {
    ALL("all", "All Expenses", Icons.Default.Home, "All Expenses Screen"),
    INCOME("income", "Income", Icons.Default.Home, "Income Screen"),
    GRAPHS("graphs", "Graphics", Icons.Default.Home, "Graphics Screen")
}

@Composable
fun AppNavHost(
    navHostController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier
) {
    NavHost(navHostController, startDestination.route) {
        Destination.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    Destination.ALL -> NewsScreen()
                    Destination.INCOME -> IncomeScreen()
                    Destination.GRAPHS -> GraphsScreen()
                }
            }
        }
    }
}


@Composable
fun NavigationBarMotherScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = Destination.ALL
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                Destination.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(route = destination.route)
                            selectedDestination = index
                        },
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = destination.contentDestination
                            )
                        },
                        label = { Text(destination.title) }
                    )
                }
            }
        }
    ) { contentPadding ->
        AppNavHost(navController, startDestination, modifier = Modifier.padding(contentPadding))
    }
}