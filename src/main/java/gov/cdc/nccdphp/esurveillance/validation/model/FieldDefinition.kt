package gov.cdc.nccdphp.esurveillance.validation.model

import org.springframework.data.mongodb.core.mapping.Field

open class FieldDefinition(@Field("fieldNumber") val path: Int, val type: String, val required: Boolean) {
    var name: String? = null
    //Validation
    var format: String? = null
    var rangeMin: Double? = 0.0
    var rangeMax: Double? = 0.0
    var possibleAnswers: String? = null
    var fieldValidationRules: List<CrossFieldValidationDefinition>? = null

    var category: String? = null
    var label: String? = null



}
