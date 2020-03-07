package gov.cdc.nccdphp.esurveillance.validation.model

/**
 *
 *
 * @Created - 2019-04-12
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
open class CrossFieldValidationDefinition(val rule: String, val message: String, val category: String) {
    var relatedFields: Array<Int>? = null
}