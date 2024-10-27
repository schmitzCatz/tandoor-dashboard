package net.octosystems.smarthome.tandoordashboard.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Meal(
    val id: Long,
    val title: String,
    val recipe: Recipe
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class Recipe(
        val id: Long,
        val name: String,
        val image: String,
    )
}