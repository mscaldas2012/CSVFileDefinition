package gov.cdc.nccdphp.esurveillance.rest.model

import com.fasterxml.jackson.annotation.JsonPropertyOrder

val APP_NAME = "DPRP Data Submission Portal"
val SERVICE_NAME: String = "CSVFileDefinition"

@JsonPropertyOrder("appName", "source", "environment", "token", "url", "targetSite", "exceptionMessage", "stackTrace")
data class CDCLogEntry(val environment:String, val token: String, val url: String, val targetSite: String,
                       val exceptionMessage: String, val stackTrace: String) {
    val appName: String = APP_NAME
    val source: String = SERVICE_NAME
}
