package com.ohapps.snobbinapi.infrastructure.storage.snobgroups

import com.ohapps.snobbinapi.domain.snobgroups.*
import com.ohapps.snobbinapi.infrastructure.storage.snobgroups.entities.*
import com.ohapps.snobbinapi.infrastructure.storage.snobgroups.repositories.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class SnobGroupStorageImpl(
    private val snobGroupRepository: SnobGroupRepository,
    private val snobGroupMemberRepository: SnobGroupMemberRepository,
    private val rankingItemRepository: RankingItemRepository,
    private val rankingRepository: RankingRepository,
    private val snobGroupInviteRepository: SnobGroupInviteRepository,
) : SnobGroupStorage {
    override fun save(snobGroup: SnobGroup) = snobGroupRepository.save(SnobGroupEntity.toEntity(snobGroup)).toModel()
    override fun delete(snobGroup: SnobGroup) = snobGroupRepository.delete(SnobGroupEntity.toEntity(snobGroup))
    override fun findAllBySnobId(snobId: String) = snobGroupRepository.findAllBySnobId(snobId).map { it.toModel() }
    override fun findById(id: UUID) = snobGroupRepository.findById(id).getOrNull()?.toModel()

    override fun saveMember(snobGroupMember: SnobGroupMember) =
        snobGroupMemberRepository.save(SnobGroupMemberEntity.toEntity(snobGroupMember)).toModel()

    override fun findMembersByGroupId(groupId: UUID) =
        snobGroupMemberRepository.findByGroupId(groupId).map { it.toModel() }

    override fun saveRankingItem(rankingItem: RankingItem) {
        rankingItemRepository.save(RankingItemEntity.from(rankingItem))
    }

    override fun deleteRankingItem(rankingItem: RankingItem) {
        rankingItemRepository.delete(RankingItemEntity.from(rankingItem))
    }

    override fun findRankingItemById(id: UUID) = rankingItemRepository.findById(id).getOrNull()?.toModel()

    override fun findRankingItemsByGroup(groupId: UUID): List<RankingItem> =
        rankingItemRepository.findAllByGroupId(groupId).map { it.toModel() }

    override fun searchRankingItemsByGroup(groupId: UUID, keyword: String, page: Int, limit: Int, sort: RankingSortBy?, dir: RankingSortDirection?): RankingItemResults {
        val sortBy = when {
            sort != null && dir == RankingSortDirection.ASC -> Sort.by(sort.column).ascending()
            sort != null && dir == RankingSortDirection.DESC -> Sort.by(sort.column).descending()
            else -> Sort.by(RankingSortBy.AVERAGE_RANKING.column).descending()
        }
        val results = rankingItemRepository.searchByGroupIdAndKeyword(groupId, "%$keyword%", PageRequest.of(page, limit, sortBy))
        return RankingItemResults(
            total = results.totalElements,
            pages = results.totalPages,
            items = results.toList().map { it.toModel() },
        )
    }

    override fun saveRanking(ranking: Ranking) {
        rankingRepository.save(RankingEntity.from(ranking))
    }

    override fun deleteRanking(ranking: Ranking) {
        rankingRepository.delete(RankingEntity.from(ranking))
    }

    override fun findRankingById(id: UUID) = rankingRepository.findById(id).getOrNull()?.toModel()

    override fun findExistingRankingForMember(itemId: UUID, groupMemberId: UUID) =
        rankingRepository.findByItemIdAndGroupMemberId(itemId, groupMemberId)?.toModel()

    override fun findRankingsByItemId(itemId: UUID) = rankingRepository.findAllByItemId(itemId).map { it.toModel() }

    override fun saveGroupInvite(snobGroupInvite: SnobGroupInvite) {
        snobGroupInviteRepository.save(SnobGroupInviteEntity.from(snobGroupInvite))
    }

    override fun deleteGroupInvite(snobGroupInvite: SnobGroupInvite) {
        snobGroupInviteRepository.delete(SnobGroupInviteEntity.from(snobGroupInvite))
    }

    override fun findGroupInvites(groupId: UUID, email: String): List<SnobGroupInvite> =
        snobGroupInviteRepository.findAllByGroupIdAndEmail(groupId, email).map { it.toModel() }

    override fun findGroupInvites(groupId: UUID): List<SnobGroupInvite> =
        snobGroupInviteRepository.findAllByGroupId(groupId).map { it.toModel() }

    override fun findPendingGroupInvites(email: String) =
        snobGroupInviteRepository.findAllByEmailAndStatus(email, InviteStatus.PENDING).map { it.toModel() }

    override fun findGroupInvite(id: UUID) = snobGroupInviteRepository.findById(id).getOrNull()?.toModel()
}
