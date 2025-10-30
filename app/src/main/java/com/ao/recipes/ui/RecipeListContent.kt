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

package com.ao.recipes.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import com.ao.recipes.R
import com.ao.recipes.data.Recipe
import com.ao.recipes.ui.components.RecipeDetailAppBar
import com.ao.recipes.ui.components.RecipeDockedSearchBar
import com.ao.recipes.ui.components.RecipeListItem
import com.ao.recipes.ui.components.RecipeDetailItem
import com.ao.recipes.ui.utils.RecipeType
import com.ao.recipes.ui.utils.NavigationType
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane

@Composable
fun RecipeListScreen(
    contentType: RecipeType,
    recipeHomeUIState: RecipeHomeUIState,
    navigationType: NavigationType,
    displayFeatures: List<DisplayFeature>,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, RecipeType) -> Unit,
    modifier: Modifier = Modifier
) {
    /**
     * When moving from LIST_AND_DETAIL page to LIST page clear the selection and user should see LIST screen.
     */
    LaunchedEffect(key1 = contentType) {
        if (contentType == RecipeType.SINGLE_PANE && !recipeHomeUIState.isDetailOnlyOpen) {
            closeDetailScreen()
        }
    }

    val recipeLazyListState = rememberLazyListState()

    if (contentType == RecipeType.DUAL_PANE) {
        TwoPane(
            first = {
                RecipeList(
                    recipes = recipeHomeUIState.recipes,
                    openedRecipe = recipeHomeUIState.openedRecipe,
                    recipeLazyListState = recipeLazyListState,
                    navigateToDetail = navigateToDetail
                )
            },
            second = {
                val recipeToShow = recipeHomeUIState.openedRecipe ?: recipeHomeUIState.recipes.firstOrNull()
                if (recipeToShow != null) {
                    RecipeDetail(
                        recipe = recipeToShow,
                        isFullScreen = false
                    )
                } else {
                    // Show a placeholder or an empty state indication
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.no_recipe_selected))
                    }
                }
            },
            strategy = HorizontalTwoPaneStrategy(splitFraction = 0.5f, gapWidth = 16.dp),
            displayFeatures = displayFeatures
        )
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            RecipeSinglePaneContent(
                recipeHomeUIState = recipeHomeUIState,
                recipeLazyListState = recipeLazyListState,
                modifier = Modifier.fillMaxSize(),
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail
            )
            // When we have bottom navigation we show FAB at the bottom end.
            if (navigationType == NavigationType.BOTTOM_NAVIGATION) {
                ExtendedFloatingActionButton(
                    text = { Text(text = stringResource(id = R.string.edit)) },
                    icon = { Icon(Icons.Default.Edit, stringResource(id = R.string.compose)) },
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    expanded = recipeLazyListState.lastScrolledBackward ||
                        !recipeLazyListState.canScrollBackward
                )
            }
        }
    }
}

@Composable
fun RecipeSinglePaneContent(
    recipeHomeUIState: RecipeHomeUIState,
    recipeLazyListState: LazyListState,
    modifier: Modifier = Modifier,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, RecipeType) -> Unit
) {
    if (recipeHomeUIState.openedRecipe != null && recipeHomeUIState.isDetailOnlyOpen) {
        BackHandler {
            closeDetailScreen()
        }
        RecipeDetail(recipe = recipeHomeUIState.openedRecipe) {
            closeDetailScreen()
        }
    } else {
        RecipeList(
            recipes = recipeHomeUIState.recipes,
            openedRecipe = recipeHomeUIState.openedRecipe,
            recipeLazyListState = recipeLazyListState,
            modifier = modifier,
            navigateToDetail = navigateToDetail
        )
    }
}

@Composable
fun RecipeList(
    recipes: List<Recipe>,
    openedRecipe: Recipe?,
    recipeLazyListState: LazyListState,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long, RecipeType) -> Unit
) {
    Box(modifier = modifier.windowInsetsPadding(WindowInsets.statusBars)) {
        RecipeDockedSearchBar(
            recipes = recipes,
            onSearchItemSelected = { searchedRecipes ->
                navigateToDetail(searchedRecipes.id, RecipeType.SINGLE_PANE)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        )

        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 80.dp),
            state = recipeLazyListState
        ) {
            items(items = recipes, key = { it.id }) { recipe ->
                RecipeListItem(
                    recipe = recipe,
                    navigateToDetail = { recipeId ->
                        navigateToDetail(recipeId, RecipeType.SINGLE_PANE)
                    },
                    isOpened = openedRecipe?.id == recipe.id,
                )
            }
            // Add extra spacing at the bottom if
            item {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
            }
        }
    }
}

@Composable
fun RecipeDetail(
    recipe: Recipe,
    modifier: Modifier = Modifier,
    isFullScreen: Boolean = true,
    onBackPressed: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .background(MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        item {
            RecipeDetailAppBar(recipe, isFullScreen) {
                onBackPressed()
            }
        }
        item {
            RecipeDetailItem(recipe = recipe)
        }
        item {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
        }
    }
}

