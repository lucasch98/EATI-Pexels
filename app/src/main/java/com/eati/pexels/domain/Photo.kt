package com.eati.pexels.domain

data class Photo(
    val id: Int,
    val width: Int,
    val height: Int,
    val originalPhoto: String,
    val postUrl: String,
    val photographer: String,
    val photographerUrl: String,
    val photographerId: Int,
    val avgColor: String,
    val liked: Boolean,
    val alt: String,
)