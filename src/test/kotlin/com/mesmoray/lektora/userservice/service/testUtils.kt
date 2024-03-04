package com.mesmoray.lektora.userservice.service

import java.nio.charset.StandardCharsets

class testUtils {

    companion object {
        fun loadFileFromResources(fileName: String): String {
            return Thread.currentThread().contextClassLoader.getResourceAsStream(fileName).use { inputStream ->
                inputStream?.readBytes()?.toString(StandardCharsets.UTF_8) ?: throw IllegalArgumentException("File not found")
            }
        }
    }
}
