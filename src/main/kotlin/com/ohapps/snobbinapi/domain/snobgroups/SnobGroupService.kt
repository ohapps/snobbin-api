package com.ohapps.snobbinapi.domain.snobgroups

import com.ohapps.snobbinapi.domain.common.*
import java.util.*

class SnobGroupService(
    private val securityService: SecurityService,
    private val snobGroupStorage: SnobGroupStorage,
) {
    fun myGroups(): List<SnobGroup> {
        val authUser = securityService.getAuthUser()
        return snobGroupStorage.findAllBySnobId(authUser.id)
    }

    fun getGroup(id: UUID) = snobGroupStorage.findById(id) ?: throw DataNotFound("snob group $id not found")

    fun createGroup(snobGroupUpdate: SnobGroupUpdate): SnobGroup {
        val authUser = securityService.getAuthUser()
        val snobGroup = SnobGroup.from(snobGroupUpdate)
        snobGroupStorage.save(snobGroup)
        val snobGroupMember = SnobGroupMember(
            id = generateId(),
            groupId = snobGroup.id,
            snobId = authUser.id,
            role = SnobGroupMemberRole.ADMIN,
        )
        snobGroupStorage.saveMember(snobGroupMember)
        val snobGroupInvite = SnobGroupInvite(
            id = generateId(),
            groupId = snobGroup.id,
            email = authUser.email.lowercase().trim(),
            status = InviteStatus.ACCEPTED,
        )
        snobGroupStorage.saveGroupInvite(snobGroupInvite)
        return snobGroup
    }

    fun updateGroup(id: UUID, snobGroupUpdate: SnobGroupUpdate): SnobGroup {
        val (snobGroup) = findGroupForUserWithRoles(id, adminRoles)
        val updatedSnobGroup = snobGroup.applyUpdate(snobGroupUpdate)
        snobGroupStorage.save(updatedSnobGroup)
        return updatedSnobGroup
    }

    fun deleteGroup(id: UUID): SnobGroup {
        val (snobGroup) = findGroupForUserWithRoles(id, adminRoles)
        snobGroupStorage.delete(snobGroup)
        return snobGroup
    }

    fun getRankingItems(groupId: UUID): List<RankingItem> {
        val (snobGroup) = findGroupForUserWithRoles(groupId, allRoles)
        return snobGroupStorage.findRankingItemsByGroup(snobGroup.id)
    }

    fun searchRankingItems(groupId: UUID, keyword: String, page: Int, limit: Int, sort: RankingSortBy?, dir: RankingSortDirection?): RankingItemResults {
        val (snobGroup) = findGroupForUserWithRoles(groupId, allRoles)
        return snobGroupStorage.searchRankingItemsByGroup(snobGroup.id, keyword, page, limit, sort, dir)
    }

    fun getGroupMembers(groupId: UUID): List<SnobGroupMember> {
        val (snobGroup) = findGroupForUserWithRoles(groupId, allRoles)
        return snobGroupStorage.findMembersByGroupId(snobGroup.id)
    }

    fun createRankingItem(groupId: UUID, rankingItemUpdate: RankingItemUpdate): RankingItem {
        val (snobGroup) = findGroupForUserWithRoles(groupId, allRoles)
        val rankingItem = RankingItem.from(snobGroup, rankingItemUpdate)
        snobGroupStorage.saveRankingItem(rankingItem)
        return rankingItem
    }

    fun updateRankingItem(id: UUID, rankingItemUpdate: RankingItemUpdate): RankingItem {
        val (rankingItem) = findRankingItemAndGroupForUpdate(id)
        val updatedRankingItem = rankingItem.applyUpdate(rankingItemUpdate)
        snobGroupStorage.saveRankingItem(updatedRankingItem)
        return updatedRankingItem
    }

    fun deleteRankingItem(id: UUID): RankingItem {
        val (rankingItem) = findRankingItemAndGroupForUpdate(id)
        snobGroupStorage.deleteRankingItem(rankingItem)
        return rankingItem
    }

    fun getRankings(itemId: UUID): List<Ranking> {
        val (rankingItem) = findRankingItemAndGroupForUpdate(itemId)
        return snobGroupStorage.findRankingsByItemId(rankingItem.id)
    }

    fun createRanking(itemId: UUID, rankingUpdate: RankingUpdate): Ranking {
        val (rankingItem, snobGroup, snobGroupMember) = findRankingItemAndGroupForUpdate(itemId)
        val existingRanking = snobGroupStorage.findExistingRankingForMember(rankingItem.id, snobGroupMember.id)
        if (existingRanking != null) {
            return updateRanking(existingRanking.id, rankingUpdate)
        }
        val ranking = Ranking.from(rankingItem, snobGroupMember, rankingUpdate)
        snobGroupStorage.saveRanking(ranking)
        calculateRankings(rankingItem, snobGroup)
        return ranking
    }

    fun updateRanking(id: UUID, rankingUpdate: RankingUpdate): Ranking {
        val (ranking, rankingItem, snobGroup) = findRankingInfoForUpdate(id)
        val updatedRanking = ranking.applyUpdate(rankingUpdate)
        snobGroupStorage.saveRanking(updatedRanking)
        calculateRankings(rankingItem, snobGroup)
        return updatedRanking
    }

    fun deleteRanking(id: UUID): Ranking {
        val (ranking, rankingItem, snobGroup) = findRankingInfoForUpdate(id)
        snobGroupStorage.deleteRanking(ranking)
        calculateRankings(rankingItem, snobGroup)
        return ranking
    }

    fun createGroupInvite(snobGroupInviteRequest: SnobGroupInviteRequest): SnobGroupInvite {
        val (snobGroup) = findGroupForUserWithRoles(snobGroupInviteRequest.groupId, adminRoles)
        val existingInvites = snobGroupStorage.findGroupInvites(snobGroup.id, snobGroupInviteRequest.email.lowercase().trim())

        if (existingInvites.any { it.status === InviteStatus.ACCEPTED }) {
            throw BadRequest("user with same email has already accepted")
        }

        val pendingInvite = existingInvites.firstOrNull { it.status == InviteStatus.PENDING }
        if (pendingInvite != null) {
            return pendingInvite
        }
        val snobGroupInvite = SnobGroupInvite.from(snobGroupInviteRequest)
        snobGroupStorage.saveGroupInvite(snobGroupInvite)
        // TODO: send email for group invite
        return snobGroupInvite
    }

    fun deleteGroupInvite(id: UUID): SnobGroupInvite {
        val groupInvite = snobGroupStorage.findGroupInvite(id) ?: throw DataNotFound("group invite $id not found")
        findGroupForUserWithRoles(groupInvite.groupId, adminRoles)
        snobGroupStorage.deleteGroupInvite(groupInvite)
        return groupInvite
    }

    fun acceptGroupInvite(id: UUID): SnobGroupInvite {
        val groupInvite = snobGroupStorage.findGroupInvite(id) ?: throw DataNotFound("group invite $id not found")
        val authUser = securityService.getAuthUser()
        if (groupInvite.email != authUser.email) {
            throw AccessDenied("access denied to invite $id for email ${authUser.email}")
        }
        val snobGroupMember = SnobGroupMember(
            id = generateId(),
            groupId = groupInvite.groupId,
            snobId = authUser.id,
            role = SnobGroupMemberRole.ADMIN,
        )
        snobGroupStorage.saveMember(snobGroupMember)
        val updatedInvite = groupInvite.copy(status = InviteStatus.ACCEPTED)
        snobGroupStorage.saveGroupInvite(updatedInvite)
        return updatedInvite
    }

    fun declineGroupInvite(id: UUID): SnobGroupInvite {
        val groupInvite = snobGroupStorage.findGroupInvite(id) ?: throw DataNotFound("group invite $id not found")
        val authUser = securityService.getAuthUser()
        if (groupInvite.email != authUser.email) {
            throw AccessDenied("access denied to invite $id for email ${authUser.email}")
        }
        val updatedInvite = groupInvite.copy(status = InviteStatus.REJECTED)
        snobGroupStorage.saveGroupInvite(updatedInvite)
        return updatedInvite
    }

    fun getGroupInvites(id: UUID) = snobGroupStorage.findGroupInvites(id)

    fun getPendingGroupInvites(): List<SnobGroupInvite> {
        val authUser = securityService.getAuthUser()
        return snobGroupStorage.findPendingGroupInvites(authUser.email)
    }

    private fun checkAccess(
        authUser: AuthUser,
        snobGroup: SnobGroup,
        roles: List<SnobGroupMemberRole>,
    ): SnobGroupMember {
        val groupMembers = snobGroupStorage.findMembersByGroupId(snobGroup.id)
        return groupMembers.firstOrNull { it.snobId == authUser.id && roles.contains(it.role) }
            ?: throw AccessDenied("auth user ${authUser.id} does not have access to snob group ${snobGroup.id}")
    }

    private fun findGroupForUserWithRoles(
        id: UUID,
        roles: List<SnobGroupMemberRole>,
    ): Pair<SnobGroup, SnobGroupMember> {
        val authUser = securityService.getAuthUser()
        val snobGroup = snobGroupStorage.findById(id) ?: throw DataNotFound("snob group $id not found")
        val snobGroupMember = checkAccess(authUser, snobGroup, roles)
        return Pair(snobGroup, snobGroupMember)
    }

    private fun findRankingItemAndGroupForUpdate(id: UUID): Triple<RankingItem, SnobGroup, SnobGroupMember> {
        val rankingItem =
            snobGroupStorage.findRankingItemById(id) ?: throw DataNotFound("ranking item not found with id $id")
        val (snobGroup, snobGroupMember) = findGroupForUserWithRoles(rankingItem.groupId, allRoles)
        return Triple(rankingItem, snobGroup, snobGroupMember)
    }

    private fun findRankingInfoForUpdate(id: UUID): Triple<Ranking, RankingItem, SnobGroup> {
        val ranking = snobGroupStorage.findRankingById(id) ?: throw DataNotFound("invalid ranking id")
        val (rankingItem, snobGroup, snobGroupMember) = findRankingItemAndGroupForUpdate(ranking.itemId)
        if (ranking.groupMemberId != snobGroupMember.id) {
            throw AccessDenied("auth user ${snobGroupMember.snobId} does not have access to ranking ${ranking.id}")
        }
        return Triple(ranking, rankingItem, snobGroup)
    }

    private fun calculateRankings(rankingItem: RankingItem, snobGroup: SnobGroup) {
        val rankings = snobGroupStorage.findRankingsByItemId(rankingItem.id)
        val ranked = (rankings.size >= snobGroup.rankingsRequired)
        val averageRanking = rankings.map { it.rank }.average().roundToIncrement(snobGroup.increments)
        val updatedRankingItem = rankingItem.copy(
            ranked = ranked,
            averageRanking = averageRanking,
        )
        snobGroupStorage.saveRankingItem(updatedRankingItem)
    }
}
