package com.mesmoray.lektora.userservice.adapter.web.input

import com.mesmoray.lektora.userservice.adapter.web.input.dto.UserProfile
import com.mesmoray.lektora.userservice.model.User
import com.mesmoray.lektora.userservice.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    fun createUser(@RequestBody userProfile: UserProfile): User {
        return userService.createUser(
            countryCodes = userProfile.countryCodes,
            languageCodes = userProfile.languageCodes
        )
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun updateUserProfile(@RequestBody userProfile: UserProfile): User {
        return userService.updateUserProfile(
            countryCodes = userProfile.countryCodes,
            languageCodes = userProfile.languageCodes
        )
    }

    @PostMapping("/profile/addCountry")
    @ResponseStatus(HttpStatus.OK)
    fun addUserCountry(@RequestParam countryCode: String) {
        userService.addUserCountry(countryCode)
    }

    @PostMapping("/profile/removeCountry")
    @ResponseStatus(HttpStatus.OK)
    fun removeUserCountry(@RequestParam countryCode: String) {
        userService.removeUserCountry(countryCode)
    }

    @PostMapping("/profile/addLanguage")
    @ResponseStatus(HttpStatus.OK)
    fun addUserLanguage(@RequestParam languageCode: String) {
        userService.addUserLanguage(languageCode)
    }

    @PostMapping("/profile/removeLanguage")
    @ResponseStatus(HttpStatus.OK)
    fun removeUserLanguage(@RequestParam languageCode: String) {
        userService.removeUserLanguage(languageCode)
    }
}
