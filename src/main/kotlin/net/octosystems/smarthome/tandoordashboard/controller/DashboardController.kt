package net.octosystems.smarthome.tandoordashboard.controller

import net.octosystems.smarthome.tandoordashboard.model.RecipeView
import net.octosystems.smarthome.tandoordashboard.service.TandoorService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class DashboardController(
    private val service: TandoorService,
) {

    @GetMapping
    fun index(model: Model): String {

        val plan = service.getMealPlan()

        if (plan.isNotEmpty()) {
            val recipes = plan.map { service.getRecipe(it.recipe) }.map { RecipeView.from(it) }
            model.addAttribute("recipes", recipes)
        }

        return "index"
    }
}