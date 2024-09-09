package com.mesmoray.lektora.userservice.service

import com.mesmoray.lektora.userservice.configuration.SessionManager
import com.mesmoray.lektora.userservice.data.repository.UserRepository
import com.mesmoray.lektora.userservice.model.Profile
import com.mesmoray.lektora.userservice.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @Autowired
    private lateinit var sessionManager: SessionManager

    @Autowired
    private lateinit var userRepository: UserRepository

    fun createUser(countryCodes: List<String>, languageCodes: List<String>): User {
        val user = User(
            profile = Profile(
                countryCodes = countryCodes,
                languageCodes = languageCodes
            )
        )

        val storedUser = userRepository.save(user)
        applicationEventPublisher.publishEvent(UserCreatedAEvent(storedUser))

        return storedUser
    }

    fun updateUserProfile(countryCodes: List<String>, languageCodes: List<String>): User {
        val user = getUser()
        val updatedUser = user.copy(profile = Profile(countryCodes, languageCodes))
        return updateUser(updatedUser)
    }

    fun addUserCountry(countryCode: String): User {
        val user = getUser()
        if (user.profile.countryCodes.contains(countryCode)) throw IllegalArgumentException("Country $countryCode is already part of user profile (userId = ${user.id})")
        val updatedUser = user.copy(profile = user.profile.copy(countryCodes = user.profile.countryCodes + countryCode))
        return updateUser(updatedUser)
    }

    fun removeUserCountry(countryCode: String): User {
        val user = getUser()
        if (!user.profile.countryCodes.contains(countryCode)) throw IllegalArgumentException("Country $countryCode is not part of user profile (userId = ${user.id})")
        val newCountryCodes = user.profile.countryCodes.filter { it != countryCode }
        val updatedUser = user.copy(profile = user.profile.copy(countryCodes = newCountryCodes))
        return updateUser(updatedUser)
    }

    fun addUserLanguage(languageCode: String): User {
        val user = getUser()
        if (user.profile.languageCodes.contains(languageCode)) throw IllegalArgumentException("Language $languageCode is already part of user profile (userId = ${user.id})")
        val updatedUser = user.copy(profile = user.profile.copy(languageCodes = user.profile.languageCodes + languageCode))
        return updateUser(updatedUser)
    }

    fun removeUserLanguage(languageCode: String): User {
        val user = getUser()
        if (!user.profile.languageCodes.contains(languageCode)) throw IllegalArgumentException("Language $languageCode is not part of user profile (userId = ${user.id})")
        val newLanguageCodes = user.profile.languageCodes.filter { it != languageCode }
        val updatedUser = user.copy(profile = user.profile.copy(languageCodes = newLanguageCodes))
        return updateUser(updatedUser)
    }

    private fun getUser(): User {
        val userId = sessionManager.getUserId() ?: throw IllegalArgumentException("No UserID header provided")
        val userOptional = userRepository.findById(userId)
        if (userOptional.isEmpty) {
            throw NoSuchElementException("User not found")
        }
        return userOptional.get()
    }

    private fun updateUser(user: User): User {
        val storedUser = userRepository.save(user)
        applicationEventPublisher.publishEvent(UserUpdatedAEvent(storedUser))
        return storedUser
    }
}
