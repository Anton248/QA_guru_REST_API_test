package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelenoidTests {
    /*
    1. Make request to https://selenoid.autotests.cloud/status
    2. Get response { total: 20, used: 0, queued: 0, pending: 0, browsers: ...
    3. Check total is 20
     */

    @Test
    void testTotal() {
        when().
                get("https://selenoid.autotests.cloud/status").
        then().
                body("total", is(20));
    }

    @Test
    void testTotalWithLogs() {
        given().log().uri().log().body().
        when().
                get("https://selenoid.autotests.cloud/status").
        then().log().all().
                statusCode(200).
                body("total", is(20));
    }

    @Test
    void testTotalAndChromeVersion() {
        given().log().uri().log().body().
        when().
                get("https://selenoid.autotests.cloud/status").
        then().log().all().
                statusCode(200).
                body("total", is(20)).
                body("browsers.chrome", hasKey("100.0"));
    }

    @Test
    void testTotalResponse() {

        Integer expectedTotal = 20;

        Integer actualTotal = given().log().uri().log().body().
                when().
                get("https://selenoid.autotests.cloud/status").
                then().log().all().
                statusCode(200).
                extract().path("total");

        assertEquals(expectedTotal, actualTotal);

    }

    @Test
    void testWithJsonScheme() {

                given().
                        log().uri().
                        log().body().
                when().
                        get("https://selenoid.autotests.cloud/status").
                then().
                        log().status().
                        log().body().
                        statusCode(200).
                        body(matchesJsonSchemaInClasspath("schemes/status-response-scheme.json")).
                        body("total", is(20)).
                        body("browsers.chrome", hasKey("100.0"));


    }
}
