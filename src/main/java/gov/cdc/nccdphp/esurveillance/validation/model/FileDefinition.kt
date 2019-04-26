package gov.cdc.nccdphp.esurveillance.validation.model

/**
 *
 *
 * @Created - 2019-04-12
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
open class FileDefinition {
    lateinit var fields:Array<FieldDefinition>
    var rowValidation: List<RowValidationDefinition>? = null
}