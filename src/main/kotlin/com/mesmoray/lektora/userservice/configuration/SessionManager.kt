package com.mesmoray.lektora.userservice.configuration

import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Component
class SessionManager {
    private val userIds: ConcurrentHashMap<Long, UUID?> = ConcurrentHashMap()

    fun setUserId(userId: UUID) {
        userIds[Thread.currentThread().id] = userId
    }

    fun getUserId(): UUID? {
        return userIds[Thread.currentThread().id]
    }

    fun clearSession() {
        userIds.remove(Thread.currentThread().id)
    }
}
