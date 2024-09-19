package com.mesmoray.lektora.userservice.service

import com.mesmoray.lektora.userservice.configuration.SessionManager
import com.mesmoray.lektora.userservice.configuration.interceptor.userid.UserIdInterceptor.Companion.USER_ID_HEADER
import com.mesmoray.lektora.userservice.data.repository.UserRepository
import com.mesmoray.lektora.userservice.model.Profile
import com.mesmoray.lektora.userservice.model.User
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var sessionManager: SessionManager

    @Autowired
    private lateinit var userRepository: UserRepository

    fun createUser(countryCodes: List<String>, languageCodes: List<String>, publisherNames: List<String>): User {
        val user = User(
            profile = Profile(
                countryCodes = countryCodes,
                languageCodes = languageCodes,
                publisherNames = publisherNames
            )
        )

        val storedUser = userRepository.save(user)
        applicationEventPublisher.publishEvent(UserCreatedAEvent(storedUser))

        return storedUser
    }

    fun getUser(): User {
        return findUser()
    }

    fun updateUserProfile(countryCodes: List<String>, languageCodes: List<String>, publisherNames: List<String>): User {
        val user = findUser()
        val updatedUser = user.copy(profile = Profile(countryCodes, languageCodes, publisherNames))
        return updateUser(updatedUser)
    }

    fun addUserCountry(countryCode: String): User {
        val user = findUser()
        if (user.profile.countryCodes.contains(countryCode)) throw IllegalArgumentException("Country $countryCode is already part of user profile (userId = ${user.id})")
        val updatedUser = user.copy(profile = user.profile.copy(countryCodes = user.profile.countryCodes + countryCode))
        return updateUser(updatedUser)
    }

    fun removeUserCountry(countryCode: String): User {
        val user = findUser()
        if (!user.profile.countryCodes.contains(countryCode)) throw IllegalArgumentException("Country $countryCode is not part of user profile (userId = ${user.id})")
        val newCountryCodes = user.profile.countryCodes.filter { it != countryCode }
        val updatedUser = user.copy(profile = user.profile.copy(countryCodes = newCountryCodes))
        return updateUser(updatedUser)
    }

    fun setUserCountries(countries: List<String>): User {
        val user = findUser()
        if (user.profile.countryCodes.toSet() == countries.toSet()) {
            logger.warn("Countries update request contains the same list of countries for user with userId = ${user.id}")
            return user
        }
        val updatedUser = user.copy(profile = user.profile.copy(countryCodes = countries))
        return updateUser(updatedUser)
    }

    fun addUserLanguage(languageCode: String): User {
        val user = findUser()
        if (user.profile.languageCodes.contains(languageCode)) throw IllegalArgumentException("Language $languageCode is already part of user profile (userId = ${user.id})")
        val updatedUser = user.copy(profile = user.profile.copy(languageCodes = user.profile.languageCodes + languageCode))
        return updateUser(updatedUser)
    }

    fun removeUserLanguage(languageCode: String): User {
        val user = findUser()
        if (!user.profile.languageCodes.contains(languageCode)) throw IllegalArgumentException("Language $languageCode is not part of user profile (userId = ${user.id})")
        val newLanguageCodes = user.profile.languageCodes.filter { it != languageCode }
        val updatedUser = user.copy(profile = user.profile.copy(languageCodes = newLanguageCodes))
        return updateUser(updatedUser)
    }

    fun setUserLanguages(languages: List<String>): User {
        val user = findUser()
        if (user.profile.languageCodes.toSet() == languages.toSet()) {
            logger.warn("Languages update request contains the same list of languages for user with userId = ${user.id}")
            return user
        }
        val updatedUser = user.copy(profile = user.profile.copy(languageCodes = languages))
        return updateUser(updatedUser)
    }

    fun addUserPublisher(publisherName: String): User {
        val user = findUser()
        if (user.profile.publisherNames.contains(publisherName)) throw IllegalArgumentException("Publisher $publisherName is already part of user profile (userId = ${user.id})")
        val updatedUser = user.copy(profile = user.profile.copy(publisherNames = user.profile.publisherNames + publisherName))
        return updateUser(updatedUser)
    }

    fun removeUserPublisher(publisherName: String): User {
        val user = findUser()
        if (!user.profile.publisherNames.contains(publisherName)) throw IllegalArgumentException("Publisher $publisherName is not part of user profile (userId = ${user.id})")
        val newPublishers = user.profile.publisherNames.filter { it != publisherName }
        val updatedUser = user.copy(profile = user.profile.copy(publisherNames = newPublishers))
        return updateUser(updatedUser)
    }

    fun setUserPublishers(publishers: List<String>): User {
        val user = findUser()
        if (user.profile.publisherNames.toSet() == publishers.toSet()) {
            logger.warn("Publishers update request contains the same list of publishers for user with userId = ${user.id}")
            return user
        }
        val updatedUser = user.copy(profile = user.profile.copy(publisherNames = publishers))
        return updateUser(updatedUser)
    }

    private fun findUser(): User {
        val userId = sessionManager.getUserId() ?: throw IllegalArgumentException("No $USER_ID_HEADER header provided")
        val userOptional = userRepository.findById(userId)
        if (userOptional.isEmpty) {
            throw NoSuchElementException("User with id = $userId not found")
        }
        return userOptional.get()
    }

    private fun updateUser(user: User): User {
        val storedUser = userRepository.save(user)
        applicationEventPublisher.publishEvent(UserUpdatedAEvent(storedUser))
        return storedUser
    }
}
