package com.ohapps.snobbinapi.infrastructure.config

import com.ohapps.snobbinapi.domain.common.SecurityService
import com.ohapps.snobbinapi.domain.snobgroups.SnobGroupService
import com.ohapps.snobbinapi.domain.snobgroups.SnobGroupStorage
import com.ohapps.snobbinapi.domain.snobs.SnobService
import com.ohapps.snobbinapi.domain.snobs.SnobStorage
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AutowireConfig {

    @Bean
    fun snobService(securityService: SecurityService, snobStorage: SnobStorage) =
        SnobService(securityService, snobStorage)

    @Bean
    fun snobGroupService(securityService: SecurityService, snobGroupStorage: SnobGroupStorage) =
        SnobGroupService(securityService, snobGroupStorage)
}
