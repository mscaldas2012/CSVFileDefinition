package gov.cdc.nccdphp.esurveillance.csvDefinition.model

/**
 *
 *
 * @Created - 2019-04-24
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
data class ValidationError(val location:Location, val category: ValidationCategory, val errorCode: String, val message: String) {
    var culpritValue: String? = null
    var relatedFields: Array<Int>? = null

    constructor(l:Location, c:ValidationCategory, m: String, e: String, culprit: String): this(l, c, e, m) {
        culpritValue = culprit
    }
}

data class Location(val rowNumber: Int, val fieldNumber: Int)