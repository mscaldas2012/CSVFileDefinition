package gov.cdc.nccdphp.esurveillance.rest.security

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest//(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = ["classpath:application-test.yml"])
class S2SAuthTest {
    companion object {
        const val VALID_TOKEN = "unittest-token"
    }

    @Autowired
    private lateinit var s2sAuth: S2SAuth

//    @Before
//    fun setUp() {
//        val config = S2SAuthConfig()
//        config.token= VALID_TOKEN
//        s2sAuth = S2SAuth(config)
//    }

    @Test
    fun checkS2SCredentialsValid() {
        s2sAuth.checkS2SCredentials(VALID_TOKEN)
        assertTrue("Token successfully validated", true)
    }

    @Test
    fun checkS2SCredentialsInvalid() {
        try {
            s2sAuth.checkS2SCredentials("invalidtoken")
            assertFalse("Exception supposed to be thrown!", false)
        } catch (e: ServiceNotAuthorizedException) {
            assertTrue("Exception properly thrown: ${e.message} ", true )
        }
    }


}