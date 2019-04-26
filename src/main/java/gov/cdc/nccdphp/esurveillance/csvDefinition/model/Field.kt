package gov.cdc.nccdphp.esurveillance.csvDefinition.model

import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.DataField

/**
 *
 *
 * @Created - 2019-04-24
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
class Field(override val fieldNumber: Int, override var value: String) : DataField