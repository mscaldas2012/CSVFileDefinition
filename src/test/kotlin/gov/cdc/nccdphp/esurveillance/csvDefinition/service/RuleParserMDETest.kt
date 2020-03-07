package gov.cdc.nccdphp.esurveillance.csvDefinition.service

import com.google.gson.Gson
import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.DataRow
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.CSVDefinition
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.Field
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.Row
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RuleParserMDETest {

    lateinit var ruleParser: RuleParserMDE
    lateinit var ruleEvaluator: CalculatedFieldMDE
    val gson  = Gson()
    lateinit var metadata: Map<String, String>

    @Before
    fun init() {
        val fileDefContent = RuleParserMDETest::class.java.getResource("/DPRP_1.0.json").readText()
        val csvDef = gson.fromJson(fileDefContent, CSVDefinition::class.java)

        ruleParser = RuleParserMDE(csvDef)

        ruleEvaluator = CalculatedFieldMDE(ruleParser)
        metadata = mapOf("FIRST_SESSION" to "9/9/2020")

    }

    @Test
    fun testTreeParsing() {
        val rule = "(\$this < $$(\$METADATA_FIRST_SESSION + 6~MONTHS - 15~DAYS))"

        val tree = ruleParser.buildFullTree(rule)
        println(tree)
    }


    @Test
    fun testRemoveRuleFromMessage() {
        val row = Row(1, arrayOf(Field(1,"abc")))
        val message = "Core Sessions must have a date less than 6 months. (%%$$(\$METADATA_FIRST_SESSION + 6~MONTHS - 15~DAYS)%%). the second rule would be: %%$$(5+3) %%"
//        val regEx = "[A-Za-z0-9 .!]*%%([A-Za-z0-9 .!]*)%%[A-Za-z0-9 .!]*".toRegex()
       val rules = extractRules(message)
        println("rule: $rules")
        assert(rules.size == 2)

        rules.forEach {
            println("\t\trule: " + ruleEvaluator.calculateField(it, row, metadata))
        }
        println("\t\t\tnew message: " + replaceRules(message, row, metadata))

        val emptyRules = extractRules("THis message has no rules")
        println("emptyRUles: $emptyRules")
        assert(emptyRules.size == 0)
        println("\t\t\tnew message: " + replaceRules("THis message has no rules", row, metadata))

        val trickyRule = extractRules("This message does not have rules, but has a %% used here.")
        println("trickyRules: $trickyRule")
        assert(trickyRule.size == 0)
        println("\t\t\tnew message: " + replaceRules("This message does not have rules, but has a %% used here.", row, metadata))


        val singleRule = extractRules("I got a rule %%$$(19*3)%% here")
        println("single rule: $singleRule")
        assert(singleRule.size ==1 )
        singleRule.forEach {
            println("\t\trule: " + ruleEvaluator.calculateField(it, Row(1, arrayOf(Field(1,"abc"))), metadata))
        }
        println("\t\t\tnew message: " + replaceRules("I got a rule %%$$(19*3)%% here", row, metadata))
    }

    private fun replaceRules(message: String, row:Row, metadata: Map<String, String>): String {
        val rules = mutableListOf<String>()
        var newMessage = message
        var startIndex = message.indexOf("%%")
        if (startIndex > 0) {
            var secondIndex = message.indexOf("%%", startIndex + 2)
            if (secondIndex > 0) {
                newMessage = message.substring(0, startIndex)
                do {
                    val rule = message.substring(startIndex + 2, secondIndex)
                    newMessage += ruleEvaluator.calculateField(rule, row, metadata)
                    val tempIndex = secondIndex +2
                    startIndex = message.indexOf("%%", secondIndex + 2)
                    secondIndex = message.indexOf("%%", startIndex + 2)
                    if (startIndex > 0)
                        newMessage += message.substring(tempIndex, startIndex)
                    else newMessage += message.substring(tempIndex)
                } while (startIndex > 0)
            }
        }

        return newMessage
    }


    private fun extractRules(message: String): List<String> {
        val rules = mutableListOf<String>()
        var startIndex = message.indexOf("%%")
        if (startIndex > 0) {
            var secondIndex = message.indexOf("%%", startIndex + 2)
            if (secondIndex > 0)
                do {
                    rules.add(message.substring(startIndex + 2, secondIndex))
                    startIndex = message.indexOf("%%", secondIndex + 2)
                    secondIndex = message.indexOf("%%", startIndex + 2)
                } while (startIndex > 0)
        }

        return rules
    }
}