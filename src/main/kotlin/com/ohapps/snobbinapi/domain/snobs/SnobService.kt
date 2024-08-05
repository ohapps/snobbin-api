package com.ohapps.snobbinapi.domain.snobs

import com.ohapps.snobbinapi.domain.common.AuthUser
import com.ohapps.snobbinapi.domain.common.SecurityService

class SnobService(
    private val securityService: SecurityService,
    private val snobStorage: SnobStorage,
) {
    fun getCurrentSnob(): Snob {
        val authUser = securityService.getAuthUser()
        return snobStorage.findById(authUser.id) ?: createNewSnob(authUser)
    }

    fun getSnob(id: String) = snobStorage.findById(id)

    fun updateCurrentSnob(snobUpdate: SnobUpdate): Snob {
        val snobInfo = getCurrentSnob()
        val updatedSnobInfo = snobInfo.applyUpdate(snobUpdate)
        snobStorage.save(updatedSnobInfo)
        return updatedSnobInfo
    }

    private fun createNewSnob(authUser: AuthUser): Snob {
        val newSnob = Snob.from(authUser)
        snobStorage.save(newSnob)
        return newSnob
    }
}
