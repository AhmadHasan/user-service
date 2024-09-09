package com.mesmoray.lektora.userservice.configuration.interceptor.userid

import com.mesmoray.lektora.userservice.configuration.SessionManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.util.UUID
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class UserIdInterceptor : HandlerInterceptor {

    companion object {
        const val USER_ID_HEADER = "X-USER-ID"
    }

    @Autowired
    private lateinit var sessionManager: SessionManager

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val userIdHeader = request.getHeader(USER_ID_HEADER)
        if (userIdHeader != null) {
            sessionManager.setUserId(UUID.fromString(userIdHeader))
        }
        return true
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        sessionManager.clearSession()
    }
}
