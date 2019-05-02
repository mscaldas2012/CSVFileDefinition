package gov.cdc.nccdphp.esurveillance.bizRulesEngine

import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.Leaf
import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.CalculatedFieldType
import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.Expression
import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.Node
import org.springframework.stereotype.Component

/**
 * This Class is responsible for parsing the rules in string format as a Tree that can be
 * evaluated by the RuleEvaluator.
 *
 * @Created - 2019-04-12
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@Component
abstract class RuleParser {
    //Caches equations that have been parsed into Trees
    private var trees:HashMap<String, Node> = HashMap()

    fun buildFullTree(equation: String): Node {
        if (!trees.containsKey(equation)) {
            val exprSplit = run {
                val expressions = "[\\+\\-\\*\\/\\^\\(\\)]|==|!=|>=|<=|> |< |IN|AFTER|BEF|AND|OR|NOT|$$"
                val expressionRegEx = "(?<=$expressions)|(?=$expressions)".toRegex()
                expressionRegEx.split(equation).filter { !it.isBlank() }.map { it.trim() }

            }
            trees.put(equation,buildtree(exprSplit))
        }
        return trees[equation]!!

    }

    private fun buildtree(s: List<String>): Node {
        if (s.size == 1) {
            return Leaf(s[0], getFieldType(s[0]))
        }
        when (s[0]) {
            "(" -> {
                findCloseParenthesis(s.subList(1, s.size)).let {
                    val node = buildtree(s.subList(1, it))
                    return if (it == s.size - 1) node
                    else Expression(node, s[it + 1], buildtree(s.subList(it + 2, s.size)))
                }
            }
            "NOT", "$$" -> {
                val notNode: Node
                var cp = 1
                if (s[1] == "(") {
                    cp = findCloseParenthesis(s.subList(2, s.size))
                    notNode = Expression(null, s[0], buildtree(s.subList(2, cp + 1))) //Add the NOT operator
                    cp += 1 //Add close parenthesis
                } else {
                    notNode = Expression(null, s[0], Leaf(s[1], getFieldType(s[1])))

                }
                return if (cp == s.size - 1) { //don't count last close parenthesis
                    notNode
                } else {
                    Expression(notNode, s[cp + 1], buildtree(s.subList(cp + 2, s.size)))
                }
            }
            else -> {
                val endOfConditional = findEndOfCondition(s.subList(2, s.size))
                val n = Expression(Leaf(s[0], getFieldType(s[0])), s[1], buildtree(s.subList(2, endOfConditional)))
                return if (s.size > endOfConditional) //Keep Building...
                    Expression(n, s[3], buildtree(s.subList(4, s.size)))
                else n
            }
        }
    }

    /**
     * A conditional (no parenthesis) can have only the following formats:
     * - <$f> <COMPARATOR> <Value>, field comparing with a value
     * - <$F> <COMPARATOR <$F>    , field comparing with another field
     * - <$F> <COMPARATOR> $$(CalcField) , field comparing with a calculated field.
     *
     * The first two options, the result should be 3
     * THe last option is just a matter of finding that close parenthesis...
     */

    private fun findEndOfCondition(subList: List<String>): Int {
        return if (subList.size > 1 && "$$" == subList[0]) {
            findCloseParenthesis(subList.subList(2, subList.size)) + 4
        } else 3
    }


    private fun findCloseParenthesis(s: List<String>): Int {
        var openParenthesisCount = 1 //we already found one open parenthesis.
        var i = 0
        while (openParenthesisCount != 0) {
            when (s[i]) {
                "(" -> openParenthesisCount++
                ")" -> openParenthesisCount--
            }
            i++
        }
        return i
    }

    @Throws(InvalidRuleException::class)
    abstract fun getFieldType(element: String): CalculatedFieldType
}