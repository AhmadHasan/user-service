package com.mesmoray.lektora.userservice.service

import com.mesmoray.lektora.userservice.configuration.SessionManager
import com.mesmoray.lektora.userservice.data.repository.UserRepository
import com.mesmoray.lektora.userservice.model.Profile
import com.mesmoray.lektora.userservice.model.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.context.ApplicationEventPublisher
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @Mock
    private lateinit var sessionManager: SessionManager

    @Captor
    private lateinit var userCaptor: ArgumentCaptor<User>

    @InjectMocks
    private lateinit var userService: UserService

    private fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

    private val user = User(
        profile = Profile(
            countryCodes = listOf("US"),
            languageCodes = listOf("en")
        )
    )

    private val nonExistingUser = User(
        profile = Profile(
            countryCodes = listOf("US"),
            languageCodes = listOf("en")
        )
    )

    @BeforeEach
    fun setUp() {
    }

    @Test
    fun createUser() {
        whenever(userRepository.save(any<User>())).thenReturn(user)
        whenever(applicationEventPublisher.publishEvent(any<UserCreatedAEvent>())).then { }

        userService.createUser(user.profile.countryCodes, user.profile.languageCodes, user.profile.publisherNames)

        verify(userRepository).save(any<User>())
        verify(applicationEventPublisher).publishEvent(any<UserCreatedAEvent>())
    }

    @Test
    fun `updateUserProfile throws IllegalArgumentException when userId not set`() {
        assertThrows(IllegalArgumentException::class.java) {
            userService.updateUserProfile(user.profile.countryCodes, user.profile.languageCodes, user.profile.publisherNames)
        }
    }

    @Test
    fun `updateUserProfile throws NoSuchElementException when user not found`() {
        whenever(sessionManager.getUserId()).thenReturn(nonExistingUser.id)
        assertThrows(NoSuchElementException::class.java) {
            userService.updateUserProfile(user.profile.countryCodes, user.profile.languageCodes, user.profile.publisherNames)
        }
    }

    @Test
    fun `updateUserProfile should update user profile`() {
        whenever(sessionManager.getUserId()).thenReturn(user.id)
        whenever(userRepository.save(any<User>())).thenReturn(user)
        whenever(userRepository.findById(user.id)).thenReturn(Optional.of(user))
        whenever(applicationEventPublisher.publishEvent(any<UserUpdatedAEvent>())).then { }
        userService.updateUserProfile(
            user.profile.countryCodes + "DE",
            user.profile.languageCodes + "de",
            user.profile.publisherNames + "Test Publisher"
        )
        verify(userRepository).save(capture(userCaptor))
        val user = userCaptor.value
        assertThat(user.profile.countryCodes).contains("DE")
        assertThat(user.profile.languageCodes).contains("de")
    }

    @Test
    fun `addUserCountry throws IllegalArgumentException when userId not set`() {
        assertThrows(IllegalArgumentException::class.java) {
            userService.addUserCountry("DE")
        }
    }

    @Test
    fun `addUserCountry throws NoSuchElementException when user not found`() {
        whenever(sessionManager.getUserId()).thenReturn(nonExistingUser.id)
        assertThrows(NoSuchElementException::class.java) {
            userService.addUserCountry("DE")
        }
    }

    @Test
    fun addUserCountry() {
        whenever(sessionManager.getUserId()).thenReturn(user.id)
        whenever(userRepository.save(any<User>())).thenReturn(user)
        whenever(userRepository.findById(user.id)).thenReturn(Optional.of(user))
        whenever(applicationEventPublisher.publishEvent(any<UserUpdatedAEvent>())).then { }
        userService.addUserCountry("DE")
        verify(userRepository).save(capture(userCaptor))
        val user = userCaptor.value
        assertThat(user.profile.countryCodes).contains("DE")
    }

    @Test
    fun `removeUserCountry throws IllegalArgumentException when userId not set`() {
        assertThrows(IllegalArgumentException::class.java) {
            userService.removeUserCountry("DE")
        }
    }

    @Test
    fun `removeUserCountry throws NoSuchElementException when user not found`() {
        whenever(sessionManager.getUserId()).thenReturn(nonExistingUser.id)
        assertThrows(NoSuchElementException::class.java) {
            userService.removeUserCountry("DE")
        }
    }

    @Test
    fun `removeUserCountry throws IllegalArgumentException when country is not in user profile`() {
        whenever(sessionManager.getUserId()).thenReturn(user.id)
        whenever(userRepository.findById(user.id)).thenReturn(Optional.of(user))
        assertThrows(IllegalArgumentException::class.java) {
            userService.removeUserCountry("DE")
        }
    }

    @Test
    fun removeUserCountry() {
        whenever(sessionManager.getUserId()).thenReturn(user.id)
        whenever(userRepository.save(any<User>())).thenReturn(user)
        whenever(userRepository.findById(user.id)).thenReturn(Optional.of(user))
        whenever(applicationEventPublisher.publishEvent(any<UserUpdatedAEvent>())).then { }
        userService.removeUserCountry("US")
        verify(userRepository).save(capture(userCaptor))
        val user = userCaptor.value
        assertThat(user.profile.countryCodes).doesNotContain("DE")
    }

    @Test
    fun `addUserLanguage throws IllegalArgumentException when userId not set`() {
        assertThrows(IllegalArgumentException::class.java) {
            userService.addUserLanguage("de")
        }
    }

    @Test
    fun `addUserLanguage throws NoSuchElementException when user not found`() {
        whenever(sessionManager.getUserId()).thenReturn(nonExistingUser.id)
        assertThrows(NoSuchElementException::class.java) {
            userService.addUserLanguage("de")
        }
    }

    @Test
    fun addUserLanguage() {
        whenever(sessionManager.getUserId()).thenReturn(user.id)
        whenever(userRepository.save(any<User>())).thenReturn(user)
        whenever(userRepository.findById(user.id)).thenReturn(Optional.of(user))
        whenever(applicationEventPublisher.publishEvent(any<UserUpdatedAEvent>())).then { }
        userService.addUserLanguage("de")
        verify(userRepository).save(capture(userCaptor))
        val user = userCaptor.value
        assertThat(user.profile.languageCodes).contains("de")
    }

    @Test
    fun `removeUserLanguage throws IllegalArgumentException when userId not set`() {
        assertThrows(IllegalArgumentException::class.java) {
            userService.removeUserLanguage("de")
        }
    }

    @Test
    fun `removeUserLanguage throws NoSuchElementException when user not found`() {
        whenever(sessionManager.getUserId()).thenReturn(nonExistingUser.id)
        assertThrows(NoSuchElementException::class.java) {
            userService.removeUserLanguage("de")
        }
    }

    @Test
    fun `removeUserLanguage throws IllegalArgumentException when language is not in user profile`() {
        whenever(sessionManager.getUserId()).thenReturn(user.id)
        whenever(userRepository.findById(user.id)).thenReturn(Optional.of(user))
        assertThrows(IllegalArgumentException::class.java) {
            userService.removeUserLanguage("de")
        }
    }

    @Test
    fun removeUserLanguage() {
        whenever(sessionManager.getUserId()).thenReturn(user.id)
        whenever(userRepository.save(any<User>())).thenReturn(user)
        whenever(userRepository.findById(user.id)).thenReturn(Optional.of(user))
        whenever(applicationEventPublisher.publishEvent(any<UserUpdatedAEvent>())).then { }
        userService.removeUserLanguage("en")
        verify(userRepository).save(capture(userCaptor))
        val user = userCaptor.value
        assertThat(user.profile.languageCodes).doesNotContain("en")
    }
}
