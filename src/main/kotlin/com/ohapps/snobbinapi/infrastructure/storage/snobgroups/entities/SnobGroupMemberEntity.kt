package com.ohapps.snobbinapi.infrastructure.storage.snobgroups.entities

import com.ohapps.snobbinapi.domain.snobgroups.SnobGroupMember
import com.ohapps.snobbinapi.domain.snobgroups.SnobGroupMemberRole
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "snob_group_members")
data class SnobGroupMemberEntity(
    @Id
    val id: UUID,
    @Column
    val groupId: UUID,
    @Column
    val snobId: String,
    @Enumerated(EnumType.STRING)
    @Column
    val role: SnobGroupMemberRole,
) {
    companion object {
        fun toEntity(snobGroupMember: SnobGroupMember) = SnobGroupMemberEntity(
            id = snobGroupMember.id,
            groupId = snobGroupMember.groupId,
            snobId = snobGroupMember.snobId,
            role = snobGroupMember.role,
        )
    }

    fun toModel() = SnobGroupMember(
        id = this.id,
        groupId = this.groupId,
        snobId = this.snobId,
        role = this.role,
    )
}
