package gov.cdc.nccdphp.esurveillance.csvDefinition.model

import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.DataField
import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.DataRow

/**
 *
 *
 * @Created - 2019-04-24
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
data class Row(override val rowNumber: Int, override val fields: Array<DataField>) : DataRow {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Row

        if (rowNumber != other.rowNumber) return false
        if (!fields.contentEquals(other.fields)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rowNumber
        result = 31 * result + fields.contentHashCode()
        return result
    }
}