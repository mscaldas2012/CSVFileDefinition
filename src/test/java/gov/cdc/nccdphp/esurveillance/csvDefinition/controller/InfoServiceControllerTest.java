package gov.cdc.nccdphp.esurveillance.csvDefinition.controller;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

/**
 * Created by marcelo on 10/4/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations="classpath:application-test.yml")
public class InfoServiceControllerTest {

    @Value("${server.port}")
    private int _port ;

    @Value("${server.context-path}")
    private String contextPath;

    private String rootAPIIURL;

    @Before
    public void setUp()  {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = _port;

        this.rootAPIIURL =  contextPath + "/v1/";

    }

    @Test
    public void about() {
        when().
                get(this.rootAPIIURL + "info/about").
        then().
                statusCode(200).
                body("summary", containsString("Spring REST RENAME_ME"));
    }

    @Test
    public void ping()  {
        when()
                .get(this.rootAPIIURL + "info/ping")
          .then()
                .statusCode(200)
                .body(containsString ("Hello There! I'm alive."));
    }

    @Test
    public void config()  {
        when()
                .get(this.rootAPIIURL + "info/config")
          .then()
                .statusCode(200)
                .body(containsString ("defaultPageSize"));
    }
}

