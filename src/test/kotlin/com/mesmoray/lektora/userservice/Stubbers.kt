package com.mesmoray.lektora.userservice

import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import java.nio.charset.StandardCharsets

class Stubbers {

    companion object {

        fun loadFileFromResource(fileName: String): String {
            return ClassPathResource(fileName).inputStream.bufferedReader(StandardCharsets.UTF_8).readText()
        }

        fun stubCNN(webTestClient: WebTestClient, url: String) {
            stubFeed(webTestClient, url, "dnn.xml")
        }

        private fun stubFeed(webTestClient: WebTestClient, url: String, fileName: String) {
            val xmlContent = loadFileFromResource(fileName)

            webTestClient
                .get()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
                .exchange()
                .expectStatus().isOk
                .expectBody(String::class.java)
                .isEqualTo(xmlContent)
        }
    }
}
