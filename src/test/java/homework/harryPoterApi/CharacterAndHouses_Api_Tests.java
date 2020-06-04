package homework.harryPoterApi;

import homework.utilities.ConfigurationReader;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.config;
import static org.hamcrest.Matchers.*;

public class CharacterAndHouses_Api_Tests {

  public static String apiKey = ConfigurationReader.getProperty("API.KEY");

    @BeforeAll
    public static void beforeAll() {
        baseURI = ConfigurationReader.getProperty("POTTERAPI.URI");
        config = config().objectMapperConfig(new ObjectMapperConfig(ObjectMapperType.GSON));


    }

    /*
  Verify bad key
1. Send a get request to /characters. Request includes : • Header Accept with value application/json
• Query param key with value invalid
2. Verify status code 401, content type application/json; charset=utf-8
3. Verify response status line include message Unauthorized
4. Verify that response body says "error": "API Key Not Found
*
* */
    @Test
    @DisplayName("Verify bad key")
    public void verifyApiKey(){

        Response response =
                given().accept(ContentType.JSON).
                            queryParam("key","invali").
                 when().get("/characters");

        response.
                then().
                statusCode(401).
                contentType("application/json; charset=utf-8").
                body("error",is("API Key Not Found"));

    }
/*
Verify no key
1. Send a get request to /characters. Request includes : • Header Accept with value application/json
2. Verify status code 409, content type application/json; charset=utf-8
3. Verify response status line include message Conflict
4. Verify that response body says "error": "Must pass API key for request"
}*/
    @Test
    @DisplayName("Verify No key")
    public void verifyNoKey(){

                given().accept(ContentType.JSON).
                when().get("/characters").
                then().
                statusCode(409).
                and().assertThat().
                contentType("application/json; charset=utf-8").
                and().assertThat().
                statusLine(containsString("Conflict")).
                and().assertThat().
                body("error",is("Must pass API key for request"));

    }
/*Verify numbe
r of characters
1. Send a get request to /characters. Request includes : • Header Accept with value application/json
• Query param key with value {{apiKey}}
2. Verify status code 200, content type application/json; charset=utf-8
3. Verify response contains 194 characters*/
    @Test
    @DisplayName("Verify number of chars")
    public void verifyNumberOfCharacters(){

        Response response =
                given().accept(ContentType.JSON).log().all().
                        queryParam("key",apiKey).
                 when().get("/characters").prettyPeek();

        response.then().assertThat().statusCode(200).contentType(ContentType.JSON);

        List<Character> listChar = response.jsonPath().getList("");
        Assertions.assertEquals(194,listChar.size());

    }


/*Verify number of character id and house
1. Send a get request to /characters. Request includes : • Header Accept with value application/json
• Query param key with value {{apiKey}}
2. Verify status code 200, content type application/json; charset=utf-8
3. Verify all characters in the response have id field which is not empty
4. Verify that value type of the field dumbledoresArmy is a boolean in all characters in the response
5. Verify value of the house in all characters in the response is one of the following:
"Gryffindor", "Ravenclaw", "Slytherin", "Hufflepuff"*/

    @Test
    @DisplayName("Verify number of char id and house")
    public void verifyNumberCharAndHouse(){

        Response response =
                given().accept(ContentType.JSON).
                        queryParams("key",apiKey).
                when().get(EndPoints.getChar);

        response.then().
                assertThat().statusCode(200).
                assertThat().contentType("application/json; charset=utf-8").
                //3. Verify all characters in the response have id field which is not empty
                body("id",everyItem(is(notNullValue()))).
                body("dumbledoresArmy",everyItem(is(instanceOf(Boolean.class)))).
                body("house",everyItem(is(oneOf("Gryffindor","Ravenclaw","Slytherin","Hufflepuff",null))));





    }

/*Verify all character information
1. Send a get request to /characters. Request includes : • Header Accept with value application/json
• Query param key with value {{apiKey}}
2. Verify status code 200, content type application/json; charset=utf-8
3. Select name of any random character
4. Send a get request to /characters. Request includes :
• Header Accept with value application/json • Query param key with value {{apiKey}}
• Query param name with value from step 3
5. Verify that response contains the same character information from step 3. Compare all fields.*/

