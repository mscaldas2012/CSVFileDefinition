package gov.cdc.nccdphp.esurveillance.validation.model

class RowValidationDefinition(val scope: List<String>, val rules: List<CrossFieldValidationDefinition>) {
    var name: String? = null
}

