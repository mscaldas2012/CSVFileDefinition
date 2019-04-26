package gov.cdc.nccdphp.esurveillance.csvDefinition.model

import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.DataRow

/**
 *
 *
 * @Created - 2019-04-24
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
class CSVFile(val fileName: String) {
    var rows:MutableList<DataRow> = ArrayList()

    fun addRow(row: DataRow) {
        rows.add(row)
    }
}