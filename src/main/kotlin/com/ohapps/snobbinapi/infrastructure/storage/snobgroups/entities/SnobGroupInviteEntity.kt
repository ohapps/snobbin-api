package com.ohapps.snobbinapi.infrastructure.storage.snobgroups.entities

import com.ohapps.snobbinapi.domain.snobgroups.InviteStatus
import com.ohapps.snobbinapi.domain.snobgroups.SnobGroupInvite
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "snob_group_invites")
data class SnobGroupInviteEntity(
    @Id
    val id: UUID,
    @Column
    val groupId: UUID,
    @Column
    val email: String,
    @Enumerated(EnumType.STRING)
    @Column
    val status: InviteStatus,
) {
    companion object {
        fun from(snobGroupInvite: SnobGroupInvite) = SnobGroupInviteEntity(
            id = snobGroupInvite.id,
            groupId = snobGroupInvite.groupId,
            email = snobGroupInvite.email,
            status = snobGroupInvite.status,
        )
    }

    fun toModel() = SnobGroupInvite(
        id = this.id,
        groupId = this.groupId,
        email = this.email,
        status = this.status,
    )
}
