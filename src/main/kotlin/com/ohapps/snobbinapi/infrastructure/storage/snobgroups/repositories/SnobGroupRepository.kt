package com.ohapps.snobbinapi.infrastructure.storage.snobgroups.repositories

import com.ohapps.snobbinapi.infrastructure.storage.snobgroups.entities.SnobGroupEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SnobGroupRepository : JpaRepository<SnobGroupEntity, UUID> {
    @Query(
        """
        select *
        from snob_groups
        where id in (
            select group_id
            from snob_group_members
            where snob_id = :snobId
        )
        and deleted = false
        """,
        nativeQuery = true,
    )
    fun findAllBySnobId(snobId: String): List<SnobGroupEntity>
}
