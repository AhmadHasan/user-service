package com.mesmoray.lektora.userservice.configuration.interceptor.userid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class UserIdWebConfig : WebMvcConfigurer {

    @Autowired
    private lateinit var userIdInterceptor: UserIdInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(userIdInterceptor)
    }
}
