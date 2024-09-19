package com.mesmoray.lektora.userservice.adapter.controller.dto

data class UserProfileDTO(
    val countryCodes: List<String>,
    val languageCodes: List<String>,
    val publisherNames: List<String> = listOf()
)
