package gov.cdc.nccdphp.esurveillance.rest.security

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Component

@Component
class S2SAuth(val config: S2SAuthConfig) {
    companion object {
        val log: Log = LogFactory.getLog(S2SAuth::class.java)
    }

    @Throws( ServiceNotAuthorizedException::class)
        fun checkS2SCredentials(token: String?):Boolean {
            if (config.token != token) {
                log.error("Invalid token - Authentication Failed")
                throw ServiceNotAuthorizedException("Service not Authorized to call method!")
            }
            return true
        }
}