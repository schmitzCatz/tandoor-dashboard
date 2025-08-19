package net.octosystems.smarthome.tandoordashboard.model

import java.net.URI

data class Page (
    val count: Int = 0,
    val next: URI? = null,
    val prev: URI? = null,
    val results: List<Meal> = emptyList(),
)