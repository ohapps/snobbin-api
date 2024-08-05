package com.ohapps.snobbinapi.infrastructure.storage.snobgroups.repositories

import com.ohapps.snobbinapi.infrastructure.storage.snobgroups.entities.RankingEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RankingRepository : JpaRepository<RankingEntity, UUID> {
    fun findAllByItemId(itemId: UUID): List<RankingEntity>
    fun findByItemIdAndGroupMemberId(itemId: UUID, groupMemberId: UUID): RankingEntity?
}