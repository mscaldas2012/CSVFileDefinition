package gov.cdc.nccdphp.esurveillance.csvDefinition.service

import gov.cdc.nccdphp.esurveillance.bizRulesEngine.RuleEvaluator
import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.DataField
import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.DataRow
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.*
import gov.cdc.nccdphp.esurveillance.validation.model.FieldDefinition
import gov.cdc.nccdphp.esurveillance.validation.model.FileDefinition
import org.springframework.stereotype.Component
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 *
 *
 * @Created - 2019-03-17
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@Component
class Validator(private val valueSetService: ValueSetServices,
                private val mdeDefinition: CSVDefinitionService) {
    private var config: String? = null
    private var version: String? = null

    private var valueSets: Map<String, ValueSet>? = null
    private var definition: FileDefinition? = null

//    @Autowired
//    lateinit  var mdeDefinition: CSVDefinitionService
//    @Autowired
//    lateinit var valueSetService: ValueSetServices

    val booleanKeywords = arrayOf("true", "false", "1", "0", "Y", "N", "Yes", "No")

    private lateinit var calculatedField: RuleEvaluator


    fun configure(config: String, version: String) {
        this.config = config
        this.version = version

        this.valueSets = valueSetService.getValueSetsAsMap()

        definition = mdeDefinition.getFileDefinition(config, version)
        val ruleParser = RuleParserMDE(definition as CSVDefinition)
        calculatedField = CalculatedFieldMDE(ruleParser)
    }

    @Throws(InvalidConfigurationException::class)
    fun validate(file: CSVFile, metadata: Map<String, Any>? = null): ValidationReport {
        if (this.config == null) {
            throw Exception("Validator not properly configured!")
        }
        val report = ValidationReport()
        file.rows.forEach {  validateRow(it, definition!!.fields, metadata, report) }
        return report
    }

    private fun validateRow(row: DataRow, fieldCollection: Array<FieldDefinition>, metadata: Map<String, Any>?, report: ValidationReport) {
        fieldCollection.forEach {
            validateField(row, row.fields.first { a -> a.fieldNumber == it.fieldNumber }, it, metadata, report)}
    }

    private fun validateField(row: DataRow, field: DataField, fieldDef: FieldDefinition, metadata: Map<String, Any>?, report: ValidationReport) {
        validateRequired(row.rowNumber, field, fieldDef, report)
        if (field.value.isNotEmpty()) {
            validateType(row.rowNumber, field, fieldDef, report)
            validateValue(row.rowNumber, field, fieldDef, report)
            if (fieldDef.format != null && fieldDef.type != "Date")
                validateFormat(row.rowNumber, field, fieldDef, report)
            //perform X-field Validation...
            fieldDef.fieldValidationRules?.forEachIndexed { i, r ->
                val cfResult = calculatedField.calculateField(r.rule.replace("\$this", "\$${fieldDef.fieldNumber}"), row, metadata)
                if (cfResult == null || !(cfResult as Boolean)) {
                    val error = ValidationError(Location(row.rowNumber, field.fieldNumber), ValidationCategory.valueOf(r.category), replaceRules(r.message, row, metadata) , "${field.fieldNumber}_10$i", field.value)
                    error.relatedFields = r.relatedFields
                    report.addError(error)
                }

            }
        }
    }
    private fun validateRequired(rowNumber: Int, field: DataField, fieldDef: FieldDefinition, report: ValidationReport) {
        if (fieldDef.required && field.value.isEmpty()) { //None of the values are provided...
            val error = ValidationError(Location(rowNumber, field.fieldNumber), ValidationCategory.ERROR, "${field.fieldNumber}_1", "Field ${fieldDef.name} is required")
            report.addError(error)
        }
    }

    private fun validateFormat(rowNumber: Int, field: DataField, fieldDef: FieldDefinition, report: ValidationReport) {
            if (!field.value.isBlank()) {
                var matches = true
                when (fieldDef.format?.trim()) {
                    "N/A", ""-> {
                    }
                    else -> {
                        val regEx = fieldDef.format!!.toRegex()
                        matches = regEx.matches(field.value)
                    }
                }
                if (!matches) { //see if there's no coded answer:
                    if (answerNotValid( field, fieldDef)) {
                        val error = ValidationError(Location(rowNumber, field.fieldNumber), ValidationCategory.ERROR,"${field.fieldNumber}_6","Answer provided does not match required Format ${fieldDef.format}", field.value)
                        report.addError(error)
                    }
                }
            }
    }

    @Throws(InvalidConfigurationException::class)
    private fun validateType(rowNumber: Int, field: DataField,fieldDef: FieldDefinition, report: ValidationReport) {
        if (!field.value.isBlank()) {
            when (fieldDef.type.toUpperCase()) {
                "NUMBER" ->
                    try {
                         Integer.parseInt(field.value)
                    } catch (e: NumberFormatException) {
                        val error = ValidationError(Location(rowNumber, field.fieldNumber), ValidationCategory.ERROR, "${field.fieldNumber}_2","Value must be numeric",  field.value)
                        report.addError(error)
                    }
                "DATE" -> {
                    try {
                        val df = SimpleDateFormat(fieldDef.format)
                        df.isLenient = false
                        df.parse(field.value)
                    } catch (e: ParseException) {
                        val error = ValidationError(Location(rowNumber, field.fieldNumber), ValidationCategory.ERROR, "${field.fieldNumber}_2","Value must be a valid date",  field.value)
                        report.addError(error)
                    } catch (e: IllegalArgumentException) {
                        throw InvalidConfigurationException("Invalid configuration - unable to use ${fieldDef.format} as a valid Date formatting. Error: ${e.message}")
                    }
                }
                "BOOLEAN" -> {
                    val match = booleanKeywords.filter { field.value.contains(it, ignoreCase = true) }
                    if (match.isEmpty()) {
                        val error = ValidationError(Location(rowNumber, field.fieldNumber), ValidationCategory.ERROR, "${field.fieldNumber}_2","Value must be boolean.",  field.value)
                        report.addError(error)
                    }
                }
            }
        }
    }

    private fun validateValue(rowNumber: Int, field: DataField, fieldDef: FieldDefinition, report: ValidationReport) {
        //Check if Range is defined - if it is and no issues, we're good, but if it fails need to validate if is a valid coded value
        if (fieldDef.rangeMin!= null || fieldDef.rangeMax != null) { //Range is defined...
            val value = field.value
            if (!value.isBlank()) {
                when (fieldDef.type.toUpperCase()) {
                    "NUMERIC" ->
                        try {
                            val intvalue = Integer.parseInt(value)
                            val rangeMin = if (fieldDef.rangeMin != null) Integer.parseInt(fieldDef.rangeMin) else 0
                            if (rangeMin > 0 && intvalue < rangeMin) {
                                val error = ValidationError(Location(rowNumber, field.fieldNumber), ValidationCategory.ERROR, "${field.fieldNumber}_3","Value needs to be greater or equal than ${fieldDef.rangeMin}.", value)
                                report.addError(error)
                            }
                            //Could be a coded value...
                            val rangeMax = if (fieldDef.rangeMax != null) Integer.parseInt(fieldDef.rangeMax) else 0
                            if (rangeMax > 0 && intvalue > rangeMax) {
                                if (fieldDef.possibleAnswers.isNullOrBlank() || answerNotValid(field, fieldDef)) {
                                    val error = ValidationError(Location(rowNumber, field.fieldNumber), ValidationCategory.ERROR, "${field.fieldNumber}_4","Value needs to be less or equal than ${fieldDef.rangeMax}.", value)
                                    report.addError(error)
                                }

                            }
                        } catch (e: NumberFormatException) {
                            //This error is handled on validateType...
                        }
                    "STRING" -> {
                        val rangeMin = if (fieldDef.rangeMin != null) Integer.parseInt(fieldDef.rangeMin) else 0
                        if (rangeMin > 0 && value.length < rangeMin) {
                            val error = ValidationError(Location(rowNumber, field.fieldNumber), ValidationCategory.ERROR, "${field.fieldNumber}_3","Length of value must be equal or greater than ${fieldDef.rangeMin}.", value)
                            report.addError(error)
                        }
                        val rangeMax = if (fieldDef.rangeMax != null) Integer.parseInt(fieldDef.rangeMax) else 0
                        if (rangeMax > 0 && value.length > rangeMax) {
                            val error = ValidationError(Location(rowNumber, field.fieldNumber), ValidationCategory.ERROR, "${field.fieldNumber}_4","Length of value must be equal or less than ${fieldDef.rangeMax}.", value)
                            report.addError(error)
                        }
                    }
                    "DATE" -> {
                        val formatter = SimpleDateFormat(fieldDef.format)
                        val rangeMin = when (fieldDef.rangeMin ) {
                            null -> null
                            "\$TODAY" -> Date()
                            else -> {
                                formatter.parse(fieldDef.rangeMin)
                            }
                        }
                        val rangeMax = when (fieldDef.rangeMax ) {
                            null -> null
                            "\$TODAY" -> Date()
                            else -> {
                                formatter.parse(fieldDef.rangeMax)
                            }
                        }
                        val fieldValueDate = formatter.parse(field.value)
                        if (fieldValueDate < rangeMin) {
                            val error = ValidationError(Location(rowNumber, field.fieldNumber), ValidationCategory.ERROR, "${field.fieldNumber}_4","Dates for this field must be after ${formatter.format(rangeMin)}", value)
                            report.addError(error)

                        }
                        if (fieldValueDate > rangeMax) {
                            val error = ValidationError(Location(rowNumber, field.fieldNumber), ValidationCategory.ERROR, "${field.fieldNumber}_4","Dates for this field must be before ${formatter.format(rangeMax)}", value)
                            report.addError(error)
                        }

                    }
                    //TODO ADD RANGE FOR DATE
//                    else -> {
//                    }
                }
            }
        } else if (!(fieldDef.possibleAnswers.isNullOrBlank()) && (fieldDef.format.isNullOrBlank())) { //Has a Possible answer and is not a date!
            validatePossibleAnswers(rowNumber, field, fieldDef, report)
        }
    }

    private fun answerNotValid(field: DataField, fieldDef: FieldDefinition): Boolean {
        val possibleAnswers = this.valueSets!![fieldDef.possibleAnswers]
        try {
            val match = possibleAnswers?.choices?.filter { a -> a.code == field.value}
            return match == null || match.isEmpty()
        } catch (npe: NullPointerException) {
            throw npe
        }
    }

    private fun validatePossibleAnswers(rowNumber: Int, field: DataField, fieldDef: FieldDefinition, report: ValidationReport) {
        if (!(field.value.isBlank())) {
            if (answerNotValid(field, fieldDef)) {
                val error = ValidationError(Location(rowNumber, field.fieldNumber), ValidationCategory.ERROR, "${field.fieldNumber}_5","Answer provided is not in the set of valid possible answer.", field.value)
                report.addError(error)
            }
        }
    }

    //This method replaces dynamic rules wrapped in %%rule%% with their actual evaluated values.
    private fun replaceRules(message: String, row:DataRow, metadata: Map<String, Any>?): String {
        val rules = mutableListOf<String>()
        var newMessage = message
        var startIndex = message.indexOf("%%")
        if (startIndex > 0) {
            var secondIndex = message.indexOf("%%", startIndex + 2)
            if (secondIndex > 0) {
                newMessage = message.substring(0, startIndex)
                do {
                    val rule = message.substring(startIndex + 2, secondIndex)
                    val calculatedValue = calculatedField.calculateField(rule, row, metadata)
                    newMessage += when (calculatedValue) {
                        is Date -> SimpleDateFormat("M/d/yyyy").format(calculatedValue)
                        else -> calculatedValue.toString()
                    }
                    val tempIndex = secondIndex +2
                    startIndex = message.indexOf("%%", secondIndex + 2)
                    secondIndex = message.indexOf("%%", startIndex + 2)
                    if (startIndex > 0)
                        newMessage += message.substring(tempIndex, startIndex)
                    else newMessage += message.substring(tempIndex)
                } while (startIndex > 0)
            }
        }
        return newMessage
    }
}