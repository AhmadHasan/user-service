package com.mesmoray.lektora.userservice.configuration

import com.mesmoray.lektora.userservice.configuration.traceid.TraceIdInterceptor.Companion.TRACE_ID_MDC_KEY
import com.mesmoray.lektora.userservice.configuration.useragent.UserAgentInterceptor.Companion.USER_AGENT_MDC_KEY
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
    companion object{
        private const val MAX_RESPONSE_SIZE = 1024
    }

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
        val userAgent = MDC.get(USER_AGENT_MDC_KEY) ?: "No User Agent Set"
        val responseLog = getResponseLog(response)
        logger.info(
            "Trace ID: {}, User Agent: {}, Request URI: {}, Response Status: {}, Response Time: {} ms, response(1024): {}",
            traceId,
            userAgent,
            request.requestURI,
            response.status,
            responseTime,
            responseLog
        )
    }

    private fun getResponseLog(response: ContentCachingResponseWrapper): Any {
        val responseString = response.contentAsByteArray.toString(Charsets.UTF_8)
        var log = responseString.take(1024)
        if(log.length < responseString.length)
            log = "$log[$MAX_RESPONSE_SIZE of ${responseString.length} chars]"
        return log
    }
}

