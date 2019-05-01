package gov.cdc.nccdphp.esurveillance.csvDefinition.controller

import gov.cdc.nccdphp.esurveillance.csvDefinition.model.ValueSet
import gov.cdc.nccdphp.esurveillance.csvDefinition.service.ValueSetServices
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 *
 * @Created - 2019-05-01
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@RestController
@RequestMapping("/v1/valuesets")
class ValueSetController(private val valueSetServices: ValueSetServices) {

    @GetMapping("")
    fun getAll(): Map<String, ValueSet> {
        return valueSetServices.getValueSets()
    }

    @GetMapping("/{valueSetName}")
    fun getValueSet(@PathVariable valueSetName: String): ValueSet? {
        return valueSetServices.getValueSets().get(valueSetName)
    }
}