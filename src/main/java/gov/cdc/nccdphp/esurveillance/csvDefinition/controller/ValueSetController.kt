package gov.cdc.nccdphp.esurveillance.csvDefinition.controller

import gov.cdc.nccdphp.esurveillance.csvDefinition.model.ValueSet
import gov.cdc.nccdphp.esurveillance.csvDefinition.service.ValueSetServices
import org.apache.commons.logging.Log
import org.springframework.web.bind.annotation.*
import org.apache.commons.logging.LogFactory


/**
 *
 *
 * @Created - 2019-05-01
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@RestController
@RequestMapping("/v1/valuesets")
class ValueSetController(private val valueSetServices: ValueSetServices) {

    companion object {
        val LOG: Log = LogFactory.getLog(ValueSetController::class.java)
    }
    private var vsCache: MutableMap<String, ValueSet>? = null

    @GetMapping("")
    fun getAll(): MutableMap<String, ValueSet> {
        LOG.info("AUDIT: Retrieving ValueSets Map")
        return getValueSets()
    }

    @GetMapping("/{valueSetName}")
    fun getValueSet(@PathVariable valueSetName: String): ValueSet? {
        LOG.info("AUDIT: Retrieving ValueSet $valueSetName")
        val vs =  getValueSets()
        return vs.getValue(valueSetName)
    }

    @DeleteMapping("/cache")
    fun invalidateCache() {
        LOG.info("AUDIT: Clearing cache")
        vsCache = null
    }

    private fun getValueSets(): MutableMap<String, ValueSet> {
        if (vsCache == null) {
            LOG.info("AUDIT: Initializing Cache")
            vsCache = valueSetServices.getValueSetsAsMap()
        }
        return vsCache!!
    }
}