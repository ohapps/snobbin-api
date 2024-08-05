package com.ohapps.snobbinapi.domain.snobs

interface SnobStorage {
    fun findById(id: String): Snob?
    fun save(snob: Snob)
}