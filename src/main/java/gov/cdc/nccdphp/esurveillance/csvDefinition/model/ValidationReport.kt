package gov.cdc.nccdphp.esurveillance.csvDefinition.model

/**
 *
 *
 * @Created - 2019-04-24
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
class ValidationReport() {
    var errors: MutableList<ValidationError> = ArrayList()

    fun addError(error: ValidationError ) {
        errors.add(error)
    }

}