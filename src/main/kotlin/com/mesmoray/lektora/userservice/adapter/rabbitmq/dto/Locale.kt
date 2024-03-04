package com.mesmoray.lektora.userservice.adapter.rabbitmq.dto

data class Locale(
    val languageCode: String,
    val countryCode: String,
    val code: String = "$languageCode-$countryCode"
)
