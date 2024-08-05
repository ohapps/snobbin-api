package com.ohapps.snobbinapi.infrastructure.storage.snobs

import com.ohapps.snobbinapi.domain.snobs.Snob
import com.ohapps.snobbinapi.domain.snobs.SnobStorage
import com.ohapps.snobbinapi.infrastructure.storage.snobs.entities.SnobEntity
import com.ohapps.snobbinapi.infrastructure.storage.snobs.repositories.SnobRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class SnobStorageImpl(
    private val snobRepository: SnobRepository,
) : SnobStorage {
    override fun findById(id: String) = snobRepository.findById(id).getOrNull()?.toModel()
    override fun save(snob: Snob) {
        snobRepository.save(SnobEntity.toEntity(snob))
    }
}
