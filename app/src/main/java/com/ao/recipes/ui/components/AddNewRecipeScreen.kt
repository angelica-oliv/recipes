package com.ao.recipes.ui.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.ao.recipes.R
import com.ao.recipes.data.Recipe
import com.ao.recipes.data.RecipeType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewRecipeScreen(
    onSaveRecipe: (Recipe) -> Unit,
    onFillWithGemini: (String) -> Unit,
    modifier: Modifier = Modifier,
    recipePreFilledAI: Recipe? = null,
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var steps by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }
    var recipeTypeStr by remember { mutableStateOf(RecipeType.MAIN_COURSE.name) }
    var isStarredState by remember { mutableStateOf(false) }

    LaunchedEffect(recipePreFilledAI) {
        recipePreFilledAI?.let {
            name = it.name
            description = it.description
            ingredients = it.ingredients
            steps = it.steps
            link = it.link ?: ""
            recipeTypeStr = it.type.name
            isStarredState = it.isStarred
        }
    }

    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp), // Consistent with Card usage elsewhere
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp) // Inner padding like RecipeDetailItem
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.add_new_recipe),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.recipe_name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.description)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = ingredients,
                onValueChange = { ingredients = it },
                label = { Text(stringResource(R.string.ingredients)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp, max = 200.dp),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = steps,
                onValueChange = { steps = it },
                label = { Text(stringResource(R.string.steps)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp, max = 300.dp),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = link,
                onValueChange = { link = it },
                label = { Text(stringResource(R.string.link)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            // TODO: Consider using a DropdownMenu for RecipeType selection
            OutlinedTextField(
                value = recipeTypeStr,
                onValueChange = { recipeTypeStr = it },
                label = { Text(stringResource(R.string.recipe_type)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.favorite), style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = isStarredState,
                    onCheckedChange = { isStarredState = it }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                Button(
                    onClick = {
                        val recipeType = RecipeType.entries.firstOrNull { it.name.equals(recipeTypeStr, ignoreCase = true) }
                            ?: RecipeType.MAIN_COURSE

                        try {
                            val newRecipe = Recipe(
                                id = recipePreFilledAI?.id ?: System.currentTimeMillis(), // Use existing ID or generate new
                                name = name,
                                description = description,
                                ingredients = ingredients,
                                steps = steps,
                                link = link,
                                picture = recipePreFilledAI?.picture, // Preserve the picture
                                isStarred = isStarredState,
                                type = recipeType
                            )
                            onSaveRecipe(newRecipe)
                        } catch (e: Exception) {
                            // Log error o
                            Log.e("AddRecipe","Error creating Recipe: $name", e)
                        }
                    },
                    enabled = name.isNotBlank() && ingredients.isNotBlank() && steps.isNotBlank()
                ) {
                    Text(stringResource(R.string.save_recipe))
                }

                Button(
                    onClick = { onFillWithGemini(name) },
                    enabled = name.isNotBlank()
                ) {
                    Text(stringResource(R.string.fill_with_gemini))
                }
            }
        }
    }
}

@Preview
@Composable
fun AddNewRecipeScreenPreview() {
    AddNewRecipeScreen(
        onSaveRecipe = {},
        onFillWithGemini = {})
}

@Preview
@Composable
fun AddNewRecipeScreenPrefilledPreview() {
    val prefilledRecipe = Recipe(
        id = 1L,
        name = "AI Pizza",
        description = "A delicious pizza generated by AI.",
        ingredients = "Dough, Tomato Sauce, Cheese, AI Magic",
        steps = "1. Make dough. 2. Add toppings. 3. Bake. 4. Enjoy!",
        link = "www.aipizza.com",
        type = RecipeType.MAIN_COURSE,
        isStarred = true,
        picture = null
    )
    AddNewRecipeScreen(
        onSaveRecipe = {},
        onFillWithGemini = {},
        recipePreFilledAI = prefilledRecipe
    )
}
