package gov.cdc.nccdphp.esurveillance.csvDefinition.service

import gov.cdc.nccdphp.esurveillance.bizRulesEngine.RuleEvaluator
import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.CalculatedFieldType
import gov.cdc.nccdphp.esurveillance.bizRulesEngine.RuleParser
import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.DataRow
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class CalculatedFieldMDE(ruleParser: RuleParser): RuleEvaluator(ruleParser) {


    override fun getValue(operand: String, row: DataRow, fieldType: CalculatedFieldType, metadata: Map<String, Any>? ): Any? {
        var fieldValue: String? = operand
        when {
            "\$TODAY" == operand -> return Date()
            "NULL" == operand.toUpperCase() -> return null
            operand.startsWith("\$METADATA_") -> {
                    return metadata?.get(operand.substring(10))
            }
            operand.startsWith("$") -> {
                //TODO::Use Hashmaps on Rows for faster finding of field
                val field = RuleParserMDE.FIELD.find(operand)
                if (field != null) {
                    val fieldNbr = field.groups[1]?.value
                    val field = row.fields.find { f -> f.fieldNumber == fieldNbr?.toInt() }
                    fieldValue = field?.value
                }
            }
            operand.contains("~") -> {
                return operand //It's a Date calculation... will add/subtract number of days/months/years
            }
        }
        return if (!fieldValue.isNullOrBlank()) {
            when (fieldType) {
                CalculatedFieldType.Numeric -> fieldValue.toInt()
                CalculatedFieldType.Date -> {
                    //TODO::Use date format from Definition!
                    val formatter = DateTimeFormatter.ofPattern("M/d/yyyy", Locale.ENGLISH)
                    LocalDate.parse(fieldValue, formatter).let {
                        Date.from(it.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
                    }
                }
                CalculatedFieldType.Character, CalculatedFieldType.String -> fieldValue
                CalculatedFieldType.Boolean -> fieldValue.toBoolean()
            }
        } else null
    }
}