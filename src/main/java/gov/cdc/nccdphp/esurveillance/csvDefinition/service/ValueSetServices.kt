package gov.cdc.nccdphp.esurveillance.csvDefinition.service

import gov.cdc.nccdphp.esurveillance.csvDefinition.model.ValueSet
import gov.cdc.nccdphp.esurveillance.csvDefinition.repository.ValueSetMongoRepo
import org.springframework.stereotype.Service
import java.util.stream.Collectors

/**
 *
 *
 * @Created - 2019-04-24
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@Service
class ValueSetServices(val repo: ValueSetMongoRepo) {

    fun getValueSetsAsMap(): MutableMap<String, ValueSet> {
        val list = repo.findAll()

        return list.stream().collect(Collectors.toMap<ValueSet, String, ValueSet>({ item -> item.name }, { item -> item }))
    }

    fun getValueSets(): List<ValueSet> {
        return repo.findAll()
    }
}