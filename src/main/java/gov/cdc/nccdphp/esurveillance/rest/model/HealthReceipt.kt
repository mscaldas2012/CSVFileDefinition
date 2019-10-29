package gov.cdc.nccdphp.esurveillance.rest.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


enum class HEALTH_STATUS {
    OK, DOWN, UNHEALTHY
}

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder( "timestamp", "status", "dbHealth", "dbErrorMessage")
data class HealthReceipt(val status: HEALTH_STATUS, val dbHealth: HEALTH_STATUS) {
    val timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT)
    var dbErrorMessage: String? = null;
}