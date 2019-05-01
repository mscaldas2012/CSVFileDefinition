package gov.cdc.nccdphp.esurveillance.csvDefinition.model

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "Value_sets")
class ValueSet(val name: String, val choices: MutableList<AnswerChoice>) {
    fun addChoice(code: String, label: String) {
        this.choices += (AnswerChoice(code, label))
    }
}

class AnswerChoice(val code: String, val label: String)
