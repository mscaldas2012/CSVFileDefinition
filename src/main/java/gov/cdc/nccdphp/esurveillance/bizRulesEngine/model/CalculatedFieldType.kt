package gov.cdc.nccdphp.esurveillance.bizRulesEngine.model

/**
 *
 *
 * @Created - 2019-03-31
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
enum class CalculatedFieldType {
    Character,
    Numeric,
    Date,
    Boolean
}

enum class CalculatedFieldOperation {
    PLUS,
    MINUS,
    TIMES,
    DIVISION
}