package gov.cdc.nccdphp.esurveillance.csvDefinition.service

import gov.cdc.nccdphp.esurveillance.csvDefinition.model.CSVDefinition
import gov.cdc.nccdphp.esurveillance.csvDefinition.repository.CSVDefinitionMongoRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 *
 *
 * @Created - 2019-04-24
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@Service
class CSVDefinitionService {

    @Autowired
    lateinit var repo: CSVDefinitionMongoRepo

    fun save(mdeDef: CSVDefinition): CSVDefinition {
        return repo.save(mdeDef)
    }

    fun getFileDefinition(code: String, version: String): CSVDefinition {
        return repo.findByCodeAndVersion(code, version)
    }
}