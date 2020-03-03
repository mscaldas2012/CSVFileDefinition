package gov.cdc.nccdphp.esurveillance.csvDefinition.service

import gov.cdc.nccdphp.esurveillance.csvDefinition.model.*
import gov.cdc.nccdphp.esurveillance.utils.StatisticsUtils
import gov.cdc.nccdphp.esurveillance.validation.model.FieldDefinition
import org.apache.juli.logging.LogFactory
import org.springframework.stereotype.Service
import java.lang.NumberFormatException
import java.text.ParseException
import java.text.SimpleDateFormat

@Service
class SummarizerService {
    companion object {
        val logger = LogFactory.getLog(SummarizerService::class.java)
    }

    fun summarize(config: CSVDefinition, content: String, skipheader: Int ): Summary {
        //Pivot the CSV to a Map of Columns and it's values.
        val pivot = pivot(content, skipheader, config)
        val summary = Summary(pivot.get(config.fields[0].name)!!.size)
        pivot.entries.forEach {
            val fieldDef = config.getFieldByName(it.key)

            val enumType = if (fieldDef.possibleAnswers == null)
                                COLUMN_TYPE.valueOf(fieldDef.type.toUpperCase())
                            else if ((fieldDef.rangeMin != null && fieldDef.rangeMin!! > 0.0) || (fieldDef.rangeMax != null && fieldDef.rangeMax!! > 0.0))
                                COLUMN_TYPE.valueOf(fieldDef.type.toUpperCase())
                           else COLUMN_TYPE.VALUESET

//TODO:: 2 - Handle Decimals
//TODO:: 3 - Add Numeric stats - AVG, STD, Quartiles, etc
//TODO:: 4 -
            val columnSummary = when (enumType) {
                COLUMN_TYPE.NUMERIC  ->  createNumberStats(it.value, fieldDef)
                COLUMN_TYPE.DATE     -> createDateStats(fieldDef, it.value)
                COLUMN_TYPE.STRING   -> ColumnStats.getColumnStats(enumType, it.value.filter{v -> !"".equals(v.trim())}.min()!!,  it.value.filter{v -> !"".equals(v.trim())}.max()!!, it.value.filter{v -> !"".equals(v.trim())}.distinct().count(), it.value.filter {v -> "".equals(v.trim())}.count())
                COLUMN_TYPE.VALUESET -> createValueSetStats(it.value)
            }
            summary.columns.put(fieldDef.name!!, columnSummary)
        }
        return summary

    }

    private fun createValueSetStats(columnValues: MutableList<String>): ValueSetStats {
        val filtered = columnValues.filter { v -> !"".equals(v.trim()) }
        val c = ValueSetStats(filtered.min()!!,
                filtered.max()!!,
                filtered.distinct().count(),
                columnValues.filter { v -> "".equals(v.trim()) }.count())
        c.valueSets = filtered.groupBy { it }.mapValues { it.value.count() }
        return c
    }

    private fun createDateStats(fieldDef: FieldDefinition, columnValues: MutableList<String>): ColumnStats {
        val dateFormat = SimpleDateFormat(fieldDef.format)
        val dateArray = columnValues.mapNotNull {
            try {
                dateFormat.parse(it)
            } catch (e: ParseException) {
                null
            }
        }
        val stats =  DateStats(dateArray.min().toString(),
                dateArray.max().toString(),
                dateArray.distinct().count(),
                columnValues.filter { v -> "".equals(v.trim()) }.count())
        stats.mode = StatisticsUtils.modeOf(dateArray)
        return stats

    }

    private fun createNumberStats(columnValues: MutableList<String>, fieldDef: FieldDefinition): NumberStats {
        val intArray = columnValues.mapNotNull {
            try {
                it.toInt()
            } catch (e: NumberFormatException) {
                0
            }
        }.filter { it >= fieldDef.rangeMin!! && it <= fieldDef.rangeMax!! }

        val stats = NumberStats(intArray.min().toString(),
                intArray.max().toString(),
                intArray.distinct().count(),
                columnValues.filter { v -> "".equals(v.trim()) }.count())
        stats.average = intArray.average()
        stats.sum = intArray.sum()
        stats.std = StatisticsUtils.stdDevOf(intArray)
        stats.mode = StatisticsUtils.modeOf(intArray)
        stats.range = StatisticsUtils.rangeOf(intArray)

        return stats
    }


    fun pivot(content: String, skipheader: Int, config: CSVDefinition): HashMap<String, MutableList<String>> {
        val pivot = hashMapOf<String, MutableList<String>>()
        content.split("\n").drop(skipheader).forEach { row ->
            row.split(",").forEachIndexed { i, e ->
                val column = config.fields.get(i).name!!
                val a = pivot.getOrDefault(column, mutableListOf())
                a.add(e)
                pivot.put(column, a)
            }
        }
        return pivot
    }
}