    @Test
    @DisplayName("verify all characters information")
    public void verifyAllCharsInfo(){

        Response response =
                given().accept(ContentType.JSON).
                        queryParams("key",apiKey).
                when().get(EndPoints.getChar).prettyPeek();
        
        response.then().assertThat().statusCode(200).
                        assertThat().contentType("application/json; charset=utf-8");

        List<Map<String,String>>allCharacters = response.jsonPath().getList("");
        System.out.println("allCharacters = " + allCharacters);
        int randomCharater = new Random().nextInt(allCharacters.size());
        String anyName = allCharacters.get(randomCharater).get("name");
        System.out.println("anyName = " + anyName);

        Response response2 =
                given().
                        header("Accept","application/json").
                        queryParams("key",apiKey).
                        queryParam("name",anyName).
                        when().
                        get("/characters").prettyPeek();
        response2.then().
                assertThat().
                body("[0].name",is(anyName));

    }

/*Verify name search
1. Send a get request to /characters. Request includes : • Header Accept with value application/json
• Query param key with value {{apiKey}}
• Query param name with value Harry Potter
2. Verify status code 200, content type application/json; charset=utf-8
3. Verify name Harry Potter

4. Send a get request to /characters. Request includes :
• Header Accept with value application/json • Query param key with value {{apiKey}}
• Query param name with value Marry Potter
5. Verify status code 200, content type application/json; charset=utf-8
6. Verify response body is empty
*/
    @Test
    @DisplayName("Verify name search")
    public void verifyNameSearch(){

        Response response =
                given().
                        header("Accept","application/json").
                        queryParams("key",apiKey).
                        queryParams("name", "Harry Potter").
                        when().
                        get("/characters").prettyPeek();
        response.then().assertThat().
                statusCode(200).
                contentType("application/json; charset=utf-8").
                body("[0].name",is("Harry Potter"));
        Response response2 =
                given().
                        header("Accept","application/json").
                        queryParams("key",apiKey).
                        queryParams("name", "Marry Potter").
                        when().
                        get("/characters").prettyPeek();
        response2.then().assertThat().
                statusCode(200).
                contentType("application/json; charset=utf-8").
                body("[0]",isEmptyOrNullString()).
                body("size()",is(0));

    }
/*
*Verify house members
1. Send a get request to /houses. Request includes : • Header Accept with value application/json
• Query param key with value {{apiKey}}
2. Verify status code 200, content type application/json; charset=utf-8
3. Capture the id of the Gryffindor house
4. Capture the ids of the all members of the Gryffindor house
5. Send a get request to /houses/:id. Request includes :
• Header Accept with value application/json • Query param key with value {{apiKey}}
• Path param id with value from step 3
6. Verify that response contains the same member ids as the step 4 */

    @Test
    @DisplayName("Verify House members")
    public void HouseMembers (){

        Response response =
                given().
                        header("Accept","application/json").
                        queryParams("key",apiKey).
                        when().
                        get("/houses").prettyPeek();
        response.then().assertThat().
                statusCode(200).
                contentType("application/json; charset=utf-8");
        String gryffindorID = response.jsonPath().getString("find{it.name == 'Gryffindor'}_id");
        System.out.println("gryffindorID = " + gryffindorID);
        List<String>memberIDs = response.jsonPath().getList("find{it.name == 'Gryffindor'}.members");
        System.out.println("Gryffindor member IDs = " + memberIDs);
        Response response2 =
                given().
                        header("Accept","application/json").
                        queryParams("key",apiKey).
                        pathParam("id",gryffindorID).
                        when().
                        get("/houses/{id}").prettyPeek();
        List<String>memberIDsFrom2ndResponse = response2.jsonPath().getList("[0].members._id");
        System.out.println("memberIDsFrom2ndResponse = " + memberIDsFrom2ndResponse);
      Assertions. assertEquals(memberIDs,memberIDsFrom2ndResponse);

    }

    /*Verify house members again
1. Send a get request to /houses/:id. Request includes : • Header Accept with value application/json
• Query param key with value {{apiKey}}
• Path param id with value 5a05e2b252f721a3cf2ea33f
2. Capture the ids of all members
3. Send a get request to /characters. Request includes :
• Header Accept with value application/json • Query param key with value {{apiKey}}
• Query param house with value Gryffindor
4. Verify that response contains the same member ids from step 2*/

    @Test
    @DisplayName("Verify House members again")
    public void verifyHouseMembersAgain(){

        Response response =
                given().accept(ContentType.JSON).
                        queryParam("key",apiKey).
                        when().get("/houses/{id}","5a05e2b252f721a3cf2ea33f").prettyPeek();

    //2. Capture the ids of all members

    }


    /*Verify house with most members
1. Send a get request to /houses. Request includes : • Header Accept with value application/json
• Query param key with value {{apiKey}}
2. Verify status code 200, content type application/json; charset=utf-8
3. Verify that Gryffindor house has the most members*/
    @Test
    @DisplayName("Verify house with most members")
    public void verifyHouseWithMostMembers(){
        Response response =
                given().accept(ContentType.JSON).
                        queryParams("key",apiKey).
                when().get("/houses").prettyPeek();

        response.then().assertThat().statusCode(200).
                        assertThat().contentType(ContentType.JSON);


        //3. Verify that Gryffindor house has the most members*/


    }

}
