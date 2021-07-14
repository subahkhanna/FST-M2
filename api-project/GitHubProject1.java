package GitHubProj;

import static io.restassured.RestAssured.given;
//import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.equalTo;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;


public class GitHubProject1 {

	 // Declare request specification
  RequestSpecification requestSpec;
  // Declare response specification
  ResponseSpecification responseSpec;
  String sshkey;
  int sshkeyId;
  
  @BeforeClass
  public void setUp() {
      // Create request specification
      requestSpec = new RequestSpecBuilder()
              // Set content type
              .setContentType(ContentType.JSON)
              .addHeader("Authorization", "token <blank>")
              // Set base URL
              .setBaseUri("https://api.github.com")
              // Build request specification
              .build();

      sshkey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDOdlLEZmYtxzTZ6xtyG1Pm07Q11iydLQk9gLrwM7X+bQIkLPGkzzYdubD7v4ZIOQmyc7WekLvLGQxNzvXj34JdEEGGFEcVlJAAuntVPBqe6lP9zeKIFs2L+amYYXpDFo6iXspRX0dwOh+rfKndb12PU9i+cSQqNFd4TSTwVVbfQLHBkqk/PJWFN9SaT/tnXHC6WYBbrahofq04qvkbjvrs8WDuVS9ltf8ai4nNPv7nSNp5pQRF48koEKPgBmUvF8jtUfN/ZEXSqB8+w77N7bJd68+Q4KHwsQ/cRfNfNm2tA8SOhzytq4StkaAiwgLGMgkkPmBVQDzNVaUEXEcblank";
  }
  
  @Test(priority=1)
  // Test case using a DataProvider
  public void addToken() {
  	String reqBody= "{\"title\": \"TestAPIKey\", \"key\": \""+sshkey+"\"}";
  	    	
      Response response = given().spec(requestSpec) // Use requestSpec
              .body(reqBody) // Send request body
              .when().post("/user/keys"); // Send POST request

     String resBody = response.getBody().asPrettyString();
      System.out.println(resBody);
      sshkeyId =response.then().extract().path("id");
      
      // Assertions
      response.then().statusCode(201); 
      response.then().body("id",equalTo(sshkeyId));
  }
  
  @Test(priority=2)
  public void getPets() {
      Response response = given().spec(requestSpec) // Use requestSpec
              .when().get("/user/keys"); // Send GET request

      // Print response
      System.out.println(response.asPrettyString());
      
      // Assertions
      response.then().statusCode(200); 
      response.then().body("[0].id",equalTo(sshkeyId));
  }
  
  
  @Test(priority=3)
  public void deletePets() {
      Response response = given().spec(requestSpec) // Use requestSpec
              .pathParam("keyId", sshkeyId) // Add path parameter
              .when().delete("/user/keys/{keyId}"); // Send GET request

      // Print response
      System.out.println(response.asPrettyString());
      
      // Assertions
      response.then().statusCode(204);
  }
  
}
