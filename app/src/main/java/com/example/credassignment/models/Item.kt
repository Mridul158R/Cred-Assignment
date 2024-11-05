package com.example.credassignment.models

data class Item(
    val closed_state: ClosedState,
    val cta_text: String,
    val open_state: OpenState
)