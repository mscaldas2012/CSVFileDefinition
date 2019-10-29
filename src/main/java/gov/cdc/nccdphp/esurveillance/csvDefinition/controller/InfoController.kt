package gov.cdc.nccdphp.esurveillance.csvDefinition.controller

import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.core.JsonProcessingException
import gov.cdc.nccdphp.esurveillance.View
import gov.cdc.nccdphp.esurveillance.csvDefinition.About
import gov.cdc.nccdphp.esurveillance.csvDefinition.EipServiceConfig
import gov.cdc.nccdphp.esurveillance.csvDefinition.repository.ValueSetMongoRepo
import gov.cdc.nccdphp.esurveillance.data.DataLoader
import gov.cdc.nccdphp.esurveillance.rest.model.HEALTH_STATUS
import gov.cdc.nccdphp.esurveillance.rest.model.HealthReceipt
import org.apache.catalina.valves.HealthCheckValve
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 *
 *
 * @Created - 2019-05-08
 * @Author Marcelo Caldas mcq1@cdc.gov
 */

@RestController
@RequestMapping("/info/")
class InfoController(val dataLoader: DataLoader) {

    @Autowired
    private val about: About? = null

    @Autowired
    private val config: EipServiceConfig? = null

    @Autowired
    private val valueSetRepo: ValueSetMongoRepo? = null

    @JsonView(View.Summary::class)
    @GetMapping( "/about")
    // produces = {"application/cdc.info.about-v1+json"}) //This forces Safari to download the file instead of opening it on the browser.
    @ResponseBody
    @Throws(JsonProcessingException::class)
    fun about(): About? {
        return about
    }

    @GetMapping( "/version")
    fun getVersion(): String {
        return "Version: " + javaClass.getPackage().implementationVersion
    }

    @GetMapping("/ping")
    fun ping(): String {
        return "Hello There! I'm alive.\nYou pinged me at " + ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT)
    }

    @GetMapping("/health")
    fun health(): HealthReceipt {
        var dberror: String? = null
        val dbstatus =
            try {
                valueSetRepo!!.findByName("YN_LOOKUP")
                 HEALTH_STATUS.OK
            } catch ( e: Exception ) {
                //e.printStackTrace()
                dberror = e.message!!
                HEALTH_STATUS.DOWN;
            }
        val health = HealthReceipt(HEALTH_STATUS.OK, dbstatus)
        health.dbErrorMessage = dberror
        return health
    }

    private fun pingDB(): Boolean {
        return try {
            valueSetRepo!!.findByName("YN_LOOKUP")
            true
        } catch ( e: Exception ) {
            e.printStackTrace()
            false;
        }
    }

    @JsonView(View.Summary::class)
    @GetMapping("/config", produces = ["application/json"])
    fun getConfig(): EipServiceConfig? {
        return config
    }

    @PostMapping( "/loadData/{config}")
    fun loadData(@RequestBody content: String, @PathVariable config: String): String {
        if ("VALUESET" == config)
            dataLoader.loadDataSets(content)
        else {
            dataLoader.loadDefinition(content)
        }
        return "$config Successfully Loaded"
    }



}