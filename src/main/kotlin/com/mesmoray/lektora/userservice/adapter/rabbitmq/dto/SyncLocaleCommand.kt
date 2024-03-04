package com.mesmoray.lektora.userservice.adapter.rabbitmq.dto

data class SyncLocaleCommand(
    val locales: List<Locale>
)
