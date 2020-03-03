package gov.cdc.nccdphp.esurveillance.csvDefinition.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.CSVDefinition
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.io.File


@RunWith(SpringRunner::class)
@SpringBootTest
class SummarizerServiceTest {

    @Autowired
    lateinit var service: SummarizerService

    lateinit var config: CSVDefinition
    @Before
    fun loadDefinition() {
        val content = SummarizerServiceTest::class.java.getResource("/DPRP_1.0.json").readText()
        val gson = Gson()
         config =gson.fromJson(content, CSVDefinition::class.java)

    }

    @Test
    fun summarize() {
        val content= SummarizerServiceTest::class.java.getResource("/testFile.txt").readText()
        val summary  =  service.summarize(config, content, 1 )
        println(summary);
        val mapper = ObjectMapper()
        mapper.writeValue( File("target/summary.json"), summary);
    }
}