package gov.cdc.nccdphp.esurveillance.csvDefinition.controller

import gov.cdc.nccdphp.esurveillance.csvDefinition.model.CSVFile
import gov.cdc.nccdphp.esurveillance.csvDefinition.service.CSVDefinitionService
import gov.cdc.nccdphp.esurveillance.csvDefinition.service.TransformerService
import gov.cdc.nccdphp.esurveillance.rest.exceptions.InvalidDataException
import gov.cdc.nccdphp.esurveillance.rest.model.ERROR_CODES
import gov.cdc.nccdphp.esurveillance.rest.model.ErrorReceipt
import gov.cdc.nccdphp.esurveillance.validation.model.FileDefinition
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 *
 *
 * @Created - 2019-04-24
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@RestController
@RequestMapping("/v1/csvDefinition")
class CSVDefinitionController {
    @Autowired
    lateinit var service:CSVDefinitionService

    @Autowired
    lateinit var transformer:TransformerService

    companion object {
        val log: Log = LogFactory.getLog(CSVDefinitionController::class.java)
    }
    @GetMapping("/{csvCode}")
    fun getMDE(@PathVariable csvCode: String, @RequestParam version: Optional<String>): FileDefinition {
        log.info("AUDIT - retrieving definition for $csvCode")

        return if (version.isPresent)
            service.getFileDefinition(csvCode, version.get())
        else
            service.getFileDefinition(csvCode, "LATEST")
    }

    @PostMapping("parse")
    @Throws(InvalidDataException::class)
    fun parseContent(@RequestBody content: String): CSVFile {
        return transformer.parseContentAsCSVFile(content)
    }

    @PostMapping("generate")
    @Throws(InvalidDataException::class)
    fun generateFile(@RequestParam defCode: String, @RequestParam version: String, @RequestBody file: CSVFile): String {
        return transformer.generateContent(defCode, version, file)
    }

    @ExceptionHandler(org.springframework.dao.EmptyResultDataAccessException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleNotFoundError(req: HttpServletRequest, e: org.springframework.dao.EmptyResultDataAccessException): ErrorReceipt {
        return ErrorReceipt(ERROR_CODES.BAD_REQUEST, "Configuration not found", 400, req.servletPath,  null)
    }

}