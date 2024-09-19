package com.mesmoray.lektora.userservice.adapter.controller

import com.mesmoray.lektora.userservice.adapter.controller.dto.CountriesRequestDTO
import com.mesmoray.lektora.userservice.adapter.controller.dto.LanguagesRequestDTO
import com.mesmoray.lektora.userservice.adapter.controller.dto.PublisherRequestDTO
import com.mesmoray.lektora.userservice.adapter.controller.dto.PublishersRequestDTO
import com.mesmoray.lektora.userservice.adapter.controller.dto.UserProfileDTO
import com.mesmoray.lektora.userservice.model.User
import com.mesmoray.lektora.userservice.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserProfileController {

    @Autowired
    private lateinit var userService: UserService

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    fun createUser(@RequestBody userProfileDTO: UserProfileDTO): User {
        return userService.createUser(
            countryCodes = userProfileDTO.countryCodes,
            languageCodes = userProfileDTO.languageCodes,
            publisherNames = userProfileDTO.publisherNames
        )
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getUser(): User {
        return userService.getUser()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun updateUserProfile(@RequestBody userProfileDTO: UserProfileDTO): User {
        return userService.updateUserProfile(
            countryCodes = userProfileDTO.countryCodes,
            languageCodes = userProfileDTO.languageCodes,
            publisherNames = userProfileDTO.publisherNames
        )
    }

    @PostMapping("/profile/add-country")
    @ResponseStatus(HttpStatus.OK)
    fun addUserCountry(@RequestParam countryCode: String) {
        userService.addUserCountry(countryCode)
    }

    @PostMapping("/profile/remove-country")
    @ResponseStatus(HttpStatus.OK)
    fun removeUserCountry(@RequestParam countryCode: String) {
        userService.removeUserCountry(countryCode)
    }

    @PostMapping("/profile/set-countries")
    @ResponseStatus(HttpStatus.OK)
    fun setUserCountries(@RequestBody countries: CountriesRequestDTO) {
        userService.setUserCountries(countries.countryCodes)
    }

    @PostMapping("/profile/add-language")
    @ResponseStatus(HttpStatus.OK)
    fun addUserLanguage(@RequestParam languageCode: String) {
        userService.addUserLanguage(languageCode)
    }

    @PostMapping("/profile/remove-language")
    @ResponseStatus(HttpStatus.OK)
    fun removeUserLanguage(@RequestParam languageCode: String) {
        userService.removeUserLanguage(languageCode)
    }

    @PostMapping("/profile/set-languages")
    @ResponseStatus(HttpStatus.OK)
    fun setUserLanguages(@RequestBody request: LanguagesRequestDTO) {
        userService.setUserLanguages(request.languageCodes)
    }

    @PostMapping("/profile/add-publisher")
    @ResponseStatus(HttpStatus.OK)
    fun addUserPublisher(@RequestBody request: PublisherRequestDTO) {
        userService.addUserPublisher(request.publisherName)
    }

    @PostMapping("/profile/remove-publisher")
    @ResponseStatus(HttpStatus.OK)
    fun removeUserPublisher(@RequestBody request: PublisherRequestDTO) {
        userService.removeUserPublisher(request.publisherName)
    }

    @PostMapping("/profile/set-publishers")
    @ResponseStatus(HttpStatus.OK)
    fun serUserPublishers(@RequestBody publishers: PublishersRequestDTO) {
        userService.setUserPublishers(publishers.publisherNames)
    }
}
