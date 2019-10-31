package gov.cdc.nccdphp.esurveillance.rest

import gov.cdc.nccdphp.esurveillance.rest.model.CDCLogEntry
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.apache.http.impl.client.HttpClients
import org.apache.http.conn.ssl.TrustSelfSignedStrategy
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.ssl.SSLContextBuilder


@Service
class CDCLoggerService(@Value("\${cdc_logging_url}") val cdcLoggingURL: String) {

    companion object {
        val logger: Log = LogFactory.getLog(CDCLoggerService::class.java)
    }
    lateinit var rt: RestTemplate
    //CDC uses self signed certificate... need to disable SSL verification...
    init {
        val socketFactory = SSLConnectionSocketFactory(SSLContextBuilder().loadTrustMaterial(null, TrustSelfSignedStrategy()).build())
        val httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build()
        rt = RestTemplate()
        (rt.getRequestFactory() as HttpComponentsClientHttpRequestFactory).httpClient = httpClient
    }

    fun sendError(logEntry: CDCLogEntry) {
        try {
            rt.postForEntity(cdcLoggingURL, logEntry, CDCLogEntry::class.java)
        } catch (e: Exception) {
            logger.error("Unable to Log error to CDC's Logging. Error: " + e.message)
        }
    }


}