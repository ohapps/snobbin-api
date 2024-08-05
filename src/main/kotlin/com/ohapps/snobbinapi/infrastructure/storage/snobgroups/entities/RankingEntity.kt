package com.ohapps.snobbinapi.infrastructure.storage.snobgroups.entities

import com.ohapps.snobbinapi.domain.snobgroups.Ranking
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "rankings")
data class RankingEntity(
    @Id
    val id: UUID,
    @Column
    val itemId: UUID,
    @Column
    val groupMemberId: UUID,
    @Column
    val ranking: Double,
    @Column
    val notes: String?,
) {
    companion object {
        fun from(ranking: Ranking) = RankingEntity(
            id = ranking.id,
            itemId = ranking.itemId,
            groupMemberId = ranking.groupMemberId,
            ranking = ranking.rank,
            notes = ranking.notes,
        )
    }

    fun toModel() = Ranking(
        id = this.id,
        itemId = this.itemId,
        groupMemberId = this.groupMemberId,
        rank = this.ranking,
        notes = this.notes,
    )
}
