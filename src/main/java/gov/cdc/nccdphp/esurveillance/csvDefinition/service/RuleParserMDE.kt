package gov.cdc.nccdphp.esurveillance.csvDefinition.service

import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.CalculatedFieldType
import gov.cdc.nccdphp.esurveillance.bizRulesEngine.InvalidRuleException
import gov.cdc.nccdphp.esurveillance.bizRulesEngine.RuleParser
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.CSVDefinition

class RuleParserMDE(val mdeDef: CSVDefinition) : RuleParser() {
    companion object {
        const val FIELD = "\\$[0-9]{1,2}[a-zA-Z](\\[[0-9]\\])?"
    }

    @Throws (InvalidRuleException::class)
    override fun getFieldType(element: String): CalculatedFieldType {
        val field = FIELD.toRegex().find(element)
        val fieldVal = field?.groupValues?.firstOrNull()
        if (fieldVal != null) {
            val result = mdeDef.fields[fieldVal.substring(1, fieldVal.length).toInt()] ?: throw InvalidRuleException("Field $fieldVal is not a recognized field for ${mdeDef.code}")
            return CalculatedFieldType.valueOf(result.type)
        }
        return CalculatedFieldType.Numeric //TODO:: Check if we need to return anything else here.
    }

}