package com.ohapps.snobbinapi.infrastructure.storage.snobgroups.entities

import com.ohapps.snobbinapi.domain.snobgroups.SnobGroupAttribute
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "snob_group_attributes")
data class SnobGroupAttributeEntity(
    @Id
    val id: UUID,
    @Column
    val name: String,
    @ManyToOne
    @JoinColumn(name = "group_id")
    val snobGroup: SnobGroupEntity,
) {
    companion object {
        fun toEntity(snobGroupEntity: SnobGroupEntity, snobGroupAttribute: SnobGroupAttribute) =
            SnobGroupAttributeEntity(
                id = snobGroupAttribute.id,
                name = snobGroupAttribute.name,
                snobGroup = snobGroupEntity,
            )
    }

    fun toModel() = SnobGroupAttribute(
        id = this.id,
        name = this.name,
    )
}
