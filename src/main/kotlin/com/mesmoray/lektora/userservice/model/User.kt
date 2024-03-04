package com.mesmoray.lektora.userservice.model

import org.springframework.data.annotation.Id
import java.util.Date
import java.util.UUID

data class User(
    @Id
    val value: UUID = UUID.randomUUID(),
    val createdAt: Date = Date(),
    val profile: Profile
)
