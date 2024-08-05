package com.ohapps.snobbinapi.application.controllers

import com.ohapps.snobbinapi.domain.snobgroups.*
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import java.util.UUID

@Controller
class SnobGroupController(
    val snobGroupService: SnobGroupService,
) {
    @QueryMapping
    fun myGroups() = snobGroupService.myGroups()

    @MutationMapping
    fun createGroup(@Argument snobGroupUpdate: SnobGroupUpdate) = snobGroupService.createGroup(snobGroupUpdate)

    @MutationMapping
    fun updateGroup(@Argument id: UUID, @Argument snobGroupUpdate: SnobGroupUpdate) =
        snobGroupService.updateGroup(id, snobGroupUpdate)

    @MutationMapping
    fun deleteGroup(@Argument id: UUID) = snobGroupService.deleteGroup(id)

    @MutationMapping
    fun createRankingItem(@Argument groupId: UUID, @Argument rankingItemUpdate: RankingItemUpdate) =
        snobGroupService.createRankingItem(groupId, rankingItemUpdate)

    @MutationMapping
    fun updateRankingItem(@Argument id: UUID, @Argument rankingItemUpdate: RankingItemUpdate) =
        snobGroupService.updateRankingItem(id, rankingItemUpdate)

    @MutationMapping
    fun deleteRankingItem(@Argument id: UUID) = snobGroupService.deleteRankingItem(id)

    @QueryMapping
    fun getRankingItems(@Argument groupId: UUID) = snobGroupService.getRankingItems(groupId)

    @QueryMapping
    fun searchRankingItems(
        @Argument groupId: UUID,
        @Argument keyword: String,
        @Argument page: Int,
        @Argument limit: Int,
        @Argument sort: RankingSortBy?,
        @Argument dir: RankingSortDirection?,
    ) = snobGroupService.searchRankingItems(groupId, keyword, page, limit, sort, dir)

    @MutationMapping
    fun createRanking(@Argument itemId: UUID, @Argument rankingUpdate: RankingUpdate) =
        snobGroupService.createRanking(itemId, rankingUpdate)

    @MutationMapping
    fun updateRanking(@Argument id: UUID, @Argument rankingUpdate: RankingUpdate) =
        snobGroupService.updateRanking(id, rankingUpdate)

    @MutationMapping
    fun deleteRanking(@Argument id: UUID) = snobGroupService.deleteRanking(id)

    @QueryMapping
    fun getRankings(@Argument itemId: UUID) = snobGroupService.getRankings(itemId)

    @SchemaMapping(typeName = "SnobGroup", field = "rankingItems")
    fun rankingItem(snobGroup: SnobGroup) = snobGroupService.getRankingItems(snobGroup.id)

    @SchemaMapping(typeName = "SnobGroup", field = "groupMembers")
    fun groupMembers(snobGroup: SnobGroup) = snobGroupService.getGroupMembers(snobGroup.id)

    @MutationMapping
    fun createGroupInvite(@Argument snobGroupInviteRequest: SnobGroupInviteRequest) =
        snobGroupService.createGroupInvite(snobGroupInviteRequest)

    @MutationMapping
    fun deleteGroupInvite(@Argument id: UUID) = snobGroupService.deleteGroupInvite(id)

    @MutationMapping
    fun acceptGroupInvite(@Argument id: UUID) = snobGroupService.acceptGroupInvite(id)

    @MutationMapping
    fun declineGroupInvite(@Argument id: UUID) = snobGroupService.declineGroupInvite(id)

    @QueryMapping
    fun getPendingGroupInvites() = snobGroupService.getPendingGroupInvites()

    @SchemaMapping(typeName = "SnobGroup", field = "groupInvites")
    fun groupInvites(snobGroup: SnobGroup) = snobGroupService.getGroupInvites(snobGroup.id)

    @SchemaMapping(typeName = "SnobGroupInvite", field = "group")
    fun group(snobGroupInvite: SnobGroupInvite) = snobGroupService.getGroup(snobGroupInvite.groupId)
}
