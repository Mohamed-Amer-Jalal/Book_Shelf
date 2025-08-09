package com.m_amer.bookshelf.model

import kotlinx.serialization.Serializable

@Serializable
data class SaleInfo(
    val country: String?,
    val isEbook: Boolean?,
    val saleability: String?
)