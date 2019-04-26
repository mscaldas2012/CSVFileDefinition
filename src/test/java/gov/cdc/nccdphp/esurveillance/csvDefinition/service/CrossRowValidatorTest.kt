package gov.cdc.nccdphp.esurveillance.csvDefinition.service

import gov.cdc.nccdphp.esurveillance.csvDefinition.model.CSVFile
import gov.cdc.nccdphp.esurveillance.rest.exceptions.InvalidDataException
import org.junit.Test

import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.io.File
import java.io.InputStream

/**
 * @Created - 2019-04-10
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class CrossRowValidatorTest {
    val WW_MDE = "DPRP"
    val VERSION = "1.0"

    @Autowired
    internal var transformer: TransformerService? = null
    @Autowired
    //private val validationService: ValidationService? = null
    private val rowValidator: CrossRowValidator? = null

    @Before
    fun init() {
        this.rowValidator!!.configure(WW_MDE, VERSION)
    }

    @Test
    @Throws(Exception::class)
    fun validate() {
        val content = parse()
        val report = rowValidator!!.validate(content)
        println("report =\n $report")
    }


    @Throws(InvalidDataException::class)
    private fun parse(): CSVFile {
        val content = getContent("testFile.mde").reader().readText()
        val file = transformer!!.parseContentAsCSVFile(content)
        //println("file = $file")
        return file
    }
    fun getContent(fileName: String): InputStream {
        return File(fileName).inputStream()
    }
}