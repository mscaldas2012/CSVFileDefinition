package gov.cdc.nccdphp.esurveillance.csvDefinition.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import java.util.*
import kotlin.collections.HashMap

data class Summary(val numberOfRows: Int) {
    val columns =  HashMap<String, ColumnStats>()

}

enum class COLUMN_TYPE {
    NUMERIC, STRING, DATE, VALUESET
}
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    Type(value = NumberStats::class, name = "numeric"),
    Type(value = DateStats::class, name = "date"),
    Type(value = ValueSetStats::class, name = "valueset"),
    Type(value = StringStats::class, name = "string")
)
abstract class ColumnStats(open val min: String, open val max: String, open val unique: Int, open val empty: Int) {
    companion object {
        fun getColumnStats(type: COLUMN_TYPE, min: String, max: String, unique: Int, empty: Int): ColumnStats {
            when (type) {
                COLUMN_TYPE.NUMERIC  -> return NumberStats(min, max, unique, empty)
                COLUMN_TYPE.DATE     -> return DateStats(min, max, unique , empty)
                COLUMN_TYPE.STRING   -> return StringStats( min, max, unique, empty)
                COLUMN_TYPE.VALUESET -> return ValueSetStats(min, max, unique, empty)
             }
        }
    }
}

//@JsonSubTypes.Type(value=NumberStats::class, name="Numeric")
@JsonTypeName("numeric")
data class NumberStats(override val min: String,
                       override val max: String,
                       override val unique: Int,
                       override val empty: Int):ColumnStats(min, max, unique, empty) {
    var average: Double = 0.0
    var sum: Int = 0
    var std: Double = 0.0
    lateinit var mode: List<Int>
    var range: Int = 0

}
//@JsonSubTypes.Type(value=DateStats::class, name="Date")
@JsonTypeName("date")
data class DateStats(override val min: String,
                     override val max: String,
                     override val unique: Int,
                     override val empty: Int): ColumnStats( min, max, unique, empty) {
    var average: Date? = null
    @JsonFormat(pattern="yyyy-MM-dd")
    lateinit var mode: List<Date>

}

//@JsonSubTypes.Type(value=ValueSetStats::class, name="ValueSet")
@JsonTypeName("valueset")
data class ValueSetStats(override val min: String,
                         override val max: String,
                         override val unique: Int,
                         override val empty: Int): ColumnStats( min, max, unique,empty ) {
    var valueSets:Map<String, Int> = HashMap()
}

//@JsonSubTypes.Type(value=StringStats::class, name="String")
@JsonTypeName("string")
data class StringStats(override val min: String,
                       override val max: String,
                       override val unique: Int,
                       override val empty: Int): ColumnStats( min, max, unique, empty)

