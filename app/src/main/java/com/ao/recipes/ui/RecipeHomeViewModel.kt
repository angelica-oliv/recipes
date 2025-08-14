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

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ao.recipes.data.Recipe
import com.ao.recipes.data.RecipesRepository
import com.ao.recipes.data.RecipesRepositoryImpl
import com.ao.recipes.ui.utils.RecipeType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class RecipeHomeViewModel(private val recipesRepository: RecipesRepository = RecipesRepositoryImpl()) :
    ViewModel() {

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(RecipeHomeUIState(loading = true))
    val uiState: StateFlow<RecipeHomeUIState> = _uiState

    fun initRecipes(context: Context) {
        viewModelScope.launch {
            recipesRepository.getAllRecipes(context)
                .catch { ex ->
                    _uiState.value = RecipeHomeUIState(error = ex.message)
                }
                .collect { recipes ->
                    /**
                     * We set first recipe selected by default for first App launch in large-screens
                     */
                    _uiState.value = RecipeHomeUIState(
                        recipes = recipes,
                        openedRecipe = recipes.first()
                    )
                }
        }
    }

    fun setOpenedRecipe(recipeId: Long, contentType: RecipeType) {
        /**
         * We only set isDetailOnlyOpen to true when it's only single pane layout
         */
        val recipe = uiState.value.recipes.find { it.id == recipeId }
        _uiState.value = _uiState.value.copy(
            openedRecipe = recipe,
            isDetailOnlyOpen = contentType == RecipeType.SINGLE_PANE
        )
    }

    fun closeDetailScreen() {
        _uiState.value = _uiState
            .value.copy(
                isDetailOnlyOpen = false,
                openedRecipe = _uiState.value.recipes.first()
            )
    }
}

data class RecipeHomeUIState(
    val recipes: List<Recipe> = emptyList(),
    val openedRecipe: Recipe? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)
