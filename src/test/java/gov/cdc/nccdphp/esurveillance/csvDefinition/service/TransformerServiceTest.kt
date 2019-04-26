package gov.cdc.nccdphp.esurveillance.csvDefinition.service

import org.junit.Test

import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.io.File

/**
 * @Created - 2019-04-26
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class TransformerServiceTest {

    @Autowired
    lateinit var  transformerService:TransformerService

    @Test
    fun parseContentAsCSVFile() {
        val content = parseContent("testfile.csv")
        val filecontent = transformerService.parseContentAsCSVFile( content)
        println(filecontent)
    }

    @Test
    fun parseContentAsJSON() {
        val content = parseContent("testfile.csv")
        val json = transformerService.parseContentAsJson("DPRP", "1.0", content)
        println(json)
    }


    fun parseContent(fileName: String): String {
        val content = File(fileName).inputStream().reader().readText()
        return content
    }

}