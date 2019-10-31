package gov.cdc.nccdphp.esurveillance.rest

import gov.cdc.nccdphp.esurveillance.rest.model.CDCLogEntry
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.impl.client.HttpClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.security.cert.X509Certificate


@Service
class CDCLoggerService(@Value("\${cdc_logging_url}") val cdcLoggingURL: String) {

    companion object {
        val logger: Log = LogFactory.getLog(CDCLoggerService::class.java)
    }
    val rt: RestTemplate //= RestTemplate()
    //CDC uses self signed certificate... need to disable SSL verification...
    init {
        val acceptingTrustStrategy = { chain: Array<X509Certificate>, authType: String -> true }

        val sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build()

        val csf = SSLConnectionSocketFactory(sslContext)

        val httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build()

        val requestFactory = HttpComponentsClientHttpRequestFactory()
        requestFactory.httpClient = httpClient
        rt = RestTemplate(requestFactory)

    }

    fun sendError(logEntry: CDCLogEntry) {
        try {
            rt.postForEntity(cdcLoggingURL, logEntry, CDCLogEntry::class.java)
        } catch (e: Exception) {
            logger.error("Unable to Log error to CDC's Logging. Error: " + e.message)
        }
    }


}