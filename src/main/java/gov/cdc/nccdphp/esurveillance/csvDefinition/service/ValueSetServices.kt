package gov.cdc.nccdphp.esurveillance.csvDefinition.service

import gov.cdc.nccdphp.esurveillance.csvDefinition.model.ValueSet
import gov.cdc.nccdphp.esurveillance.csvDefinition.repository.ValueSetMongoRepo
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 *
 *
 * @Created - 2019-04-24
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@Service
class ValueSetServices(val repo: ValueSetMongoRepo) {

    fun getValueSetsAsMap(): Mono<MutableMap<String, ValueSet>> {
        val flux = repo.findAll()

        return flux.collectMap(
                        { item -> item.name },
                        { item -> item})
                //.block()
    }

    fun getValueSets(): Flux<ValueSet> {
        return repo.findAll()
    }
}