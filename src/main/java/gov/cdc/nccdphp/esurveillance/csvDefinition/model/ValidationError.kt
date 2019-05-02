package gov.cdc.nccdphp.esurveillance.csvDefinition.model

/**
 *
 *
 * @Created - 2019-04-24
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
data class ValidationError(val location:Location, val category: ValidationCategory, val message: String) {
    var culpritValue: String? = null

    constructor(l:Location, c:ValidationCategory, m: String, culprit: String): this(l, c, m) {
        culpritValue = culprit
    }
}

data class Location(val rowNumber: Int, val fieldNumber: Int)