package gov.cdc.nccdphp.esurveillance.csvDefinition.service

import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.CalculatedFieldType
import gov.cdc.nccdphp.esurveillance.bizRulesEngine.InvalidRuleException
import gov.cdc.nccdphp.esurveillance.bizRulesEngine.RuleParser
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.CSVDefinition

class RuleParserMDE(private val mdeDef: CSVDefinition) : RuleParser() {
    companion object {
         val FIELD = "\\$([0-9]{1,2})".toRegex()
       // val METADATA = "\\$[METADATA_](a-zA-A-_])".toRegex()
    }

    @Throws (InvalidRuleException::class)
    override fun getFieldType(element: String): CalculatedFieldType {
        val field = FIELD.find(element)
        if (field != null) {
            val fieldVal = field.groups[1]?.value
            if (fieldVal != null) {
                val result = mdeDef.fields[fieldVal.toInt() - 1]
                return CalculatedFieldType.valueOf(result.type)
            }
        }
        return try {
            Integer.parseInt(element)
            CalculatedFieldType.Numeric
        } catch (e: NumberFormatException) {
            CalculatedFieldType.String
        }
    }

}