package com.mesmoray.lektora.userservice.configuration.useragent

import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class UserAgentInterceptor : HandlerInterceptor {
    companion object {
        const val USER_AGENT_HEADER = "User-Agent"
        const val USER_AGENT_MDC_KEY = "userAgent"
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val traceId = request.getHeader(USER_AGENT_HEADER) ?: "Not Found"
        MDC.put(USER_AGENT_MDC_KEY, traceId)
        return true
    }
}
