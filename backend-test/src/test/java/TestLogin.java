
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static    org.hamcrest.Matchers.*;



public class TestLogin {

    @Before
    public void setup() {
        RestAssured.baseURI = "https://o1xc5igurl.execute-api.eu-central-1.amazonaws.com/Stage";
    }

 /*   @Test
    public void TestLogin() {
        given().urlEncodingEnabled(true)
                .param("name", "test")
                .param("password", "1234")
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .post("/login")
                .then().statusCode(200);
    }*/

    @Test
    public void TestCreateUser() {

        //TODO Fix iether remove the user before test, or fix random user name

        String payload = "{\"name\": \"test20\",\"password\": \"1234\"}";

        given().contentType(ContentType.JSON)
                .body(payload)
                .post("/registered-users")
                .then()
                .statusCode(201)
                .extract()
                .response().body().equals("Created");
    }


}
