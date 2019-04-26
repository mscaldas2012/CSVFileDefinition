package gov.cdc.nccdphp.esurveillance.bizRulesEngine.model

/**
 * @Created - 2019-03-20
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
data class Expression(val left:  Node?, val operation: String, val right: Node): Node {
}