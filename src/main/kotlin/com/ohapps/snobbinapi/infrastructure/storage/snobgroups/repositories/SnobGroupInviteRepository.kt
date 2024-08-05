package com.ohapps.snobbinapi.infrastructure.storage.snobgroups.repositories

import com.ohapps.snobbinapi.domain.snobgroups.InviteStatus
import com.ohapps.snobbinapi.infrastructure.storage.snobgroups.entities.SnobGroupInviteEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SnobGroupInviteRepository : JpaRepository<SnobGroupInviteEntity, UUID> {
    fun findAllByGroupId(groupId: UUID): List<SnobGroupInviteEntity>
    fun findAllByGroupIdAndEmail(groupId: UUID, email: String): List<SnobGroupInviteEntity>
    fun findAllByEmailAndStatus(email: String, status: InviteStatus): List<SnobGroupInviteEntity>
}
