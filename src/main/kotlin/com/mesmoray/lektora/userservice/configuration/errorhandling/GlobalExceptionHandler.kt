package com.mesmoray.lektora.userservice.configuration.errorhandling

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(
        exception: NoSuchElementException,
        request: WebRequest
    ): ResponseEntity<Any> {
        val bodyOfResponse = exception.message ?: "The requested resource was not found"
        return handleExceptionInternal(
            exception,
            bodyOfResponse,
            HttpHeaders(),
            HttpStatus.NOT_FOUND,
            request
        )
    }

    @ExceptionHandler(value = [IllegalArgumentException::class])
    fun handleIllegalArgumentException(ex: IllegalArgumentException, request: WebRequest): ResponseEntity<Any> {
        val body = mapOf("message" to (ex.message ?: "Invalid argument"))
        return ResponseEntity(body, HttpHeaders(), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [HttpClientErrorException::class])
    fun handleHttpClientErrorException(ex: HttpClientErrorException, request: WebRequest): ResponseEntity<Any> {
        val body = mapOf("message" to (ex.message ?: "Invalid argument"))
        return ResponseEntity(body, HttpHeaders(), ex.statusCode)
    }

    @ExceptionHandler(value = [(DataIntegrityViolationException::class)])
    protected fun handleConflict(ex: DataIntegrityViolationException, request: WebRequest): ResponseEntity<Any> {
        val bodyOfResponse = "Data integrity violation: ${ex.message}"
        return ResponseEntity(bodyOfResponse, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(value = [(Exception::class)])
    fun handleGenericException(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        val bodyOfResponse = "An error occurred. ${ex.message}"
        logger.error("An error occurred: ${ex.message}", ex)
        return ResponseEntity(bodyOfResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
