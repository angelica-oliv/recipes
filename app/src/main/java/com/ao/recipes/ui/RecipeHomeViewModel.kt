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
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.ao.recipes.ui.utils.RecipeType as RecipeUIType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class RecipeHomeViewModel(private val recipesRepository: RecipesRepository = RecipesRepositoryImpl()) :
    ViewModel() {

    private val generativeBackend by lazy { // Initialize lazily
        Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel("gemini-2.5-flash")
    }

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

    fun fillWithGemini(recipeName: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(loading = true)
                val prompt = "Gere uma receita completa para \"$recipeName\" em português. " +
                        "A resposta deve ser APENAS o JSON bruto, sem nenhuma formatação extra ou markdown. " +
                        "O JSON deve ter o seguinte formato: {\\\"name\\\": \"<nome da receita>\\\", \\\"description\\\": \\\"<descrição da receita>\\\", \\\"ingredients\\\": \\\"<ingredientes separados por quebra de linha>\\\", \\\"steps\\\": \\\"<passos separados por quebra de linha>\\\", \\\"" +
                        "type\\\": \\\"<tipo da receita (apenas um dos seguintes: DESERT, MAIN_COURSE, SALAD, SOUP, APPETIZER)>\\\"}."

                val response = generativeBackend.generateContent(prompt)
                response.text?.let {
                    val recipe = Json.decodeFromString<Recipe>(it)
                    _uiState.value = _uiState.value.copy(recipePreFilledAI = recipe, loading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, loading = false)
            }
        }
    }

    fun errorShown() {
        _uiState.value = _uiState.value.copy(error = null)
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
