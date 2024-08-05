package com.ohapps.snobbinapi.domain.snobs

import com.ohapps.snobbinapi.domain.common.AuthUser
import com.ohapps.snobbinapi.domain.common.SecurityService
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SnobServiceTest {
    private val securityService = mockk<SecurityService>()
    private val snobStorage = mockk<SnobStorage>(relaxed = true)
    private val snobService = SnobService(securityService, snobStorage)

    @Nested
    inner class GetCurrentSnob {

        @Test
        fun `test get current snob returns a new snob when not found`() {
            val authUser = AuthUser("123456", "test@ohapps.com")
            val expectedSnob = Snob.from(authUser)

            every { securityService.getAuthUser() } returns authUser
            every { snobStorage.findById(authUser.id) } returns null

            val snob = snobService.getCurrentSnob()

            assertThat(snob).isEqualTo(expectedSnob)
            verify { snobStorage.save(snob) }
        }

        @Test
        fun `test get current snob returns existing snob when found`() {
            val authUser = AuthUser("123456", "test@ohapps.com")
            val existingSnob = Snob.from(authUser)

            every { securityService.getAuthUser() } returns authUser
            every { snobStorage.findById(authUser.id) } returns existingSnob

            val snob = snobService.getCurrentSnob()

            assertThat(snob).isEqualTo(existingSnob)
            verify(exactly = 0) { snobStorage.save(snob) }
        }
    }

    @Nested
    inner class UpdateCurrentSnob {

        @Test
        fun `test that snob is updated correctly`() {
            val authUser = AuthUser("123456", "test@ohapps.com")
            val existingSnob = Snob.from(authUser)
            val snobUpdate = SnobUpdate(firstName = "first", lastName = "last")
            val expectedSnob = existingSnob.copy(
                firstName = snobUpdate.firstName,
                lastName = snobUpdate.lastName,
            )

            every { securityService.getAuthUser() } returns authUser
            every { snobStorage.findById(authUser.id) } returns existingSnob

            val updatedSnob = snobService.updateCurrentSnob(snobUpdate)

            assertThat(existingSnob).isNotEqualTo(updatedSnob)
            assertThat(updatedSnob).isEqualTo(expectedSnob)
            verify { snobStorage.save(expectedSnob) }
        }
    }
}
