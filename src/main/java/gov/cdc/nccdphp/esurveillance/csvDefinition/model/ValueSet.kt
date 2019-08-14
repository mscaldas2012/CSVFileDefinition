package gov.cdc.nccdphp.esurveillance.csvDefinition.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document


@Document(collection = "Value_sets")
data class ValueSet(@Id var id: String?, val name: String, val choices: MutableList<AnswerChoice> = emptyList<AnswerChoice>().toMutableList()) {
    fun addChoice(code: String, label: String) {
        this.choices += (AnswerChoice(code, label))
    }
}

data class AnswerChoice(val code: String, val label: String)
