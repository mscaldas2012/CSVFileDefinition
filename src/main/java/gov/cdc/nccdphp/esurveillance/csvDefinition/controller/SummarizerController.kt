package gov.cdc.nccdphp.esurveillance.csvDefinition.controller

import gov.cdc.nccdphp.esurveillance.csvDefinition.model.CSVDefinition
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.Summary
import gov.cdc.nccdphp.esurveillance.csvDefinition.service.CSVDefinitionService
import gov.cdc.nccdphp.esurveillance.csvDefinition.service.SummarizerService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/summary")
class SummarizerController(val mdeDefinition: CSVDefinitionService, val summarizer: SummarizerService) {

    private val configCache = HashMap<String, CSVDefinition>()

    @PostMapping("/{config}")
    fun summarize(@PathVariable config: String, @RequestParam version: String, @RequestParam includesHeader: Boolean = true, @RequestBody content: String): Summary {
        val skipheader = if (includesHeader) 1 else 0
        val summ =  summarizer.summarize(getDef(config, version), content, skipheader)
        return summ
    }

    private fun getDef(config: String, version: String): CSVDefinition {
        var csvDef = configCache.get("$config-$version")
        if (csvDef == null) {
            csvDef = mdeDefinition.getFileDefinition(config, version)
            configCache.put("$config-$version", csvDef)
        }
        return csvDef
    }
}