package homework.githupApi;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class GitHupApi {

    @BeforeAll
    public static void beforeAll() {
        baseURI = "https://api.github.com";
        //authentication=basic("admin","admin");

    }
    /*Verify organization information
1. Send a get request to /orgs/:org. Request includes : • Path param org with value cucumber
2. Verify status code 200, content type application/json; charset=utf-8
3. Verify value of the login field is cucumber
4. Verify value of the name field is cucumber
5. Verify value of the id field is 320565*/

    @Test
    @DisplayName("Verify Organization information")
    public void verifyOrganizationInfo() {

        when().
                get("/orgs/{org}", "cucumber").prettyPeek().
                then().assertThat().statusCode(200).
                assertThat().contentType("application/json; charset=utf-8").
                assertThat().body("login", is("cucumber")).
                assertThat().body("name", is(equalToIgnoringCase("Cucumber"))).
                assertThat().body("id", is(320565));
    }

    /*Verify error message
1. Send a get request to /orgs/:org. Request includes : • Header Accept with value application/xml
• Path param org with value cucumber
2. Verify status code 415, content type application/json; charset=utf-8
3. Verify response status line include message Unsupported Media Type*/

    @Test
    @DisplayName("Verify error message")
    public void verifyErrorMessage() {

        given().
                accept(ContentType.XML).
                when().
                get("/orgs{org}", "cucumber").prettyPeek().
                then().assertThat().
                statusCode(415).
                assertThat().
                contentType("application/json; charset=utf-8").
                assertThat().
                statusLine(StringContains.containsString("Unsupported Media Type"));
    }

    /*Number of repositories
1. Send a get request to /orgs/:org. Request includes :
 • Path param org with value cucumber
2. Grab the value of the field public_repos
3. Send a get request to /orgs/:org/repos. Request includes :
• Path param org with value cucumber
4. Verify that number of objects in the response is equal to value from step 2*/
    @Test
    public void verifyOrgInfo() {

        Response response =

                get("orgs/{org}", "cucumber").prettyPeek();

        int public_repos = response.jsonPath().getInt("public_repos");


        Response response1 =
                given().
                        queryParam("per_page", 100).
                        when().get("/orgs/{org}/repos", "cucumber").prettyPeek();

        response1.then().assertThat()
                .body("size()", is(public_repos));


    }

    /*Repository id information
1. Send a get request to /orgs/:org/repos. Request includes : • Path param org with value cucumber
2. Verify that id field is unique in every in every object in the response
3. Verify that node_id field is unique in every in every object in the response*/
    @Test
    public void repositoryIdInformation() {

        Response response =

                get("orgs/{org}", "cucumber").prettyPeek();


        List<Integer> idList = response.jsonPath().getList("id");
        List<String> nodeList = response.jsonPath().getList("node_id");

        Set<Integer> idSet = new HashSet<>(idList);
        Set<String> nodeSet = new HashSet<>(nodeList);

        Assertions.assertEquals(idList.size(), idSet.size());
        Assertions.assertEquals(nodeList.size(), nodeSet.size());
    }
    /*Repository owner information
1. Send a get request to /orgs/:org. Request includes : • Path param org with value cucumber
2. Grab the value of the field id
3. Send a get request to /orgs/:org/repos. Request includes :
• Path param org with value cucumber
4. Verify that value of the id inside the owner object in every response is equal to value from step 2*/

    @Test
    public void repositoryOwnerInfo() {

        Response response =
                when().get("orgs/{org}", "cucumber").prettyPeek();

        int id = response.jsonPath().getInt("id");

        Response response1 =
                given().queryParam("per_page", 100).
                        when().get("orgs/{org}/repos", "cucumber").prettyPeek();

//4. Verify that value of the id inside the owner object in every response is equal to value from step 2*/

        response1.then().assertThat().body("owner.id", everyItem(is(id)));

        // to get all ids into list.
        //  List<Object> listt = response1.jsonPath().getList("owner.id");
        //  System.out.println(listt);

    }

    /*Ascending order by full_name sort
1. Send a get request to /orgs/:org/repos. Request includes : • Path param org with value cucumber
• Query param sort with value full_name
2. Verify that all repositories are listed in alphabetical order based on the value of the field name*/

    @Test
    public void ascendinOrder() {

        Response response =
                // given().queryParams("curl -i","https://api.github.com/repos/vmg/redcarpet/issues?state=closed").
                given().queryParams("sort", "full_name").
                        when().get("/orgs{org}/repos", "cucumber").prettyPeek();

        List<String> fullNames = response.jsonPath().getList("full_name");
        List<String> sortedNames = new ArrayList<>(fullNames);
        Collections.sort(sortedNames);

        Assertions.assertEquals(fullNames, sortedNames);

    }

    /*Descending order by full_name sort
1. Send a get request to /orgs/:org/repos. Request includes : • Path param org with value cucumber
2. Verify that by default all repositories are listed in descending order based on the value of the field
created_at*/
    public void descendingOrder() {

        Response response =
                given()
                        .queryParams("sort", "full_name").
                        queryParams("sort", "full_name").
                        queryParams("direction", "desc").

                        when().
                        get("/orgs{org}/repos", "cucumber").prettyPeek();

        List<String> fullNames = response.jsonPath().getList("full_name");
        List<String> sortedNames = new ArrayList<>(fullNames);
        Collections.sort(sortedNames, Collections.reverseOrder());

        Assertions.assertEquals(fullNames, sortedNames);
    }
    /*Default sort
1. Send a get request to /orgs/:org/repos. Request includes : • Path param org with value cucumber
2. Verify that by default all repositories are listed in descending order based on the value of the field
created_at*/


    @Test
    public void defaultSort(){
        Response response =
                given().pathParam("org","cucumber").
                when().get("/orgs{org}/repos").prettyPeek();

        List<String> dates = response.jsonPath().getList("created_at");
        List<String> sortedDates = new ArrayList<>(dates);
        Collections.sort(sortedDates,Collections.reverseOrder());

        Assertions.assertEquals(sortedDates,dates);

    }

}
