package com.mesmoray.lektora.userservice.model

data class Profile(
    val countryCodes: List<String>,
    val languageCodes: List<String>,
    val publisherNames: List<String> = emptyList()
)
