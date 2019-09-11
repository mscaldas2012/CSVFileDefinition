package gov.cdc.nccdphp.esurveillance.csvDefinition.service

import gov.cdc.nccdphp.esurveillance.csvDefinition.model.CSVFile
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.ValidationReport
import gov.cdc.nccdphp.esurveillance.rest.exceptions.InvalidDataException
import org.junit.Test

import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.io.File
import java.io.InputStream
import kotlin.system.measureTimeMillis

/**
 * @Created - 2019-03-17
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("LOCAL")
class ValidatorTest {
    companion object {
        const val WW_MDE = "DPRP"
        const val VERSION = "1.0"
    }
    @Autowired
    internal var transformer: TransformerService? = null
    @Autowired
    private val validationService: Validator? = null

    @Before
    fun init() {
        this.validationService!!.configure(WW_MDE, VERSION)
    }

    @Test
    @Throws(Exception::class)
    fun validate() {
        val content = parse()
         var report: ValidationReport? = null
        val time = measureTimeMillis {
             report = validationService!!.validate(content)
        }
        println("it took $time millisendos ")
        println("Found ${report!!.errors.size} errors!")
        println("report =\n $report")
    }


    @Throws(InvalidDataException::class)
    private fun parse(): CSVFile {
        val content = getContent("testFile.csv").reader().readText()
        //println("file = $file")
        return transformer!!.parseContentAsCSVFile( content)
    }

    fun getContent(fileName: String): InputStream {
        return File(fileName).inputStream()
    }

}