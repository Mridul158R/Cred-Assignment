package com.example.credassignment.models

data class BodyX(
    val card: Card,
    val footer: String,
    val items: List<ItemX>,
    val subtitle: String,
    val title: String
)