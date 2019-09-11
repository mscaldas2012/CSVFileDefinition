package gov.cdc.nccdphp.esurveillance.csvDefinition.validation

import org.junit.Test
import java.lang.IllegalArgumentException
import java.text.ParseException
import java.text.SimpleDateFormat

/**
 *
 *
 * @Created - 2019-08-14
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
class TestDateParsing {


    private fun parse(format: String, date: String) {
        val df = SimpleDateFormat(format)
        df.isLenient = false
        val date = df.parse(date)
        println("date = ${date}")
    }

    @Test
    fun testParseValidDate() {
       parse("yyyyMMdd", "20190813")
           assert(true)
    }

    @Test
    fun testparseInvalidCharactersDate() {
        try {
            parse("yyyyMMdd", "abcdefg")
            assert(false) // invalid date altogeter...
        } catch (e: ParseException) {
            assert(true)
        }
    }

    @Test
    fun testParseInvalidDateRange() {
        try {
            parse("yyyyMMdd", "20190230")
            assert(false) // February does not have 30 days,  using non-lenient parsing should throw an error.
        } catch (e: ParseException) {
            assert(true)
        }

    }

    @Test
    fun testParseInvalidFormat() {
        try {
            parse("yabcd", "20190813")
            assert(false) //The format is invalid and should throw a IllegalArgumentException,.
        } catch (e: IllegalArgumentException) {
            println("e.message = ${e.message}")
            assert(true)
        }
    }

    val booleanKeywords = arrayOf("true", "false", "1", "0", "Y", "N")
    @Test
    fun testBoolean() {
        var match = booleanKeywords.filter { "True".contains(it, ignoreCase = true) }
        println("match = ${match}")
        assert(match.isNotEmpty())
        match =  booleanKeywords.filter { "Verdate".contains(it, ignoreCase = true) }
        println("match = ${match}")
        assert(match.isEmpty())
    }


}