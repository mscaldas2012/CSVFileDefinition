package gov.cdc.nccdphp.esurveillance.csvDefinition.model

import gov.cdc.nccdphp.esurveillance.validation.model.FileDefinition
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document

/**
 *
 *
 * @Created - 2019-04-24
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
@Document(collection="file_definition")
@CompoundIndex(def = "{'code':1, 'version':-1}", name = "mmg_code_version_index", unique = true)
class CSVDefinition(@Id var id: String, val code: String, val version: String): FileDefinition() {
    var name: String? = null

}