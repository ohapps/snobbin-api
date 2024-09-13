package com.ohapps.snobbinapi.domain.snobgroups

import com.ohapps.snobbinapi.domain.common.generateId
import java.util.UUID

data class SnobGroupAttribute(
    val id: UUID,
    val name: String,
)

data class SnobGroupAttributeUpdate(
    val id: UUID?,
    val name: String,
) {
    fun apply() = SnobGroupAttribute(
        id = this.id ?: generateId(),
        name = this.name,
    )
}

data class SnobGroup(
    val id: UUID,
    val name: String,
    val description: String,
    val minRanking: Int,
    val maxRanking: Int,
    val increments: Float,
    val rankIcon: String,
    val rankingsRequired: Int,
    val attributes: List<SnobGroupAttribute>,
) {
    companion object {
        fun from(snobGroupUpdate: SnobGroupUpdate) = SnobGroup(
            id = generateId(),
            name = snobGroupUpdate.name,
            description = snobGroupUpdate.description,
            minRanking = snobGroupUpdate.minRanking,
            maxRanking = snobGroupUpdate.maxRanking,
            increments = snobGroupUpdate.increments,
            rankIcon = snobGroupUpdate.rankIcon,
            rankingsRequired = snobGroupUpdate.rankingsRequired,
            attributes = snobGroupUpdate.attributes.map { it.apply() },
        )
    }

    fun applyUpdate(snobGroupUpdate: SnobGroupUpdate) = this.copy(
        name = snobGroupUpdate.name,
        description = snobGroupUpdate.description,
        minRanking = snobGroupUpdate.minRanking,
        maxRanking = snobGroupUpdate.maxRanking,
        increments = snobGroupUpdate.increments,
        rankIcon = snobGroupUpdate.rankIcon,
        rankingsRequired = snobGroupUpdate.rankingsRequired,
        attributes = snobGroupUpdate.attributes.map { it.apply() },
    )
}

data class SnobGroupUpdate(
    val name: String,
    val description: String,
    val minRanking: Int,
    val maxRanking: Int,
    val increments: Float,
    val rankIcon: String,
    val rankingsRequired: Int,
    val attributes: List<SnobGroupAttributeUpdate>,
)

enum class SnobGroupMemberRole {
    MEMBER,
    ADMIN,
}

val adminRoles = listOf(SnobGroupMemberRole.ADMIN)
val allRoles = listOf(SnobGroupMemberRole.ADMIN, SnobGroupMemberRole.MEMBER)

data class SnobGroupMember(
    val id: UUID,
    val groupId: UUID,
    val snobId: String,
    val role: SnobGroupMemberRole,
)

enum class InviteStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
}

data class SnobGroupInvite(
    val id: UUID,
    val groupId: UUID,
    val email: String,
    val status: InviteStatus,
) {
    companion object {
        fun from(snobGroupInviteRequest: SnobGroupInviteRequest) = SnobGroupInvite(
            id = generateId(),
            groupId = snobGroupInviteRequest.groupId,
            email = snobGroupInviteRequest.email.lowercase().trim(),
            status = InviteStatus.PENDING,
        )
    }
}

data class SnobGroupInviteRequest(
    val groupId: UUID,
    val email: String,
)

data class RankingItemAttribute(
    val id: UUID,
    val attributeId: UUID,
    val attributeValue: String,
)

data class RankingItem(
    val id: UUID,
    val groupId: UUID,
    val description: String,
    val ranked: Boolean,
    val averageRanking: Double? = null,
    val attributes: List<RankingItemAttribute> = emptyList(),
) {
    companion object {
        fun from(snobGroup: SnobGroup, rankingItemUpdate: RankingItemUpdate) =
            RankingItem(
                id = generateId(),
                groupId = snobGroup.id,
                description = rankingItemUpdate.description,
                ranked = false,
                attributes = rankingItemUpdate.attributes.map { it.apply() },
            )
    }

    fun applyUpdate(rankingItemUpdate: RankingItemUpdate) = this.copy(
        description = rankingItemUpdate.description,
        attributes = rankingItemUpdate.attributes.map { it.apply() },
    )
}

data class RankingItemResults(
    val total: Long,
    val pages: Int,
    val items: List<RankingItem>
)

data class RankingItemAttributeUpdate(
    val id: UUID?,
    val attributeId: UUID,
    val attributeValue: String,
) {
    fun apply() = RankingItemAttribute(
        id = this.id ?: generateId(),
        attributeId = this.attributeId,
        attributeValue = this.attributeValue,
    )
}

data class RankingItemUpdate(
    val description: String,
    val attributes: List<RankingItemAttributeUpdate>,
)

data class Ranking(
    val id: UUID,
    val itemId: UUID,
    val groupMemberId: UUID,
    val rank: Double,
    val notes: String?,
) {
    companion object {
        fun from(rankingItem: RankingItem, snobGroupMember: SnobGroupMember, rankingUpdate: RankingUpdate) =
            Ranking(
                id = generateId(),
                itemId = rankingItem.id,
                groupMemberId = snobGroupMember.id,
                rank = rankingUpdate.rank,
                notes = rankingUpdate.notes,
            )
    }

    fun applyUpdate(rankingUpdate: RankingUpdate) = this.copy(
        rank = rankingUpdate.rank,
        notes = rankingUpdate.notes,
    )
}

data class RankingUpdate(
    val rank: Double,
    val notes: String?,
)

enum class RankingSortBy(val column: String) {
    AVERAGE_RANKING("average_ranking"),
    DESCRIPTION("description"),
    RANKED("ranked")
}

enum class RankingSortDirection {
    ASC,
    DESC
}
