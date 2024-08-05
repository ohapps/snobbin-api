package com.ohapps.snobbinapi.infrastructure.storage.snobs.repositories

import com.ohapps.snobbinapi.infrastructure.storage.snobs.entities.SnobEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SnobRepository : JpaRepository<SnobEntity, String>
