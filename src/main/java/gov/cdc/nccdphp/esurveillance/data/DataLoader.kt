package gov.cdc.nccdphp.esurveillance.data

import com.google.gson.Gson
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.CSVDefinition
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.ValueSet
import gov.cdc.nccdphp.esurveillance.csvDefinition.repository.ValueSetMongoRepo
import gov.cdc.nccdphp.esurveillance.csvDefinition.service.CSVDefinitionService
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

/**
 * @Created - 7/23/18
 * @Author Marcelo Caldas mcq1@cdc.gov
 */

@Component
class DataLoader(val valueSetRepo: ValueSetMongoRepo, val fileDefService: CSVDefinitionService) {

    private val log = LogFactory.getLog(DataLoader::class.java)

    fun loadDataSets(content: String) {
        //InputStream file = getClass().getClassLoader().getResourceAsStream(filename);
        Scanner(content).use { scanner ->
            scanner.nextLine() //skip first line
            //Create MMG ->
            while (scanner.hasNextLine()) {
                val line = scanner.nextLine()
                val values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()


                val valueSet = ValueSet(values[0])
                for (i in 1 until values.size) { //Add all possible value Sets:
                    val vsCode = values[i].split("\\^".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    valueSet.addChoice(vsCode[0], vsCode[1])
                }
                valueSetRepo.save(valueSet)
            }
        }
    }

    fun loadDefinition(content: String) {
        val gson = Gson()
        val newDef =gson.fromJson(content, CSVDefinition::class.java)
        fileDefService.save(newDef)

    }
}
