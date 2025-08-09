package com.m_amer.bookshelf.screens.home.home_viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.m_amer.bookshelf.model.Book
import com.m_amer.bookshelf.model.MyUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HomeViewModel : ViewModel() {
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    fun getBooksInReadingList(userId: String?) {
        if (userId.isNullOrEmpty()) {
            _errorMessage.value = "User ID is null or empty"
            return
        }

        viewModelScope.launch {
            try {
                // 1️⃣ احضر بيانات المستخدم
                val userDoc = FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(userId)
                    .get()
                    .await()

                if (!userDoc.exists()) {
                    _errorMessage.value = "User not found"
                    return@launch
                }

                val shelves = userDoc.toObject(MyUser::class.java)?.shelves ?: emptyList()
                val readingShelf = shelves.find { it.name == "Reading Now 📖" }

                if (readingShelf == null || readingShelf.bookIds.isEmpty()) {
                    _errorMessage.value = "Reading list is empty"
                    _books.value = emptyList()
                    return@launch
                }

                // 2️⃣ لو الكتاب أكثر من 10، قسم الـ IDs إلى مجموعات
                val fetchedBooks = mutableListOf<Book>()
                val chunks = readingShelf.bookIds.chunked(10) // كل مجموعة <= 10

                for (chunk in chunks) {
                    val querySnapshot = FirebaseFirestore.getInstance()
                        .collection("books")
                        .whereIn("bookID", chunk)
                        .get()
                        .await()

                    val booksInChunk =
                        querySnapshot.documents.mapNotNull { it.toObject(Book::class.java) }
                    fetchedBooks.addAll(booksInChunk)
                }

                // 3️⃣ خزّن النتيجة في StateFlow
                _books.value = fetchedBooks

            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
            }
        }
    }
}