package gov.cdc.nccdphp.esurveillance.csvDefinition.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.CSVDefinition

/**
 *
 *
 * @Created - 2019-04-24
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@Repository
interface CSVDefinitionMongoRepo: MongoRepository<CSVDefinition, String>, CSVFileDefinitionRepo {
    override fun findByCodeAndVersion(code: String, version: String): CSVDefinition
}