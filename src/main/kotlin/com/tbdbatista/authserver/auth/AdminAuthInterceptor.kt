package com.tbdbatista.authserver.auth

import com.tbdbatista.authserver.api.UnauthorizedException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@Component
class AdminAuthInterceptor(
    @Value("\${auth.adminToken:admin}") val adminToken: String
): HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val method = request.method.uppercase()
        if (method != "PUT" && method != "DELETE") {
            return true
        }
        val token = request.getHeader("X-Admin-Token")
        if (token == null || token != adminToken) {
            throw UnauthorizedException("missing or invalid admin token")
        }
        return true
    }
}

