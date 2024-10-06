/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.milleron.harmonyfinder.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import fr.milleron.harmonyfinder.ui.harmonyfinderhome.HarmonyFinderHomeScreen
import fr.milleron.harmonyfinder.ui.harmonyfinderwait.HarmonyFinderWaitScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem("Home", "main", Icons.Default.Home),
        BottomNavItem("Wait", "wait", Icons.Default.Favorite)
    )

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController, items = items) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "main",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("main") { HarmonyFinderHomeScreen(modifier = Modifier.padding(16.dp)) }
            composable("wait") { HarmonyFinderWaitScreen(modifier = Modifier.padding(16.dp)) }
            // TODO: Add more destinations
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController, items: List<BottomNavItem>) {
    NavigationBar {
        val currentRoute = currentRoute(navController)
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Avoid multiple copies of the same destination in the back stack
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Restore state when re-selecting a previously selected item
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    return navBackStackEntry?.destination?.route
}
