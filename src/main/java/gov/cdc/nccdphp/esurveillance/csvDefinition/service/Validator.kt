package gov.cdc.nccdphp.esurveillance.csvDefinition.service

import gov.cdc.nccdphp.esurveillance.bizRulesEngine.RuleEvaluator
import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.DataField
import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.DataRow
import gov.cdc.nccdphp.esurveillance.validation.model.FieldDefinition
import gov.cdc.nccdphp.esurveillance.validation.model.FileDefinition
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.*
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.ValidationError
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.ValueSet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.text.ParseException
import java.text.SimpleDateFormat


/**
 *
 *
 * @Created - 2019-03-17
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@Component
class Validator {
    private var config: String? = null
    private var version: String? = null
    private var valueSets: Map<String, ValueSet>? = null
    private var definition: FileDefinition? = null

    @Autowired
    lateinit  var mdeDefinition: CSVDefinitionService
    @Autowired
    lateinit var valueSetService: ValueSetServices

    val booleanKeywords = arrayOf("true", "false", "1", "0", "Y", "N")

    private lateinit var calculatedField: RuleEvaluator


    fun configure(config: String, version: String) {
        this.config = config
        this.version = version
        this.valueSets = valueSetService.getValueSetsAsMap().block()

        definition = mdeDefinition.getFileDefinition(config, version)
        val ruleParser = RuleParserMDE(definition as CSVDefinition)
        calculatedField = CalculatedFieldMDE(ruleParser)
    }

    @Throws(InvalidConfigurationException::class)
    fun validate(file: CSVFile): ValidationReport {
        if (this.config == null) {
            throw Exception("Validator not properly configured!")
        }
        val report = ValidationReport()
        file.rows.forEach {  validateRow(it, definition!!.fields, report) }
        return report
    }

    private fun validateRow(row: DataRow, fieldCollection: Array<FieldDefinition>, report: ValidationReport) {
        fieldCollection.forEach {
            validateField(row, row.fields.first { a -> a.fieldNumber == it.path }, it, report)}
    }

    private fun validateField(row: DataRow, field: DataField, fieldDef: FieldDefinition, report: ValidationReport) {
        validateRequired(row.rowNumber, field, fieldDef, report)
        if (field.value.isNotEmpty()) {
            validateType(row.rowNumber, field, fieldDef, report)
            validateValue(row.rowNumber, field, fieldDef, report)
            if (fieldDef.format != null && fieldDef.type != "Date")
                validateFormat(row.rowNumber, field, fieldDef, report)
            //perform X-field Validation...
            fieldDef.fieldValidationRules?.forEach { r ->
                //r.rule.replace("\$this", "\$${fieldDef.path}")
                val cfResult = calculatedField.calculateField(r.rule.replace("\$this", "\$${fieldDef.path}"), row)

                if (cfResult == null || !(cfResult as Boolean)) {
                    val error = ValidationError(Location(row.rowNumber, field.fieldNumber), ValidationCategory.valueOf(r.category), r.message, field.value)
                    report.addError(error)
                }
            }
        }
    }
    private fun validateRequired(rowNumber: Int, field: DataField, fieldDef: FieldDefinition, report: ValidationReport) {
        if (fieldDef.required && field.value.isEmpty()) { //None of the values are provided...
            val error = ValidationError(Location(rowNumber, field.fieldNumber), ValidationCategory.ERROR, "Field ${fieldDef.name} is required")
            report.addError(error)
        }
    }

    private fun validateFormat(rowNumber: Int, field: DataField, fieldDef: FieldDefinition, report: ValidationReport) {
            if (!field.value.isBlank()) {
                var matches = true
                when (fieldDef.format?.trim()) {
                    "N/A", ""-> {
                    }


                }
                if (!matches) { //see if there's no coded answer:
                    if (answerNotValid( field, fieldDef)) {
                        val error = ValidationError(Location(rowNumber, field.fieldNumber), ValidationCategory.ERROR,"Answer provided does not match required Format ${fieldDef.format}", field.value)
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
                        val error = ValidationError(Location(rowNumber, field.fieldNumber), ValidationCategory.ERROR, "Invalid number provided for field",  field.value)
                        report.addError(error)
                    }
                "DATE" -> {
                    try {
                        val df = SimpleDateFormat(fieldDef.format)
                        df.isLenient = false
                        df.parse(field.value)
                    } catch (e: ParseException) {
                        val error = ValidationError(Location(rowNumber, field.fieldNumber), ValidationCategory.ERROR, "Invalid date provided for field",  field.value)
                        report.addError(error)
                    } catch (e: IllegalArgumentException) {
                        throw InvalidConfigurationException("Invalid configuration - unable to use ${fieldDef.format} as a valid Date formatting. Error: ${e.message}")
                    }
                }
                "BOOLEAN" -> {
                    val match = booleanKeywords.filter { field.value.contains(it, ignoreCase = true) }
                    if (match.isEmpty()) {
                        val error = ValidationError(Location(rowNumber, field.fieldNumber), ValidationCategory.ERROR, "Invalid value provided for boolean field.",  field.value)
                        report.addError(error)
                    }
                }
            }
        }
    }

    private fun validateValue(rowNumber: Int, field: DataField, fieldDef: FieldDefinition, report: ValidationReport) {
        //Check if Range is defined - if it is and no issues, we're good, but if it fails need to validate if is a valid coded value
        if (fieldDef.rangeMin!! > 0 || fieldDef.rangeMax!! > 0) { //Range is defined...
                val value = field.value
                if (!value.isBlank()) {
                    try {
                        val intvalue = Integer.parseInt(value)
                        if (fieldDef.rangeMin!! > 0 && intvalue < fieldDef.rangeMin!!) {
                            val error = ValidationError(Location(rowNumber, field.fieldNumber), ValidationCategory.ERROR, "Value needs to be greater or equal than ${fieldDef.rangeMin}.", value)
                            report.addError(error)
                        }
                        //Coudl be a coded value...
                        if (fieldDef.rangeMax!! > 0 && intvalue > fieldDef.rangeMax!!) {
                            if (fieldDef.possibleAnswers.isNullOrBlank() || answerNotValid(field, fieldDef)) {
                                val error = ValidationError(Location(rowNumber, field.fieldNumber), ValidationCategory.ERROR, "Value needs to be less or equal than ${fieldDef.rangeMax}.", value)
                                report.addError(error)
                            }
                        }
                    } catch (e: NumberFormatException) {
                        //This error is handled on validateType...
                    }

                }
        } else if (!(fieldDef.possibleAnswers.isNullOrBlank()) && (fieldDef.format.isNullOrBlank())) { //Has a Possible answer nad is not a date!
            validatePossibleAnswers(rowNumber, field, fieldDef, report)
        }
    }

    private fun answerNotValid(field: DataField, fieldDef: FieldDefinition): Boolean {
        val possibleAnswers = this.valueSets!![fieldDef.possibleAnswers]
        try {
            val match = possibleAnswers!!.choices.filter { a -> a.code == field.value}
            return match.isEmpty()
        } catch (npe: NullPointerException) {
            throw npe
        }
    }

    private fun validatePossibleAnswers(rowNumber: Int, field: DataField, fieldDef: FieldDefinition, report: ValidationReport) {
            if (!(field.value.isBlank())) {
                if (answerNotValid(field, fieldDef)) {
                    val error = ValidationError(Location(rowNumber, field.fieldNumber), ValidationCategory.ERROR, "Answer provided is not part of a valid possible answer.", field.value)
                    report.addError(error)
                }
            }
    }


}

