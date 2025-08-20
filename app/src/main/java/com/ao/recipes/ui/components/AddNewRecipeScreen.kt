package com.ao.recipes.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.ao.recipes.data.Recipe
import com.ao.recipes.data.RecipeType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewRecipeScreen(
    onSaveRecipe: (Recipe) -> Unit,
    onFillWithGemini: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var steps by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }
    var recipeTypeStr by remember { mutableStateOf(RecipeType.MAIN_COURSE.name) }
    var isStarredState by remember { mutableStateOf(false) }

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
                "Add New Recipe", // This can be localized if needed
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Recipe Name*") }, // Localize
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") }, // Localize
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = ingredients,
                onValueChange = { ingredients = it },
                label = { Text("Ingredients (one per line)") }, // Localize
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp, max = 200.dp),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = steps,
                onValueChange = { steps = it },
                label = { Text("Steps (one per line)") }, // Localize
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp, max = 300.dp),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = link,
                onValueChange = { link = it },
                label = { Text("Link (Optional)") }, // Localize
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            // TODO: Consider using a DropdownMenu for RecipeType selection
            OutlinedTextField(
                value = recipeTypeStr,
                onValueChange = { recipeTypeStr = it },
                label = { Text("Recipe Type (e.g., MAIN_COURSE, DESERT)") }, // Localize
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Favorite:", style = MaterialTheme.typography.bodyLarge) // Localize
                Switch(
                    checked = isStarredState,
                    onCheckedChange = { isStarredState = it }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Note: Image for the recipe (Bitmap) needs to be handled. The 'Save' button currently cannot provide this.", // Localize
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                Button(
                    onClick = {
                        // CRITICAL: The Recipe data class requires a non-nullable 'picture: Bitmap'.
                        // This form does not currently collect or create a Bitmap.
                        // To properly save, you need to:
                        // 1. Modify Recipe to have 'picture: Bitmap?' (nullable).
                        // OR
                        // 2. Implement an image picker here and create/load a Bitmap.
                        // OR
                        // 3. Provide a default/placeholder Bitmap.

                        // val placeholderBitmap = android.graphics.Bitmap.createBitmap(1, 1, android.graphics.Bitmap.Config.ARGB_8888) // Example placeholder

                        val recipeType = RecipeType.entries.firstOrNull { it.name.equals(recipeTypeStr, ignoreCase = true) }
                            ?: RecipeType.MAIN_COURSE
                        
                        // Example: Attempting to create a recipe (will fail without a real Bitmap)
                        /*
                        try {
                            val newRecipe = Recipe(
                                id = System.currentTimeMillis(), // Or generate ID elsewhere
                                name = name,
                                description = description,
                                ingredients = ingredients,
                                steps = steps,
                                link = link,
                                picture = placeholderBitmap, // <<<< PROBLEM: Needs a real Bitmap instance
                                isStarred = isStarredState,
                                type = recipeType
                            )
                            onSaveRecipe(newRecipe)
                        } catch (e: Exception) {
                            // Log error or show message to user about the missing Bitmap
                            println("Error creating Recipe: ${'$'}{e.message}. Bitmap is required.")
                        }
                        */
                        println("Save button clicked. Recipe Name: $name. Bitmap handling is required to save.")
                        // TODO: Implement actual save logic once Bitmap handling is decided.
                    },
                    enabled = name.isNotBlank() // Basic validation: name should not be empty
                ) {
                    Text("Save Recipe") // Localize
                }

                Button(onClick = onFillWithGemini) {
                    Text("Fill with Gemini") // Localize
                }
            }
        }
    }
}
