package com.mesmoray.lektora.userservice.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("user-service")
class UserServiceConfig {
    lateinit var upstreams: Map<String, String>
}
