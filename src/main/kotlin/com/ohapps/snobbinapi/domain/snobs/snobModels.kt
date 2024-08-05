package com.ohapps.snobbinapi.domain.snobs

import com.ohapps.snobbinapi.domain.common.AuthUser

data class Snob(
    val id: String,
    val email: String,
    val firstName: String?,
    val lastName: String?,
) {
    companion object {
        fun from(authUser: AuthUser) = Snob(
            id = authUser.id,
            email = authUser.email.lowercase().trim(),
            firstName = "",
            lastName = "",
        )
    }

    fun applyUpdate(snobUpdate: SnobUpdate) = this.copy(
        firstName = snobUpdate.firstName,
        lastName = snobUpdate.lastName,
    )
}

data class SnobUpdate(
    val firstName: String,
    val lastName: String,
)
