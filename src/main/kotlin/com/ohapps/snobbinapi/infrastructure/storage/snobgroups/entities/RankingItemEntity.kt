package com.ohapps.snobbinapi.infrastructure.storage.snobgroups.entities

import com.ohapps.snobbinapi.domain.snobgroups.RankingItem
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "ranking_items")
data class RankingItemEntity(
    @Id
    val id: UUID,
    @Column
    val groupId: UUID,
    @Column
    val description: String,
    @Column
    val ranked: Boolean,
    @Column
    val averageRanking: Double?,
    @OneToMany(mappedBy = "rankingItem", cascade = [CascadeType.ALL], orphanRemoval = true)
    val attributes: List<RankingItemAttributeEntity>,
) {
    companion object {
        fun from(rankingItem: RankingItem): RankingItemEntity {
            val rankingItemEntity = RankingItemEntity(
                id = rankingItem.id,
                groupId = rankingItem.groupId,
                description = rankingItem.description,
                ranked = rankingItem.ranked,
                averageRanking = rankingItem.averageRanking,
                attributes = emptyList(),
            )
            return rankingItemEntity.copy(
                attributes = rankingItem.attributes.map { RankingItemAttributeEntity.toEntity(rankingItemEntity, it) },
            )
        }
    }

    fun toModel() = RankingItem(
        id = this.id,
        groupId = this.groupId,
        description = this.description,
        ranked = this.ranked,
        averageRanking = this.averageRanking,
        attributes = this.attributes.map { it.toModel() },
    )
}
