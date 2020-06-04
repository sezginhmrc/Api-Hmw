package homework.uiNamesApi;

import homework.pojos.UIPojo;
import homework.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;


import java.util.*;

import static io.restassured.RestAssured.*;

public class RandomNamesTestCases {

    @BeforeAll
    public static void setup() {

        baseURI = ConfigurationReader.getProperty("UINAMES.KEY");

    }

    /*1 No param Test request without providing any parameters
    2. Verify status code 200, content type application/json; charset=utf-8
    3. Verify that name, surname, gender, region fields have value
    */
    @Test
    @DisplayName("No param Test")
    public void verifyingResposne() {

        Response response = get().prettyPeek();

        response.
                then().
                assertThat().statusCode(200).
                assertThat().contentType(ContentType.JSON).
                and().
                body("name", notNullValue()).
                body("surname", notNullValue()).
                body("gender", notNullValue()).
                body("region", notNullValue());
    }

    /*Gender test
1. Create a request by providing query parameter: gender, male or female
2. Verify status code 200, content type application/json; charset=utf-8
3. Verify that value of gender field is same from step 1*/

    @Test
    @DisplayName("Gender Test")
    public void genderTest() {


        Response response =
                given().
                        queryParams("gender", "male").
                        when().get().prettyPeek();

        response.then().assertThat().statusCode(200).
                and().
                contentType("application/json; charset=utf-8").
                and().
                body("gender", is("male"));

    }

    /*2 params test
1. Create a request by providing query parameters: a valid region and gender NOTE: Available region values are given in the documentation
2. Verify status code 200, content type application/json; charset=utf-8
3. Verify that value of gender field is same from step 1
4. Verify that value of region field is same from step 1*/

    @Test
    @DisplayName("2 Params Test")
    public void twoParamTest() {

        Response response =
                given().
                        queryParams("gender", "male").
                        queryParams("region", "Turkey").
                        when().get().prettyPeek();

        response.then().assertThat().body("gender", is("male")).
                assertThat().body("region", is("Turkey"));

    }
    /*Invalid gender test
1. Create a request by providing query parameter: invalid gender
2. Verify status code 400 and status line contains Bad Request
3. Verify that value of error field is Invalid gender
*/

    @Test
    @DisplayName("ivalid gender Test")
    public void negativeGenderValidation() {

        Response response =
                given().
                        queryParams("gender", "invalid").
                        when().get().prettyPeek();

        response.
                then().assertThat().
                statusCode(400).
                and().
                statusLine(containsString("Bad Request")).
                and().
                body("error", is("Invalid gender"));

    }

    /*Invalid region test
1. Create a request by providing query parameter: invalid region
2. Verify status code 400 and status line contains Bad Request
3. Verify that value of error field is Region or language not found*/

    @Test
    @DisplayName("invalid region Test")
    public void negativeScenarioForRegion() {

        Response response =
                given().
                        queryParams("region", "invalid").
                        when().get().prettyPeek();

        response.
                then().assertThat().
                statusCode(400).
                and().
                statusLine(containsString("Bad Request")).
                and().
                body("error", is("Region or language not found"));

    }

    /*Amount and regions test
1. Create request by providing query parameters: a valid region and amount (must be bigger than 1)
2. Verify status code 200, content type application/json; charset=utf-8
3. Verify that all objects have different name+surname combination
*/
    @Test
    @DisplayName("Amount and region Test")
    public void verifyingObjectsCombination() {


        Response response =
                given().
                        queryParams("region", "Germany").
                        queryParams("amount", 25).
                        when().get().prettyPeek();


        response.then().assertThat().
                statusCode(200).
                and().
                header("Content-Type","application/json; charset=utf-8").
                header("Server","Apache").
                header("Access-Control-Allow-Methods","GET");


        List<UIPojo> userList = response.jsonPath().getList("", UIPojo.class);
        // We use "" if we dont have any collection (array) name in the response.
        System.out.println("UserList:: "+userList);

        Assertions.assertEquals(25,userList.size());
        Set<String> fullNames =  new HashSet<>();
        for (UIPojo user : userList) {
            String fullName = user.getName() + " " +user.getSurname();
            fullNames.add(fullName);
        }

        // Verify that all objects have different name+surname combination
        // fullNames is an Set collection not allowed duplicates.

        response.then().assertThat().body("size",is(fullNames.size()));
    }



    /*3 params test
1. Create a request by providing query parameters: a valid region, gender and amount (must be bigger than 1)
2. Verify status code 200, content type application/json; charset=utf-8
3. Verify that all objects the response have the same region and gender passed in step 1*/

    @Test
    @DisplayName("3 Params Test")
    public void verifyThreeParamsTest() {


        Response response =
                given().
                        queryParams("region", "Germany").
                        queryParams("amount", 50).
                        queryParams("gender", "female").
                        when().get().prettyPeek();

        response.then().assertThat().
                statusCode(200).
                and().
                contentType(" application/json; charset=utf-8").
                body("size()",is(50)).
                body("gender",everyItem(is("female"))).
                body("region",everyItem(is("Germany")));



        // 2nd solution
        List<UIPojo> list = response.jsonPath().getList("", UIPojo.class);

        //Verify that all objects the response have the same region
        for (UIPojo lst : list) {
            Assertions.assertEquals("Germany", lst.getRegion());
            Assertions.assertEquals("female", lst.getGender());
        }
    }

        /*Amount count test
1. Create a request by providing query parameter: amount (must be bigger than 1)
2. Verify status code 200, content type application/json; charset=utf-8
3. Verify that number of objects returned in the response is same as the amount passed in step 1*/

        @Test
        @DisplayName("Amount count test")
        public void verifyAmount(){

            Response response =
                    given().
                            queryParams("amount", 50).
                            when().get().prettyPeek();

            response.then().assertThat().
                    statusCode(200).
                    and().
                    contentType(" application/json; charset=utf-8").
                    body("size()",is(50));


        }
}

