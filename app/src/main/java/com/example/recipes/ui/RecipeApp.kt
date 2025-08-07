/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.recipes.ui

import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import com.example.recipes.ui.navigation.RecipeNavigationActions
import com.example.recipes.ui.navigation.RecipeNavigationWrapper
import com.example.recipes.ui.navigation.Route
import com.example.recipes.ui.utils.DevicePosture
import com.example.recipes.ui.utils.RecipeType
import com.example.recipes.ui.utils.NavigationType
import com.example.recipes.ui.utils.isBookPosture
import com.example.recipes.ui.utils.isSeparating

private fun NavigationSuiteType.toRecipeNavType() = when (this) {
    NavigationSuiteType.NavigationBar -> NavigationType.BOTTOM_NAVIGATION
    NavigationSuiteType.NavigationRail -> NavigationType.NAVIGATION_RAIL
    NavigationSuiteType.NavigationDrawer -> NavigationType.PERMANENT_NAVIGATION_DRAWER
    else -> NavigationType.BOTTOM_NAVIGATION
}

@Composable
fun RecipeApp(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
    recipeHomeUIState: RecipeHomeUIState,
    closeDetailScreen: () -> Unit = {},
    navigateToDetail: (Long, RecipeType) -> Unit = { _, _ -> },
) {
    /**
     * We are using display's folding features to map the device postures a fold is in.
     * In the state of folding device If it's half fold in BookPosture we want to avoid content
     * at the crease/hinge
     */
    val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()

    val foldingDevicePosture = when {
        isBookPosture(foldingFeature) ->
            DevicePosture.BookPosture(foldingFeature.bounds)

        isSeparating(foldingFeature) ->
            DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)

        else -> DevicePosture.NormalPosture
    }

    val contentType = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> RecipeType.SINGLE_PANE
        WindowWidthSizeClass.Medium -> if (foldingDevicePosture != DevicePosture.NormalPosture) {
            RecipeType.DUAL_PANE
        } else {
            RecipeType.SINGLE_PANE
        }
        WindowWidthSizeClass.Expanded -> RecipeType.DUAL_PANE
        else -> RecipeType.SINGLE_PANE
    }

    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        RecipeNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Surface {
        RecipeNavigationWrapper(
            currentDestination = currentDestination,
            navigateToTopLevelDestination = navigationActions::navigateTo
        ) {
            RecipeNavHost(
                navController = navController,
                contentType = contentType,
                displayFeatures = displayFeatures,
                recipeHomeUIState = recipeHomeUIState,
                navigationType = navSuiteType.toRecipeNavType(),
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail,
            )
        }
    }
}

@Composable
private fun RecipeNavHost(
    navController: NavHostController,
    contentType: RecipeType,
    displayFeatures: List<DisplayFeature>,
    recipeHomeUIState: RecipeHomeUIState,
    navigationType: NavigationType,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, RecipeType) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.Home,
    ) {
        composable<Route.Home> {
            RecipeListScreen(
                contentType = contentType,
                recipeHomeUIState = recipeHomeUIState,
                navigationType = navigationType,
                displayFeatures = displayFeatures,
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail,
            )
        }
        composable<Route.NewRecipe> {
            EmptyComingSoon()
        }
        composable<Route.Favorites> {
            EmptyComingSoon()
        }
        composable<Route.Profile> {
            EmptyComingSoon()
        }
    }
}
