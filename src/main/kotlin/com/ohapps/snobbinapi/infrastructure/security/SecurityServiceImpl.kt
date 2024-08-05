package com.ohapps.snobbinapi.infrastructure.security

import com.ohapps.snobbinapi.domain.common.AuthUser
import com.ohapps.snobbinapi.domain.common.SecurityService
import com.ohapps.snobbinapi.domain.common.InvalidUserToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service

@Service
class SecurityServiceImpl : SecurityService {

    override fun getAuthUser(): AuthUser {
        val auth: Authentication = SecurityContextHolder.getContext().authentication

        if (auth.name == null) {
            throw InvalidUserToken("token missing user id")
        }

        val jwt: Jwt = auth.principal as Jwt

        val email = if (jwt.claims.containsKey("https://ohapps.com/email")) {
            jwt.claims["https://ohapps.com/email"] as String
        } else {
            ""
        }

        return AuthUser(auth.name, email)
    }
}
