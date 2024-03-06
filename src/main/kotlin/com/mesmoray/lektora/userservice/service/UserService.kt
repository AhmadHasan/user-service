package com.mesmoray.lektora.userservice.service

import com.mesmoray.lektora.userservice.data.repository.UserRepository
import com.mesmoray.lektora.userservice.model.Profile
import com.mesmoray.lektora.userservice.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    private lateinit var userRepository: UserRepository
    fun createUser(countryCodes: List<String>, languageCodes: List<String>): User {
        val user = User(
            profile = Profile(
                countryCodes = countryCodes,
                languageCodes = languageCodes
            )
        )

        userRepository.save(user)
        return user
    }
}
