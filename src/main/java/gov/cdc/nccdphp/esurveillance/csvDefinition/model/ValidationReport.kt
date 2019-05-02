package gov.cdc.nccdphp.esurveillance.csvDefinition.model

/**
 *
 *
 * @Created - 2019-04-24
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
data class ValidationReport(val errors: MutableList<ValidationError> = emptyList<ValidationError>().toMutableList()) {
    fun addError(error: ValidationError ) {
        errors += error
    }


}