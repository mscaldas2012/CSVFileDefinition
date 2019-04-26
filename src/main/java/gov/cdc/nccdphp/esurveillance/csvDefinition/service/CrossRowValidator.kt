package gov.cdc.nccdphp.esurveillance.csvDefinition.service

import gov.cdc.nccdphp.esurveillance.bizRulesEngine.RuleEvaluator
import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.DataRow
import gov.cdc.nccdphp.esurveillance.validation.model.FieldDefinition
import gov.cdc.nccdphp.esurveillance.validation.model.FileDefinition
import gov.cdc.nccdphp.esurveillance.validation.model.RowValidationDefinition
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 *
 *
 * @Created - 2019-04-10
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@Component
class CrossRowValidator {
    private var config: String? = null
    private var version: String? = null
   // private var valueSets: Map<String, ValueSet>? = null
    private var definition: FileDefinition? = null

    @Autowired
    lateinit  var fileDefinitionService: CSVDefinitionService
    @Autowired
    lateinit var valueSetService: ValueSetServices

    lateinit var calculatedField: RuleEvaluator


    fun configure(config: String, version: String) {
        this.config = config
        this.version = version
       // this.valueSets = valueSetService.getValueSets(this.config)

        definition = fileDefinitionService.getFileDefinition(config, version)
        val ruleParser = RuleParserMDE(definition as CSVDefinition)
        calculatedField = CalculatedFieldMDE(ruleParser)
//        calculatedField = RuleEvaluator(fileDefinitionService).apply {
//            configure(config, version)
//        }
    }

    @Throws(Exception::class)
    fun validate(file: CSVFile): ValidationReport {
        if (this.config == null) {
            throw Exception("Validator not properly configured!")
        }
        val report = ValidationReport()
//        definition!!.rowValidation.forEach {  validateScope(it, file, definition!!) }
        return report
    }

//    private fun validateScope(scope: RowValidationDefinition, file: CSVFile, definition: FileDefinition) {
//        //val uniqueSets = file.rows.distinctBy { listOf(it.fields[0].values[0], it.fields[2].values[0]) }
//
//        // Get all the Unique Sets on the file for the scope
//        val uniqueSets = this.distinctBy(scope.scope, file, definition)
//        println("found ${uniqueSets.size} sets out of ${file.rows.size} rows")
//        uniqueSets.forEach { println(it.fields[2].value)}
//
//        //For each set:
//
//        val fieldPositions = scope.scope.map { (definition.fields.get(it) as FieldDefinition).path-1}
//
//        uniqueSets.forEach{ uniqueKey ->
//                println("\nnew key...")
//                file.rows.filter { //Get all the rows that matches that set and evaluate them!
//                    var found = true
//                    var i = 0
//                    while (found && i < fieldPositions.size) {
//                        found = uniqueKey.fields[fieldPositions[i]].value == it.fields[fieldPositions[i]].value
//                        i++
//                    }
//                     found
//
//                }.forEach {   // Run validation Rules...
//                    print("found ${it.rowNumber} - ")
//                }
//            }
//
//    }

//    fun getListOfFields(scope: List<String>, row: Row): CollectionsKt {
//        var result: List<String>
//    }

//      fun distinctBy(fieldList: List<String>, file: CSVFile, definition: FileDefinition): List<DataRow> {
//        val set = HashSet<String>()
//        val list = ArrayList<DataRow>()
//        val fieldPositions = fieldList.map { (definition.fields.get(it) as FieldDefinition).path-1}
//        for (e in file.rows) {
//            var key = ""
//            for (i in 0..(fieldPositions.size-1))
//                key += e.fields[fieldPositions[i]].value
//            if (set.add(key))
//                list.add(e)
//        }
//        return list
//    }
}