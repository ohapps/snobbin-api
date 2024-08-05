package com.ohapps.snobbinapi.infrastructure.storage.snobgroups.entities

import com.ohapps.snobbinapi.domain.snobgroups.RankingItemAttribute
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "ranking_item_attributes")
data class RankingItemAttributeEntity(
    @Id
    val id: UUID,
    @Column
    val attributeId: UUID,
    @Column
    val attributeValue: String,
    @ManyToOne
    @JoinColumn(name = "item_id")
    val rankingItem: RankingItemEntity,
) {
    companion object {
        fun toEntity(rankingItem: RankingItemEntity, rankingItemAttribute: RankingItemAttribute) =
            RankingItemAttributeEntity(
                id = rankingItemAttribute.id,
                attributeId = rankingItemAttribute.attributeId,
                attributeValue = rankingItemAttribute.attributeValue,
                rankingItem = rankingItem,
            )
    }

    fun toModel() = RankingItemAttribute(
        id = this.id,
        attributeId = this.attributeId,
        attributeValue = this.attributeValue,
    )
}
