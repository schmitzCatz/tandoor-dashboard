package net.octosystems.smarthome.tandoordashboard.model

import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Recipe(
    val id: Long,
    val name: String,
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    val description: String = "",
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    val image: String = "",
    val steps: List<Step> = emptyList()
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class Step(
        val id: Long,
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        val name: String = "",
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        val description: String = "",
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        val instructionsMarkdown: String
    )
}