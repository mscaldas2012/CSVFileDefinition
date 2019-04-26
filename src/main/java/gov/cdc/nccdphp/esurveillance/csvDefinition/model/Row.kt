package gov.cdc.nccdphp.esurveillance.csvDefinition.model

import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.DataField
import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.DataRow

/**
 *
 *
 * @Created - 2019-04-24
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
class Row(override val rowNumber: Int, override val fields: Array<DataField>) : DataRow