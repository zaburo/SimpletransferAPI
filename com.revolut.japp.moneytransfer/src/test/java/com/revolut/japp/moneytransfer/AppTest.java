package com.revolut.japp.moneytransfer;

import static com.jayway.restassured.RestAssured.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.RestAssured;

import static org.hamcrest.Matchers.equalTo;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @BeforeClass
    public static void configureRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 4444;
    }

    @AfterClass
    public static void unconfigureRestAssured() {
        RestAssured.reset();
    }

    @Test
    public void retrieveAllAccounts() {
        final int id = get("/api/accounts").then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath().getInt("find { it.balance==1111 }.id");
        get("/api/accounts/" + id).then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("name", equalTo("Yuanwen Li"))
                .body("currency", equalTo("EUR"));
    }

    @Test
    public void successRetrieveOneAccount() {
        get("/api/accounts/0").then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(0))
                .body("name", equalTo("Yuanwen Li"))
                .body("currency", equalTo("EUR"));
    }

    @Test
    public void failRetrieveOneAccount() {
        get("/api/accounts/999").then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void successAddAccount() {
        given().body("{\n" +
                "    \"name\": \"AAA\",\n" +
                "    \"balance\": \"50000\",\n" +
                "    \"currency\": \"GBP\"\n" +
                "}")
                .when()
                .post("api/accounts")
                .then()
                .assertThat()
                .statusCode(201);
    }

    @Test
    public void failAddAccount() {
        given().body("{\n" +
                "    \"name\": \"AAA\",\n" +
                "    \"balance\": \"50000\",\n" +
                "    \"currency\": \"reegrdf\"\n" +
                "}")
                .when()
                .post("api/accounts")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void successUpdateAccount() {
        given().body("{\n" +
                "    \"balance\": \"50000\",\n" +
                "    \"currency\": \"EUR\"\n" +
                "}")
                .when()
                .put("api/accounts/0")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(0))
                .body("name", equalTo("YUANWEN LI"))
                .body("balance", equalTo(50000))
                .body("currency", equalTo("EUR"));
    }

    @Test
    public void failUpdateAccount() {
        given().body("{\n" +
                "    \"currency\": \"FEAQWFW\"\n" +
                "}")
                .when()
                .put("api/accounts/0")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void successDeleteOneAccount() {
        delete("/api/accounts/0").then()
                .assertThat()
                .statusCode(204);
        get("/api/accounts/0").then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void failDeleteOneAccount() {
        delete("/api/accounts/999").then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void retrieveAllTransfers() {
        final int id = get("/api/transfers").then()
                .assertThat()
                .statusCode(200)
                .extract()
                .jsonPath().getInt("find { it.amount==650 }.id");
        get("/api/transfers/" + id).then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("fromAccountId", equalTo(0))
                .body("toAccountId", equalTo(1))
                .body("amount", equalTo(650))
                .body("currency", equalTo("EUR"))
                .body("comment", equalTo("Rent"));
    }

    @Test
    public void successRetrieveOneTransfer() {
        get("/api/transfers/0").then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(0))
                .body("fromAccountId", equalTo(0))
                .body("toAccountId", equalTo(1))
                .body("amount", equalTo(650))
                .body("currency", equalTo("EUR"))
                .body("comment", equalTo("Rent"));
    }

    @Test
    public void testRetrieveOneTransferFail() {
        get("/api/transfers/999").then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void successAddTransfer() {
        given().body("{\n" +
                "    \"fromAccountId\": \"1\",\n" +
                "    \"toAccountId\": \"0\",\n" +
                "    \"amount\": \"1000\",\n" +
                "    \"currency\": \"USD\",\n" +
                "    \"comment\": \"test transfer\"\n" +
                "}")
                .when()
                .post("api/transfers")
                .then()
                .assertThat()
                .statusCode(201);
    }

    @Test
    public void failAddTransfer() {
        given().body("{\n" +
                "    \"fromAccountId\": \"1\",\n" +
                "    \"toAccountId\": \"0\",\n" +
                "    \"amount\": \"1000\",\n" +
                "    \"currency\": \"UfefSD\",\n" +
                "    \"comment\": \"test transfer\"\n" +
                "}")
                .when()
                .post("api/transfers")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void successUpdateTransfer() {
        put("api/transfers/0")
                .then()
                .assertThat()
                .body("status", equalTo("SUCCEED"));
    }

    @Test
    public void failUpdateTransfer() {
        put("api/transfers/1")
                .then()
                .assertThat()
                .body("status", equalTo("FAILED"));
    }
}
