package gov.cdc.nccdphp.esurveillance.csvDefinition.repository

import gov.cdc.nccdphp.esurveillance.csvDefinition.model.ValueSet
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

/**
 *
 *
 * @Created - 2019-04-24
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@Repository
interface ValueSetMongoRepo: ReactiveMongoRepository<ValueSet, String>