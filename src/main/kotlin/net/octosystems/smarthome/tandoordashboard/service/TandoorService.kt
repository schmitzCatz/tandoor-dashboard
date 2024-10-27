package net.octosystems.smarthome.tandoordashboard.service

import net.octosystems.smarthome.tandoordashboard.model.Meal
import net.octosystems.smarthome.tandoordashboard.model.Recipe
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Service
class TandoorService(
    @Value("\${tandoor.url}") val baseUrl: String,
    @Value("\${tandoor.token}") val token: String,
) {

    fun getMealPlan(from: Instant = Instant.now(), to: Instant = Instant.now()): List<Meal> = request<Array<Meal>> {
        token(token)
        uri("$baseUrl/api/meal-plan")
        responseType(Array<Meal>::class)
        query {
            param("from_date", from.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            param("to_date", to.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        }
    }.body?.toList() ?: emptyList()

    fun getRecipe(id: Long): Recipe = request<Recipe> {
        token(token)
        uri("$baseUrl/api/recipe/$id")
        responseType(Recipe::class)
    }.body?: throw IllegalArgumentException("No recipe found")

    fun getRecipe(recipe: Meal.Recipe): Recipe = getRecipe(recipe.id)

}