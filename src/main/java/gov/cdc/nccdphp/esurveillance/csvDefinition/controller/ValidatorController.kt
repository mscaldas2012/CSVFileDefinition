package gov.cdc.nccdphp.esurveillance.csvDefinition.controller

import gov.cdc.nccdphp.esurveillance.csvDefinition.model.CSVFile
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.ValidationReport
import gov.cdc.nccdphp.esurveillance.csvDefinition.service.CSVDefinitionService
import gov.cdc.nccdphp.esurveillance.csvDefinition.service.TransformerService
import gov.cdc.nccdphp.esurveillance.csvDefinition.service.Validator
import gov.cdc.nccdphp.esurveillance.csvDefinition.service.ValueSetServices
import gov.cdc.nccdphp.esurveillance.rest.exceptions.InvalidDataException
import gov.cdc.nccdphp.esurveillance.rest.model.ERROR_CODES
import gov.cdc.nccdphp.esurveillance.rest.model.ErrorReceipt
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

/**
 *
 *
 * @Created - 2019-08-14
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@RestController
@RequestMapping("/v1/validator")
class ValidatorController {
    @Autowired
    lateinit var validators: MutableMap<String, Validator>

    @Autowired
    lateinit var valueSetService: ValueSetServices

    @Autowired
    lateinit var transformer: TransformerService
    @Autowired
    lateinit var mdedef: CSVDefinitionService

    companion object {
        val log: Log = LogFactory.getLog(CSVDefinitionController::class.java)
    }

    @PostMapping("\$validate/{config}")
    @Throws(InvalidDataException::class)
    fun parseContent(@PathVariable config: String, @RequestParam version:String, @RequestParam includesHeader: Boolean?, @RequestBody content: String): ValidationReport {

        val file = transformer.parseContentAsCSVFile(content, includesHeader?:true)
        val validator = getValidator(config, version?:"1.0")
        return validator.validate(file)
    }

    private fun getValidator(config: String, version: String): Validator {
        val validator = validators.get("$config-$version")
        if (validator == null) {
            val newValidator = Validator(valueSetService, mdedef)
            newValidator.configure(config, version)
            validators.put("$config-$version", newValidator)
            return newValidator
        }
        return validator
    }
}