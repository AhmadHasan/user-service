package com.mesmoray.lektora.userservice.adapter.controller

import com.mesmoray.lektora.userservice.configuration.interceptor.userid.UserIdInterceptor
import com.mesmoray.lektora.userservice.data.repository.UserRepository
import com.mesmoray.lektora.userservice.service.UserService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserProfileController::class)
class UserProfileControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var userIdInterceptor: UserIdInterceptor

    @MockBean
    private lateinit var userRepository: UserRepository

    @Test
    fun `createUser should return 200 on success`() {
        val userProfile = """{ "countryCodes": ["DE"], "languageCodes": ["de"] }"""

        mockMvc.perform(
            put("/user")
                .contentType("application/json")
                .content(userProfile)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `updateUserProfile should return 200 on success`() {
        val userProfile = """{ "countryCodes": ["DE"], "languageCodes": ["de"] }"""

        mockMvc.perform(
            post("/user")
                .contentType("application/json")
                .content(userProfile)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `addUserCountry should return 200 on success`() {
        mockMvc.perform(
            post("/user/profile/add-country")
                .param("countryCode", "DE")
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `removeUserCountry should return 200 on success`() {
        mockMvc.perform(
            post("/user/profile/remove-country")
                .param("countryCode", "DE")
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `setUserLanguages should return 200 on success`() {
        val userLanguages = """{ "languageCodes": ["de", "en"] }"""

        mockMvc.perform(
            put("/user/profile/languages")
                .contentType("application/json")
                .content(userLanguages)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `addUserLanguage should return 200 on success`() {
        mockMvc.perform(
            post("/user/profile/add-language")
                .param("languageCode", "de")
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `removeUserLanguage should return 200 on success`() {
        mockMvc.perform(
            post("/user/profile/remove-language")
                .param("languageCode", "de")
        )
            .andExpect(status().isOk)
    }
}
