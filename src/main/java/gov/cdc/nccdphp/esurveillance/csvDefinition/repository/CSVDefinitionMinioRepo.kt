package gov.cdc.nccdphp.esurveillance.csvDefinition.repository

import com.google.gson.Gson
import gov.cdc.nccdphp.esurveillance.csvDefinition.model.CSVDefinition
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream

//@Component
class CSVDefinitionMinioRepo(val minioProxy: MinioProxy): CSVFileDefinitionRepo {
    val gson = Gson()
    override fun save(def: CSVDefinition): CSVDefinition {
        val content = gson.toJson(def)
        minioProxy.upload(def.code +"_"+ def.version, ByteArrayInputStream(content.toByteArray(Charsets.UTF_8)))
        return findByCodeAndVersion(def.code, def.version)
    }

    override fun findByCodeAndVersion(code: String, version: String): CSVDefinition {
        val fileDefContent = minioProxy.getObject(code + "_" + version)
        return gson.fromJson(fileDefContent, CSVDefinition::class.java)
    }
}