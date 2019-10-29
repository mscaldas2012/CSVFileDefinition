package gov.cdc.nccdphp.esurveillance.rest.model

import org.junit.Test

class CDCLoggingTest {

    @Test
    fun testCreateObject() {
        val logEntry = CDCLogEntry("TK_CSVFD_01", "https://localhost:8080/fileDef/v1/test", "Test", "testCreateObj", "error", "lots of errors")
        println("logEntry = ${logEntry}")

    }
}