package com.ohapps.snobbinapi.domain.snobgroups

import java.util.UUID

interface SnobGroupStorage {
    fun save(snobGroup: SnobGroup): SnobGroup
    fun delete(snobGroup: SnobGroup)
    fun findAllBySnobId(snobId: String): List<SnobGroup>
    fun findById(id: UUID): SnobGroup?

    fun saveMember(snobGroupMember: SnobGroupMember): SnobGroupMember
    fun findMembersByGroupId(groupId: UUID): List<SnobGroupMember>

    fun saveRankingItem(rankingItem: RankingItem)
    fun deleteRankingItem(rankingItem: RankingItem)
    fun findRankingItemById(id: UUID): RankingItem?
    fun findRankingItemsByGroup(groupId: UUID): List<RankingItem>
    fun searchRankingItemsByGroup(groupId: UUID, keyword: String, page: Int, limit: Int, sort: RankingSortBy?, dir: RankingSortDirection?): RankingItemResults

    fun saveRanking(ranking: Ranking)
    fun deleteRanking(ranking: Ranking)
    fun findRankingById(id: UUID): Ranking?
    fun findExistingRankingForMember(itemId: UUID, groupMemberId: UUID): Ranking?
    fun findRankingsByItemId(itemId: UUID): List<Ranking>

    fun saveGroupInvite(snobGroupInvite: SnobGroupInvite)
    fun deleteGroupInvite(snobGroupInvite: SnobGroupInvite)
    fun findGroupInvites(groupId: UUID, email: String): List<SnobGroupInvite>
    fun findGroupInvites(groupId: UUID): List<SnobGroupInvite>
    fun findPendingGroupInvites(email: String): List<SnobGroupInvite>
    fun findGroupInvite(id: UUID): SnobGroupInvite?
}
