package gov.cdc.nccdphp.esurveillance.data

import gov.cdc.nccdphp.esurveillance.csvDefinition.model.AnswerChoice
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.ValueSet
import gov.cdc.nccdphp.esurveillance.csvDefinition.repository.ValueSetMongoRepo
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

/**
 * @Created - 7/23/18
 * @Author Marcelo Caldas mcq1@cdc.gov
 */

@Component
class DataLoader {
    @Autowired
    internal var valueSetRepo: ValueSetMongoRepo? = null

    private val log = LogFactory.getLog(DataLoader::class.java)

    fun loadDataSets(content: String) {
        //InputStream file = getClass().getClassLoader().getResourceAsStream(filename);
        Scanner(content).use { scanner ->
            scanner.nextLine() //skip first line
            //Create MMG ->
            while (scanner.hasNextLine()) {
                val line = scanner.nextLine()
                val values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()


                val valueSet = ValueSet(values[0], emptyList<AnswerChoice>().toMutableList())
                for (i in 1 until values.size) { //Add all possible value Sets:
                    val vs_code = values[i].split("\\^".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    valueSet.addChoice(vs_code[0], vs_code[1])
                }
                valueSetRepo!!.save(valueSet)
            }
        }
    }

    private fun getDoubleValue(value: String?): Double {
        return if (value == null || value.trim { it <= ' ' }.length == 0) {
            0.0
        } else {
            value.toDouble()
        }
    }
}
