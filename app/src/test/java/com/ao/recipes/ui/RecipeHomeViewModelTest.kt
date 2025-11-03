package com.ao.recipes.ui

import android.content.Context
import com.ao.recipes.data.Recipe
import com.ao.recipes.data.RecipesRepository
import com.ao.recipes.data.RecipeType
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RecipeHomeViewModelTest {

    private lateinit var viewModel: RecipeHomeViewModel
    private lateinit var repository: RecipesRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        viewModel = RecipeHomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addRecipe calls repository's addRecipe`() = runTest {
        // Given
        val mockContext = mockk<Context>()
        val newRecipe = Recipe(
            id = 1,
            name = "Test Recipe",
            description = "Test Description",
            ingredients = "Ingredient 1",
            steps = "Step 1",
            type = RecipeType.MAIN_COURSE,
            picture = mockk()
        )

        // When
        viewModel.addRecipe(mockContext, newRecipe)
        runCurrent()

        // Then
        coVerify { repository.addRecipe(mockContext, newRecipe) }
    }
}
