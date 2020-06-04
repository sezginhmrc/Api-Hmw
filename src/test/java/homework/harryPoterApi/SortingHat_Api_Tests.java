package homework.harryPoterApi;

import homework.pojos.pojosHarryPotter.House;
import homework.utilities.ConfigurationReader;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.when;


public class SortingHat_Api_Tests {
    @BeforeAll
    public static void beforeAll() {
        baseURI = ConfigurationReader.getProperty("POTTERAPI.URI");

    }

    /*
Verify sorting hat
1. Send a get request to /sortingHat. Request includes :
2. Verify status code 200, content type application/json; charset=utf-8
3. Verify that response body contains one of the following houses:
"Gryffindor", "Ravenclaw", "Slytherin", "Hufflepuff"
* */

    @Test
    public void verifyHouses() {

        Response response = when().get("/sortingHat").prettyPeek();

        response.then().statusCode(200).contentType("application/json; charset=utf-8");


        // to get one value string as String object into java...
        String house = response.as(String.class);
        System.out.println(house);

        //3. Verify that response body contains one of the following houses:
        List<String> houses = new ArrayList<>(Arrays.asList("Gryffindor", "Ravenclaw", "Slytherin", "Hufflepuff"));

        Assertions.assertTrue(houses.contains(house));
    }


    }

