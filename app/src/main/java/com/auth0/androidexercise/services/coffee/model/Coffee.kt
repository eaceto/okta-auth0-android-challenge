package com.auth0.androidexercise.services.coffee.model

data class Coffee(
    val id: Int,
    val type: String,
    val title: String,
    val description: String,
    val ingredients: List<String>
) {
    companion object {
        const val TYPE_HOT = "hot"
        const val TYPE_ICED = "iced"
    }
}
