package com.ohapps.snobbinapi.infrastructure.storage.snobgroups.repositories

import com.ohapps.snobbinapi.infrastructure.storage.snobgroups.entities.RankingItemEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RankingItemRepository : JpaRepository<RankingItemEntity, UUID> {
    fun findAllByGroupId(groupId: UUID): List<RankingItemEntity>

    @Query(
        value = """
        select distinct ri.*
        from ranking_items ri
        left join ranking_item_attributes ria on (ri.id = ria.item_id)
        where ri.group_id = :groupId
        and (
            ri.description ilike :keyword or
            ria.attribute_value ilike :keyword
        )
        """,
        nativeQuery = true,
    )
    fun searchByGroupIdAndKeyword(groupId: UUID, keyword: String, pageable: Pageable): Page<RankingItemEntity>
}
