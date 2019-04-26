package gov.cdc.nccdphp.esurveillance.bizRulesEngine

import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.*

import org.springframework.stereotype.Component
import java.util.*


/**
 * This Class is capable of evaluate trees generated by the RuleParser class.
 * It is also using for validation purposes, but evaluating the rules to Booleans.
 *
 * A Rule can be a simple conditional - Ex.: (a + b) or (a >= b)
 * Or it can have a Pre-condition to be met: Ex.: (A == NULL) => (B != NULL)
 *  It reads if A is NULL, then B must not be null.
 *
 * A calculated field can have many chained expressions wiht two pipes (||)
 *   Ex.: DateA || DateB || DateC - the value returned will be DateA if that is not NULL, or DateB if DateA is null
 *   or DateC if both A and B are null
 *
 * @Created - 2019-03-31
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@Component
abstract class RuleEvaluator(val ruleParser: RuleParser) {
    fun calculateField(equation: String, row: DataRow): Any? {
        val ruleSetRegEx = "\\|\\|".toRegex()
        val ruleSet = ruleSetRegEx.split(equation)
        var result: Any?
        ruleSet.forEach { r ->
            result = executeRule(r, row)
            if (result != null)
                return result
        }
        return null
    }

    private fun executeRule(testRule: String, row: DataRow): Any? {
        val preconditionRegEx = "=>".toRegex()
        val equation = preconditionRegEx.split(testRule)

        return if (equation.size == 1) { //No pre-conditions available
            val tree = ruleParser.buildFullTree(equation[0])
            eval(tree, row)
        } else {
            val treeL = ruleParser.buildFullTree(equation[0])
            if (eval(treeL, row) as Boolean) { //Pre-condition must always evaluate to a Boolean!
                val treeR = ruleParser.buildFullTree(equation[1])
                eval(treeR, row)
            } else {
                true
            }
        }
    }

    private fun eval(node: Node?, row: DataRow): Any? {
        when (node) {
            is Expression -> {
                return when (node.operation) {
                    //BOOLEAN Expressions
                    /**
                     * This method is used for any validation rule as well as any Pre-condition!
                     * Boolean evaluation is the most complex ones.
                     *
                     * First of, you can append several conditionals with ANDs and ORs.
                     * You can also negate a conditional with NOT (<conditional)
                     * Parethesis enforces what needs to be executed first - this is achieved while building the tree.
                     *
                     * Eventually, you can have several comparisons on a conditional:
                     *  -  ==, !=, <, <=, >, >=  can be used for Numbers and Dates
                     *  - AFTER, BEF can be used for Dates Only
                     *  - IN can be used for any list.
                     */
                    "AND" -> (eval(node.left, row) as Boolean).and(eval(node.right, row) as Boolean)
                    "OR" -> (eval(node.left, row) as Boolean).or(eval(node.right, row) as Boolean)
                    "NOT" -> (eval(node.right, row) as Boolean).not()
                    //Calculated FIeld Expression
                    "$$" -> eval(node.right, row)
                    //INT/Date  OPERATORS:
                    /**
                     * With evalInt, you can add, subtract, multiply or divide two numbers.
                     */
                    "+" -> {
                        val leftEval = eval(node.left, row)
                        val rightEval = eval(node.right, row)
                        when (leftEval) {
                            is Int -> leftEval.plus(rightEval as Int)
                            is Date -> getDate(rightEval, leftEval, node.operation)

                            else -> false
                        }
                    }
                    "-" -> {
                        val leftEval = eval(node.left, row)
                        val rightEval = eval(node.right, row)
                        when (leftEval) {
                            is Int -> leftEval.minus(rightEval as Int)
                            is Date -> getDate(rightEval, leftEval, node.operation)

                            else -> false
                        }
                    }
                    //INT Operators...
                    "*" -> (eval(node.left, row) as Int).times(eval(node.right, row) as Int)
                    "/" -> (eval(node.left, row) as Int).div(eval(node.right, row) as Int)
                    //BOOLEAN OPERATORS:
                    "==" -> {
                        val leftEval = eval(node.left, row)
                        val rightEval = eval(node.right, row)
                        if (rightEval == null)
                            leftEval == null
                        else
                            when (leftEval) {
                                is Int -> leftEval == (rightEval as Int)
                                else -> leftEval == rightEval
                            }
                    }
                    "!=" -> {
                        val leftEval = eval(node.left, row)
                        val rightEval = eval(node.right, row)
                        if (rightEval == null)
                            leftEval != null
                        else
                            when (leftEval) {
                                is Int -> leftEval != (rightEval as Int)
                                is String -> leftEval != rightEval.toString()
                                else -> leftEval != rightEval
                            }
                    }
                    ">", "AFTER" ->  {
                        val leftEval = eval(node.left, row)
                        val rightEval = eval(node.right, row)
                        when (leftEval) {
                            is Int -> leftEval > (rightEval as Int)
                            is Date -> leftEval > (rightEval as Date)
                            else -> false
                        }
                    }
                    ">=" -> {
                        val leftEval = eval(node.left, row)
                        val rightEval = eval(node.right, row)
                        when (leftEval) {
                            is Int -> leftEval >= (rightEval as Int)
                            is Date -> leftEval >= (rightEval as Date)
                            else -> false
                        }
                    }
                    "<", "BEF" -> {
                        val leftEval = eval(node.left, row)
                        val rightEval = eval(node.right, row)
                        when (leftEval) {
                            is Int -> leftEval < (rightEval as Int)
                            is Date -> leftEval < (rightEval as Date)
                            else -> false
                        }
                    }
                    "<=" -> {
                        val leftEval = eval(node.left, row)
                        val rightEval = eval(node.right, row)
                        when (leftEval) {
                            is Int -> leftEval <= (rightEval as Int)
                            is Date -> leftEval <= (rightEval as Date)
                            else -> false
                        }
                    }
                    "IN" -> {
                        val op1 = eval(node.left, row)
                        val op2 = (node.right as Leaf).element //should be an array of options...
                        val values = op2.replace("{", "").replace("}", "").split(",")
                        val foundE = values.find { it.trim() == op1 }
                        foundE != null
                    }
                    else -> false
                }
            }
            is Leaf -> {
                return getValue(node.element, row, node.type)
            }
            else -> throw Exception("Invalid Node type!")
        }
    }

    private fun getDate(rightEval: Any?, leftEval: Date, operation: String): Date? {
        val changeValues = (rightEval as String).split("~") //[0] number [1] -> unit.

        val unit = changeValues[1].run {
            when (this) {
                "DAYS", "DAY" -> Calendar.DAY_OF_MONTH
                "MONTHS", "MONTH" -> Calendar.MONTH
                "YEARS", "YEAR" -> Calendar.YEAR
                else -> Calendar.DAY_OF_MONTH
            }
        }
        val calendar = Calendar.getInstance().apply {
            time = leftEval
            this.add(unit, changeValues[0].toInt() * if (operation == "+") 1 else -1)
        }
        return calendar.time
    }



    abstract fun getValue(operand: String, row: DataRow, fieldType: CalculatedFieldType): Any?

}