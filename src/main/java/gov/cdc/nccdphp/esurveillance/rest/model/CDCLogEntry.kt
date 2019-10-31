package gov.cdc.nccdphp.esurveillance.rest.model

val APP_NAME: String = "CSVFileDefinition"

data class CDCLogEntry(val Token: String, val Url: String, val Source: String, val TargetSite: String,
                       val ExceptionMessage: String, val StackTrace: String,
                       val AppName: String = APP_NAME)
