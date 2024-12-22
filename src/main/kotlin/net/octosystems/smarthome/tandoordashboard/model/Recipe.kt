package net.octosystems.smarthome.tandoordashboard.model

import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Recipe(
    val name: String,
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    val description: String = "",
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    val image: String = "",
    val steps: List<Step> = emptyList()
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class Step(
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        val instructionsMarkdown: String,
        val ingredients: List<Ingredient> = emptyList()
    ) {
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
        data class Ingredient(
            @JsonSetter(nulls = Nulls.AS_EMPTY)
            val amount: String,
            val unit: UnitOfMeasure?,
            val food: Food?
        ) {
            @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
            data class UnitOfMeasure(
                @JsonSetter(nulls = Nulls.AS_EMPTY)
                val name: String
            )

            @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
            data class Food(
                @JsonSetter(nulls = Nulls.AS_EMPTY)
                val name: String
            )
        }
    }
}