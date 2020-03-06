package gov.cdc.nccdphp.esurveillance.validation.model

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.mongodb.core.mapping.Field

@JsonInclude(JsonInclude.Include.NON_NULL)
open class FieldDefinition(val fieldNumber: Int, val type: String, val required: Boolean) {
    var name: String? = null
    //Validation
    var format: String? = null
    var rangeMin: String? = null
    var rangeMax: String? = null
    var possibleAnswers: String? = null
    var fieldValidationRules: List<CrossFieldValidationDefinition>? = null

    var category: String? = null
    var label: String? = null
    var relatedInfo: String? = null

    //This attribute indicates whether the value should be consistent for all rows
    //identified by the given scope, with the identifier fields.
    var mustBeConsistent: Boolean = false

}
