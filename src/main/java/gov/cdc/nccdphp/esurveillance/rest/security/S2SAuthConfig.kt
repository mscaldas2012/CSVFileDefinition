package gov.cdc.nccdphp.esurveillance.rest.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "s2s-auth-config")
open class S2SAuthConfig {

  //  @Value("\${token}")
    lateinit var token: String
}