package com.mesmoray.lektora.userservice.configuration

import com.mesmoray.lektora.userservice.configuration.traceid.TraceIdInterceptor.Companion.TRACE_ID_MDC_KEY
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.util.ContentCachingResponseWrapper
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RequestResponseLoggingFilter : Filter {

    private val logger = LoggerFactory.getLogger(RequestResponseLoggingFilter::class.java)

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse
        val wrappingResponse = ContentCachingResponseWrapper(httpResponse)
        val startTime = System.currentTimeMillis()

        try {
            chain.doFilter(request, wrappingResponse)
        } finally {
            val responseTime = System.currentTimeMillis() - startTime
            logRequestAndResponse(httpRequest, wrappingResponse, responseTime)

            wrappingResponse.copyBodyToResponse()
        }
    }

    private fun logRequestAndResponse(
        request: HttpServletRequest,
        response: ContentCachingResponseWrapper,
        responseTime: Long
    ) {
        val traceId = MDC.get(TRACE_ID_MDC_KEY) ?: "No Trace ID"
        logger.info("Trace ID: {}, Request URI: {}, Response Status: {}, Response Time: {} ms", traceId, request.requestURI, response.status, responseTime)
    }
}
