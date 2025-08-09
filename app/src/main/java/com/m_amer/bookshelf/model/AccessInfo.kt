package com.m_amer.bookshelf.model

import kotlinx.serialization.Serializable
@Serializable
data class AccessInfo(
    val accessViewStatus: String?,
    val country: String?,
    val embeddable: Boolean?,
    val epub: Epub?,
    val pdf: Pdf?,
    val publicDomain: Boolean?,
    val quoteSharingAllowed: Boolean?,
    val textToSpeechPermission: String?,
    val viewability: String?,
    val webReaderLink: String?
)

@Serializable
data class Epub(
    val acsTokenLink: String?,
    val isAvailable: Boolean?
)

@Serializable
data class Pdf(
    val acsTokenLink: String?,
    val isAvailable: Boolean?
)