package com.ohapps.snobbinapi.infrastructure.storage.snobgroups.entities

import com.ohapps.snobbinapi.domain.snobgroups.SnobGroup
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import java.util.*

@Entity
@Table(name = "snob_groups")
@SQLDelete(sql = "UPDATE snob_groups SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
data class SnobGroupEntity(
    @Id
    val id: UUID,
    @Column
    val name: String,
    @Column
    val description: String,
    @Column
    val minRanking: Int,
    @Column
    val maxRanking: Int,
    @Column
    val increments: Float,
    @Column
    val rankIcon: String,
    @Column
    val rankingsRequired: Int,
    @OneToMany(mappedBy = "snobGroup", cascade = [CascadeType.ALL], orphanRemoval = true)
    val attributes: List<SnobGroupAttributeEntity>,
) {
    companion object {
        fun toEntity(snobGroup: SnobGroup): SnobGroupEntity {
            val snobGroupEntity = SnobGroupEntity(
                id = snobGroup.id,
                name = snobGroup.name,
                description = snobGroup.description,
                minRanking = snobGroup.minRanking,
                maxRanking = snobGroup.maxRanking,
                increments = snobGroup.increments,
                rankIcon = snobGroup.rankIcon,
                rankingsRequired = snobGroup.rankingsRequired,
                attributes = emptyList(),
            )
            return snobGroupEntity.copy(
                attributes = snobGroup.attributes.map { SnobGroupAttributeEntity.toEntity(snobGroupEntity, it) },
            )
        }
    }

    fun toModel() = SnobGroup(
        id = this.id,
        name = this.name,
        description = this.description,
        minRanking = this.minRanking,
        maxRanking = this.maxRanking,
        increments = this.increments,
        rankIcon = this.rankIcon,
        rankingsRequired = this.rankingsRequired,
        attributes = this.attributes.map { it.toModel() },
    )
}
