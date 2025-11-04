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
import com.ao.recipes.data.RecipeType
import com.ao.recipes.ui.utils.RecipeType as RecipeUIType
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
                        openedRecipe = recipes.firstOrNull()
                    )
                }
        }
    }

    fun addRecipe(context: Context, recipe: Recipe) {
        viewModelScope.launch {
            recipesRepository.addRecipe(context, recipe)
        }
    }

    fun fillWithGemini() {
        val geminiRecipe = Recipe(
            id = 0, // ID can be 0 for a new recipe
            name = "Pizza de Calabresa com Borda de Catupiry",
            description = "Uma pizza clássica brasileira com um toque especial de cremosidade na borda.",
            ingredients = "- 1 disco de massa de pizza\n- 1/2 xícara de molho de tomate\n- 200g de queijo mussarela ralado\n- 150g de linguiça calabresa fatiada\n- 1/2 cebola fatiada\n- Orégano a gosto\n- Azeitonas pretas a gosto\n- Catupiry (requeijão cremoso) para a borda",
            steps = "1. Pré-aqueça o forno a 220°C.\n2. Abra a massa da pizza em uma superfície enfarinhada.\n3. Espalhe o Catupiry por toda a borda da massa e dobre-a para fechar.\n4. Espalhe o molho de tomate sobre a massa.\n5. Cubra com o queijo mussarela, a calabresa, a cebola e as azeitonas.\n6. Polvilhe com orégano.\n7. Leve ao forno por 15-20 minutos ou até a massa estar dourada e o queijo derretido.",
            type = RecipeType.MAIN_COURSE,
            isStarred = false,
            picture = null // No image for now
        )
        _uiState.value = _uiState.value.copy(recipePreFilledAI = geminiRecipe)
    }

    fun setOpenedRecipe(recipeId: Long, contentType: RecipeUIType) {
        /**
         * We only set isDetailOnlyOpen to true when it's only single pane layout
         */
        val recipe = uiState.value.recipes.find { it.id == recipeId }
        _uiState.value = _uiState.value.copy(
            openedRecipe = recipe,
            isDetailOnlyOpen = contentType == RecipeUIType.SINGLE_PANE
        )
    }

    fun setOpenedRecipe(recipe: Recipe) {
        _uiState.value = _uiState.value.copy(
            openedRecipe = recipe,
        )
    }

    fun closeDetailScreen() {
        _uiState.value = _uiState
            .value.copy(
                isDetailOnlyOpen = false,
                openedRecipe = _uiState.value.recipes.firstOrNull()
            )
    }
}

data class RecipeHomeUIState(
    val recipes: List<Recipe> = emptyList(),
    val openedRecipe: Recipe? = null,
    val recipePreFilledAI: Recipe? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)
