package gov.cdc.nccdphp.esurveillance.bizRulesEngine.model
/**
 * @Created - 2019-04-12
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
interface DataRow {
    val rowNumber: Int
    val fields: Array<DataField>
}