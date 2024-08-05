package com.ohapps.snobbinapi.infrastructure.storage.snobs.entities

import com.ohapps.snobbinapi.domain.snobs.Snob
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "snobs")
data class SnobEntity(
    @Id
    val id: String,
    @Column
    val email: String,
    @Column
    val firstName: String? = "",
    @Column
    val lastName: String? = "",
) {
    companion object {
        fun toEntity(snob: Snob) = SnobEntity(
            id = snob.id,
            email = snob.email,
            firstName = snob.firstName,
            lastName = snob.lastName,
        )
    }

    fun toModel() = Snob(id, email, firstName, lastName)
}
