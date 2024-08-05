package com.ohapps.snobbinapi.application.controllers

import com.ohapps.snobbinapi.domain.snobgroups.SnobGroupMember
import com.ohapps.snobbinapi.domain.snobs.SnobService
import com.ohapps.snobbinapi.domain.snobs.SnobUpdate
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class SnobController(val snobService: SnobService) {
    @QueryMapping
    fun userInfo() = snobService.getCurrentSnob()

    @MutationMapping
    fun updateSnobInfo(@Argument snobUpdate: SnobUpdate) = snobService.updateCurrentSnob(snobUpdate)

    @SchemaMapping(typeName = "GroupMember", field = "snob")
    fun groupMembers(groupMember: SnobGroupMember) = snobService.getSnob(groupMember.snobId)
}
