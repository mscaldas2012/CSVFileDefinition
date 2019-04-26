package gov.cdc.nccdphp.esurveillance.csvDefinition

import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 *
 *
 * @Created - 2019-04-01
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
class TestKotlinFeatures {

    @Test
    fun compareDates() {
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH)
        val d1 = LocalDate.parse("11/05/2018", formatter)
        val d2 = LocalDate.parse("11/05/2018", formatter)
        val d3 = LocalDate.parse("11/01/2018", formatter)
        val d4 = LocalDate.parse("11/08/2018", formatter)
        assert(d1 == d2)
        assert(d1 != d3)
        assert(d1 >= d3)
        assert(d1 <= d4)
    }

    @Test
    fun testINRegEx() {
        val listRegEx = "\\{( ?([0-9]+),? *)+\\}".toRegex()
        val example = "{17,   18,   19}"
        val found = listRegEx.find(example)
        //found?.groupValues?.forEach { e -> println(e) }

        val values = example.replace("{","").replace("}","").split(",")
        values.forEach { e -> println(e.trim()) }

        val foundE = values.find { x -> x.trim() == "20"}
        println(foundE)
    }

    @Test
    fun testEquality() {
        val zip1:Any = "99999"
        val zip2:Any = 99999
        println(zip1 == zip2 )
    }

    @Test
    fun testFold() {
        var folded = false
        val list = listOf("\$15", "<", "=" , "250", "AND", "\$A", "==", "20", "OR", "\$B", "<", "30", "OR", "C", ">", "=", "25").zipWithNext { previous, element ->
            if ((previous == "<" || previous == ">") && element == "=") {
                folded = true
                 "$previous$element"
            } else {
                if (folded) {
                    element
                } else {
                    previous
                }
            }
        }
        println(list)
    }
}