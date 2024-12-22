package net.octosystems.smarthome.tandoordashboard.model

data class RecipeView(
    val name: String,
    val image: String,
    val instructions: String,
    val ingredients: List<String> = emptyList(),
) {
    companion object {
        fun from(recipe: Recipe): RecipeView = RecipeView(
            name = recipe.name,
            image = recipe.image,
            instructions = recipe.steps.joinToString { it.instructionsMarkdown },
            ingredients = recipe.steps.flatMap { it.ingredients }.map { if(it.food != null) "${it.amount} ${it.unit?.name} ${it.food.name}" else ""}
        )
    }
}