package com.mesmoray.lektora.userservice.model

import org.springframework.data.annotation.Id
import java.time.ZonedDateTime
import java.util.UUID

data class User(
    @Id
    val id: UUID = UUID.randomUUID(),
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    val profile: Profile
)
