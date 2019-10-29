package gov.cdc.nccdphp.esurveillance.rest.model

val APP_NAME: String = "CSVFileDefinition"

data class CDCLogEntry(val token: String, val url: String, val source: String, val targetSite: String,
                       val exceptionMessage: String, val stackTrace: String,
                       val appName: String = APP_NAME)
