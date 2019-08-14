package gov.cdc.nccdphp.esurveillance.csvDefinition.controller

import gov.cdc.nccdphp.esurveillance.csvDefinition.model.CSVFile
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.ValidationReport
import gov.cdc.nccdphp.esurveillance.csvDefinition.service.TransformerService
import gov.cdc.nccdphp.esurveillance.csvDefinition.service.Validator
import gov.cdc.nccdphp.esurveillance.rest.exceptions.InvalidDataException
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 *
 * @Created - 2019-08-14
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@RestController
@RequestMapping("/v1/csvDefinition")
class ValidatorController {
    @Autowired
    lateinit var validator: Validator

    @Autowired
    lateinit var transformer: TransformerService

    companion object {
        val log: Log = LogFactory.getLog(CSVDefinitionController::class.java)
    }

    @PostMapping("validate/{config}")
    @Throws(InvalidDataException::class)
    fun parseContent(config: String, @RequestBody content: String): ValidationReport {
        val file = transformer.parseContentAsCSVFile(content)
        return validator.validate(file)
    }

}