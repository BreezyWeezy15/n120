package com.nbc.nbcreplicate.models

data class HomePage(
    val page: String,
    val shelves: List<Shelf>
)

data class Shelf(
    val title: String,
    val type: String,
    val items: List<Item>
)

data class Item(
    val type: String,
    val title: String,
    val subtitle: String? = null,
    val image: String,
    val tagline: String? = null,
    val labelBadge: String? = null
)