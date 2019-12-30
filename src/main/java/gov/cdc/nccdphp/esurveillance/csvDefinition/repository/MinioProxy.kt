package gov.cdc.nccdphp.esurveillance.csvDefinition.repository

import io.minio.MinioClient
import io.minio.errors.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException

/**
 *
 *
 * @Created - 2019-02-22
 * @Author Marcelo Caldas mcq1@cdc.gov
 */
//@Service
class MinioProxy (@Value("\${minio.url}") val minioURL: String,
                  @Value("\${minio.accesskey}") val minioAccessKey: String,
                  @Value("\${minio.secret}") val minioSecret: String,
                  @Value("\${minio.default-bucket}") var bucketName: String) {
    companion object {
        private val LOG = LoggerFactory.getLogger(MinioProxy::class.java)
    }
    private var minioClient: MinioClient? = null

     init {
        try {
            LOG.info("minioURL = ${minioURL}")
            minioClient = MinioClient(minioURL, minioAccessKey, minioSecret)
            // Check if the bucket already exists.
            val isExist = minioClient!!.bucketExists(bucketName)
            if (isExist) {
                println("Bucket exists.")
            } else {
                // Make a new bucket called asiatrip to hold a zip file of photos.
                minioClient!!.makeBucket(bucketName)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getMinioClient(): MinioClient? {
        return this.minioClient
    }

    @Throws(IOException::class, InvalidKeyException::class, NoSuchAlgorithmException::class, InsufficientDataException::class, InvalidArgumentException::class, InternalException::class, NoResponseException::class, InvalidBucketNameException::class, XmlPullParserException::class, ErrorResponseException::class)
    fun getObjectAsInputStream(file: String): InputStream {
        return this.minioClient!!.getObject(this.bucketName, file)
    }

    @Throws(IOException::class, InvalidKeyException::class, NoSuchAlgorithmException::class, InsufficientDataException::class, InvalidArgumentException::class, InternalException::class, NoResponseException::class, InvalidBucketNameException::class, XmlPullParserException::class, ErrorResponseException::class)
    fun getObject(file: String): String {
        val inputStream = this.minioClient!!.getObject(this.bucketName, file)

        val inputString = inputStream.bufferedReader().use { it.readText() }
        return inputString
    }

    fun listObjects(prefix: String): List<String> {
        var list=  this.minioClient!!.listObjects(this.bucketName, prefix)
        return list.map{ it.get().objectName()}
    }


    @Throws(IOException::class, InvalidKeyException::class, NoSuchAlgorithmException::class, InsufficientDataException::class, InvalidArgumentException::class, InternalException::class, NoResponseException::class, InvalidBucketNameException::class, XmlPullParserException::class, ErrorResponseException::class)
    fun upload (fileName: String, content: InputStream) {
        this.minioClient!!.putObject(bucketName, fileName, content, "application/xml")
    }

    fun deleteObject(objectName: String) {
        minioClient!!.removeObject(this.bucketName, objectName)
    }

//    fun getBucketName(): String? {
//        return bucketName
//    }
}