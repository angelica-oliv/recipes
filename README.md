# Minhas Receitas (My Recipes)

Minhas Receitas is a modern Android application that allows users to browse, save, and create recipes. The app leverages artificial intelligence to help users discover and create new recipes based on their preferences and available ingredients.

## About the App

Minhas Receitas is built with [Jetpack Compose][compose] and follows [Material 3 design principles][material3]. It is based on the [Reply sample app](https://github.com/android/compose-samples/tree/main/Reply) and provides a beautiful and intuitive interface for managing your favorite recipes.

Key features:
* Browse a collection of traditional Brazilian recipes
* View detailed ingredients and step-by-step cooking instructions
* AI-powered recipe suggestions based on your preferences
* Create and save your own recipes
* Adaptive UI that works seamlessly on phones, tablets, and foldable devices

## Screenshots

<img src="screenshots/medium_and_large_display.png">
<img src="screenshots/compact_medium_large_displays.png">

## Technical Features

### AI Integration
The app uses artificial intelligence to:
* Generate new recipe ideas based on available ingredients
* Suggest modifications to existing recipes
* Generate Pictures for given recipes

### Material 3 Design
Minhas Receitas implements Material 3 design principles with:
* Dynamic color theming that adapts to your device's wallpaper (on Android 12+)
* Responsive layouts for different screen sizes
* Smooth animations and transitions

### Adaptive UI
The app provides an optimal experience across different devices:
* Responsive layouts for phones, tablets, and foldable devices
* Dynamic content organization based on screen size
* Support for different navigation patterns depending on device form factor

### Data Management
The app uses a repository pattern for data management:
* Local storage of recipes
* Categorization by recipe type (Main Course, Dessert, Appetizer, etc.)
* Efficient search and filtering capabilities

## Getting Started

To try out this app, use the latest stable version of [Android Studio](https://developer.android.com/studio).
You can clone this repository.


[compose]: https://developer.android.com/jetpack/compose
[material3]: https://m3.material.io/
