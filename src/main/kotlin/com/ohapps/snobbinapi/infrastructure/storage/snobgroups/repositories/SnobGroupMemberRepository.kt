package com.ohapps.snobbinapi.infrastructure.storage.snobgroups.repositories

import com.ohapps.snobbinapi.infrastructure.storage.snobgroups.entities.SnobGroupMemberEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SnobGroupMemberRepository : JpaRepository<SnobGroupMemberEntity, UUID> {
    fun findByGroupId(groupId: UUID): List<SnobGroupMemberEntity>
}
