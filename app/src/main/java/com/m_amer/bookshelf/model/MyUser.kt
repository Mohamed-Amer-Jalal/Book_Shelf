package com.m_amer.bookshelf.model

// MyUser: قيم افتراضية تجعل no-arg constructor غير ضروري وتضمن ديسيريالايز آمن
data class MyUser(
    val displayName: String = "",
    val userID: String = "",
    val avatar: String? = null,
    val shelves: List<Shelf> = emptyList(),
    val searchHistory: List<String> = emptyList(), // book ids
    val reviews: List<Review> = emptyList(),
    val favourites: List<String> = emptyList() // store book ids
)

// Shelf: immutable API (List) لكن يمكن استبدال المرجع عند الحاجة
data class Shelf(
    val name: String = "",
    val bookIds: List<String> = emptyList() // store ids not whole Book objects
) {
    constructor() : this("", emptyList())
}

// Review: نخزن bookId بدل كائن Book لتقليل الحجم
data class Review(
    val bookId: String = "",
    val rating: Double = 0.0,
    val reviewText: String = ""
)