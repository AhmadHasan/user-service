package com.mesmoray.lektora.userservice.adapter.web.input

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/health")
class HealthCheckController {

    @GetMapping
    fun checkHealth(): ResponseEntity<String> {
        val status = performHealthCheck()
        return if (status) {
            ResponseEntity.ok("Application is healthy")
        } else {
            ResponseEntity.status(500).body("Application is unhealthy")
        }
    }

    private fun performHealthCheck(): Boolean {
        // Perform health check logic here
        // Return true if the application is healthy, false otherwise
        // For example, you can check the connectivity to the database or any other dependencies
        // and return true if they are all functioning correctly
        return true
    }
}
