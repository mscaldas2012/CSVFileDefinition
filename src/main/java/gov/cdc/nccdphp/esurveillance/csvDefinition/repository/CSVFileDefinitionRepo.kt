package gov.cdc.nccdphp.esurveillance.csvDefinition.repository

import gov.cdc.nccdphp.esurveillance.csvDefinition.model.CSVDefinition

interface CSVFileDefinitionRepo {
    fun findByCodeAndVersion(code: String, version: String): CSVDefinition
    fun save(def: CSVDefinition): CSVDefinition
}