package gov.cdc.nccdphp.esurveillance.csvDefinition.service

import gov.cdc.nccdphp.esurveillance.bizRulesEngine.model.DataField
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.CSVFile
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.Field
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.Row
import org.springframework.stereotype.Service


/**
 *
 *
 * @Created - 2019-04-24
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@Service
class TransformerService(private val definitionService: CSVDefinitionService) {

    fun parseContentAsJson(defCode: String, version: String, content: String, includesHeader: Boolean = true): String {
        val def = definitionService.getFileDefinition(defCode, version)
        var result = ""
        val skipheader = if (includesHeader) 1 else 0

        content.split("\n").drop(skipheader).forEach { row ->
            var i = 0
            val json = Json {
                row.split(",").forEach { field ->
                    when (def.fields[i].type) {
                        "Numeric" -> def.fields[i++].name!! asInt field
                        else -> def.fields[i++].name!! to field
                    }

                }
            }
            if (result.isNotEmpty())
                result += ","
            result += json
        }
        return result
    }

    fun parseContentAsCSVFile(content: String, includesHeader: Boolean = true): CSVFile {
        val result = CSVFile("file")
        var rowNumber = 1
        //val rows = mutableListOf<DataRow>()
        val skipheader = if (includesHeader) 1 else 0

        content.split("\n").drop(skipheader).forEach { row ->

            val fields = mutableListOf<DataField>()
            var fieldNumber = 1
            row.split(",").forEach {
                val field = Field(fieldNumber++ , it)
                fields += field

            }
            val rowdata = Row(rowNumber++, fields.toTypedArray()  )
            result.addRow( rowdata)
        }
        return result
    }

    fun generateContent(defCode: String, version: String, file: CSVFile): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class Json() {

    private val json = org.json.JSONObject()

    constructor(init: Json.() -> Unit) : this() {
        this.init()
    }

    infix fun String.to(value: Json) {
        json.put(this, value.json)
    }
    infix fun String.asInt(value: String) {
        val intvalue = value.trim().replace("\r","").toInt()
        json.put(this, intvalue)
    }

    infix fun <T> String.to(value: T) {
        json.put(this, value)
    }

    override fun toString(): String {
        return json.toString()
    }
}