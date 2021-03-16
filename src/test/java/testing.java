import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.Test;
import specifications.RequestSpecs;
import helpers.basicAuth;
import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.*;

public class testing {

    basicAuth creds = new basicAuth();

    @Test
    public void getAllPostCommentsBasic() throws IOException {
        //***create new post***
        String user = creds.getuser();
        String pass = creds.getpass();
        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";
        Integer newpost = given()
                .spec(RequestSpecs.generateToken())
                .body(new File("src/main/resources/createPost.json"))
                .when()
                .post("/v1/post")
                .then()
                .extract().path("id");
        //***create new comment***
        given().auth().preemptive().basic(user, pass).contentType(ContentType.JSON)
                .body(new File("src/main/resources/createComment.json"))
                .when()
                .post("/v1/comment/" + newpost)
                .then();
        //***Get comment***

        given().auth().preemptive().basic(user, pass)
        .when()
        .get("/v1/comments/" + newpost)
        .then()
        .assertThat().statusCode(200).log().body();
        System.out.println("These are all the comments from this post id:" + newpost);
    }

    @Test
    public void failGetAllPostCommentsBasic() throws IOException {
        String user = creds.getuser();
        String pass = creds.getpass();
        //***create new post***
        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";
        Integer newpost = given()
                .spec(RequestSpecs.generateToken())
                .body(new File("src/main/resources/createPost.json"))
                .when()
                .post("/v1/post")
                .then()
                .extract().path("id");
        //***create new comment***
        given().auth().preemptive().basic(user, pass).contentType(ContentType.JSON)
                //***no login***
                .body(new File("src/main/resources/createComment.json"))
                .when()
                .post("/v1/comment/" + newpost)
                .then();
        //***Get comment***
        given()//***no login***
                .when()
                .get("/v1/comments/" + newpost)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message", equalTo("Please login first"));
        System.out.println("The API does not accept creating, deleting, updating or getting posts without login first.");
    }

    @Test
    public void getcommentbasic() throws IOException {
        //***create new post***
        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";
        Integer newpost = given()
                .spec(RequestSpecs.generateToken())
                .body(new File("src/main/resources/createPost.json"))
                .when()
                .post("/v1/post")
                .then()
                .extract().path("id");
        //***create new comment***
        String username = creds.getuser();
        String password = creds.getpass();
        Integer newid = given().auth().preemptive().basic(username, password).contentType(ContentType.JSON)
                .body(new File("src/main/resources/createComment.json"))
                .when()
                .post("/v1/comment/" + newpost)
                .then()
                .extract().path("id");
        //***Get comment***
        String newcomment = given().auth().preemptive().basic(username, password).contentType(ContentType.JSON)
                .body(new File("src/main/resources/createComment.json"))
                .when()
                .get("/v1/comment/" + newpost + "/" + newid)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().path("data.comment");
        System.out.println("The comment with the id :" + newid + " says: " + newcomment);
    }

    @Test
    public void failGetcommentbasic(){
        //***create new post***
        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";
        Integer newpost = given()
                //***no login***
                .body(new File("src/main/resources/createPost.json"))
                .when()
                .post("/v1/post")
                .then()
                .extract().path("id");
        //***create new comment***
        Integer newid = given()//***no login***
                .body(new File("src/main/resources/createComment.json"))
                .when()
                .post("/v1/comment/" + newpost)
                .then()
                .extract().path("id");
        //***Get comment***
        given()//***no login***
                .body(new File("src/main/resources/createComment.json"))
                .when()
                .get("/v1/comment/" + newpost + "/" + newid)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message", equalTo("Please login first"));
        System.out.println("The API does not accept creating, deleting, updating or getting posts without login first.");
    }

    @Test
    public void deleteCommentbasic() throws IOException {
        String username = creds.getuser();
        String password = creds.getpass();
        //***create new post***
        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";
        Integer newpost = given()
        .spec(RequestSpecs.generateToken())
        .body(new File("src/main/resources/createPost.json"))
        .when()
        .post("/v1/post")
        .then()
        .extract().path("id");
        System.out.println("Id from created post is:" + newpost);
        //***create new comment***
        Integer newid = given().auth().preemptive().basic(username, password).contentType(ContentType.JSON)
        .body(new File("src/main/resources/createComment.json"))
        .when()
        .post("/v1/comment/" + newpost)
        .then()
        .extract().path("id");
        System.out.println("Id from created comment is:" + newid);
        //***deleting comment***

        given().auth().preemptive().basic(username, password)
        .when()
        .delete("/v1/comment/" + newpost + "/" + newid)
        .then()
        .assertThat()
        .statusCode(200)
        .body("message", equalTo("Comment deleted"));
        System.out.println("Comment deleted successfully!");
    }

    @Test
    public void failDeleteCommentbasic() throws IOException {
        //***create new post***
        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";
        Integer newpost = given()
                .spec(RequestSpecs.generateToken())
                .body(new File("src/main/resources/createPost.json"))
                .when()
                .post("/v1/post")
                .then()
                .extract().path("id");
        System.out.println("Id from created post is:" + newpost);
        //***create new comment***
        String username = creds.getuser();
        String password = creds.getpass();
        Integer newid = given().auth().preemptive().basic(username, password).contentType(ContentType.JSON)
                .body(new File("src/main/resources/createComment.json"))
                .when()
                .post("/v1/comment/" + newpost)
                .then()
                .extract().path("id");
        System.out.println("Id from created comment is:" + newpost);
        //***deleting comment***
        given()
                //***no login***
                .when()
                .delete("/v1/comment/" + newpost + "/" + newid)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message", equalTo("Please login first"));
        System.out.println("The API does not accept creating, deleting, updating or getting posts without login first.");
    }



    @Test
    public void postcommentbasic() throws IOException {
        //***create new post***
        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";
        Integer newpost = given()
                .spec(RequestSpecs.generateToken())
                .body(new File("src/main/resources/createPost.json"))
                .when()
                .post("/v1/post")
                .then()
                .extract().path("id");
        System.out.println("Id from created post is:" + newpost);
        //***create new comment***
        String username = creds.getuser();
        String password = creds.getpass();
        Integer newid = given().auth().preemptive().basic(username, password).contentType(ContentType.JSON)
                .body(new File("src/main/resources/createComment.json"))
                .when()
                .post("/v1/comment/" + newpost)
                .then().assertThat().statusCode(200)
                .extract().path("id");
        System.out.println("Comment posted successfully");
        System.out.println("The id of the created comment is:" + newid);
    }

    @Test
    public void failPostcommentbasic() throws IOException {
        //***create new post***
        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";
        Integer newpost = given()
                .spec(RequestSpecs.generateToken())
                .body(new File("src/main/resources/createPost.json"))
                .when()
                .post("/v1/post")
                .then()
                .extract().path("id");
        String username = creds.getuser();
        String password = creds.getpass();
        given()
                //***no login***
                .body(new File("src/main/resources/createComment.json"))
                .when()
                .post("/v1/comment/" + newpost)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message", equalTo("Please login first"));
        System.out.println("The API does not accept creating, deleting, updating or getting posts without login first.");
    }


    @Test
    public void getAllPostsToken(){
        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";
        given()
                .spec(RequestSpecs.generateToken())
                .when()
                .get("/v1/posts")
                .then()
                .assertThat().statusCode(200)
                .log().body();
        System.out.println("These are all the posts that exist in the API");
    }

    @Test
    public void failGetAllPostsToken(){
        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";
        given()
                //***no login***
                .when()
                .get("/v1/posts")
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message", equalTo("Please login first"));
        System.out.println("The API does not accept creating, deleting, updating or getting posts without login first.");
    }

    @Test
    public void createPostToken(){
        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";
        Integer newpost = given()
                .spec(RequestSpecs.generateToken())
                .body(new File("src/main/resources/createPost.json"))
                .when()
                .post("/v1/post")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().path("id");
        System.out.println("Post created successfully");
        System.out.println("Id of the created post:" + newpost);
    }

    @Test
    public void failCreatePostToken(){
        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";
        given()
                //***no login***
                .body(new File("src/main/resources/createPost.json"))
                .when()
                .post("/v1/post")
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message", equalTo("Please login first"));
        System.out.println("The API does not accept creating, deleting, updating or getting posts without login first.");

    }

    @Test
    public void getOnePostsToken(){
        //***create new post***
        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";
        Integer newpost = given()
                .spec(RequestSpecs.generateToken())
                .body(new File("src/main/resources/createPost.json"))
                .when()
                .post("/v1/post")
                .then()
                .extract().path("id");
        System.out.println("Id from created post is:" + newpost);
        //***get post***
        given()
        .spec(RequestSpecs.generateToken())
        .when()
        .get("/v1/post/" + newpost)
        .then()
        .assertThat()
        .statusCode(200)
        .log().body().extract().path("title", "content");

    }

    @Test
    public void failGetOnePostsToken(){
        //***create new post***
        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";
        Integer newpost = given()
                .spec(RequestSpecs.generateToken())
                .body(new File("src/main/resources/createPost.json"))
                .when()
                .post("/v1/post")
                .then()
                .extract().path("id");
        System.out.println("Id from created post is:" + newpost);
        //***get post***
        given()
                //***no login***
                .when()
                .get("/v1/post/" + newpost)
                .then()
                .assertThat()
                .statusCode(401)
                .assertThat().body("message", equalTo("Please login first"));
        System.out.println("The API does not accept creating, deleting, updating or getting posts without login first.");

    }

    @Test
    public void updatePostToken(){
        //***create post***
        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";
        Integer newpost = given()
                .spec(RequestSpecs.generateToken())
                .body(new File("src/main/resources/createPost.json"))
                .when()
                .post("/v1/post")
                .then()
                .extract().path("id");
        System.out.println("Id of the created post:" + newpost);
        //***update post***
        given()
                .spec(RequestSpecs.generateToken())
                .body(new File("src/main/resources/updatePost.json"))
                .when()
                .put("/v1/post/" + newpost)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message", equalTo("Post updated"))
                .log().body();
        System.out.println("Post was updated successfully");
    }

    @Test
    public void failUpdatePostToken(){
        //***create post***
        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";
        Integer newpost = given()
                .spec(RequestSpecs.generateToken())
                .body(new File("src/main/resources/createPost.json"))
                .when()
                .post("/v1/post")
                .then()
                .extract().path("id");
        System.out.println("Id of the created post:" + newpost);
        //***update post***
        given()
                //***no login***
                .body(new File("src/main/resources/updatePost.json"))
                .when()
                .put("/v1/post/" + newpost)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message", equalTo("Please login first"));
        System.out.println("The API does not accept creating, deleting, updating or getting posts without login first.");
    }

    @Test
    public void deletePostToken(){
        //***create post***
        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";
        Integer newpost = given()
                .spec(RequestSpecs.generateToken())
                .body(new File("src/main/resources/createPost.json"))
                .when()
                .post("/v1/post")
                .then()
                .extract().path("id");
        System.out.println("Id of the created post:" + newpost);
        //***delete post***
        given()
                .spec(RequestSpecs.generateToken())
                .body(new File("src/main/resources/createPost.json"))
                .when()
                .delete("/v1/post/" + newpost)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message", equalTo("Post deleted"))
                .log().body();
        System.out.println("The post with id " + newpost + " was deleted successfully");
    }

    @Test
    public void failDeletePostToken(){
        //***create post***
        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";
        Integer newpost = given()
                .spec(RequestSpecs.generateToken())
                .body(new File("src/main/resources/createPost.json"))
                .when()
                .post("/v1/post")
                .then()
                .extract().path("id");
        //***delete post***
        given()
                //***no login***
                .body(new File("src/main/resources/updatePost.json"))
                .when()
                .delete("/v1/post/" + newpost)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message", equalTo("Please login first"));
        System.out.println("The API does not accept creating, deleting, updating or getting posts without login first.");
    }

    @Test
    public void updateCommentBasic() throws IOException {
        String username = creds.getuser();
        String password = creds.getpass();
        //***create new post***
        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";
        Integer newpost = given()
                .spec(RequestSpecs.generateToken())
                .body(new File("src/main/resources/createPost.json"))
                .when()
                .post("/v1/post")
                .then()
                .extract().path("id");
        System.out.println("Id from created post is: " + newpost);
        //***create new comment***

        Integer newid = given().auth().preemptive().basic(username, password).contentType(ContentType.JSON)
                .body(new File("src/main/resources/createComment.json"))
                .when()
                .post("/v1/comment/" + newpost)
                .then()
                .extract().path("id");
        System.out.println("Id from created comment is: " + newid);
        //***updating comment***

        given().auth().preemptive().basic(username, password).contentType(ContentType.JSON)
                .body(new File("src/main/resources/updateComment.json"))
                .when()
                .put("/v1/comment/" + newpost + "/" + newid)
                .then()
                .assertThat()
                .statusCode(200)
                .body("message", equalTo("Comment updated"));
        System.out.println("The comment with id: " + newid + " was updated successfully!");
    }

    @Test
    public void failUpdateCommentBasic() throws IOException {
        //***create new post***
        RestAssured.baseURI = "https://api-coffee-testing.herokuapp.com";
        Integer newpost = given()
                .spec(RequestSpecs.generateToken())
                .body(new File("src/main/resources/createPost.json"))
                .when()
                .post("/v1/post")
                .then()
                .extract().path("id");
        System.out.println("Id from created post is: " + newpost);
        //***create new comment***
        String username = creds.getuser();
        String password = creds.getpass();
        Integer newid = given().auth().preemptive().basic(username, password).contentType(ContentType.JSON)
                .body(new File("src/main/resources/createComment.json"))
                .when()
                .post("/v1/comment/" + newpost)
                .then()
                .extract().path("id");
        System.out.println("Id from created comment is: " + newid);
        //***updating comment***
        given()//***no login***
                .body(new File("src/main/resources/updateComment.json"))
                .when()
                .put("/v1/comment/" + newpost + "/" + newid)
                .then()
                .assertThat()
                .assertThat().statusCode(401)
                .assertThat().body("message", equalTo("Please login first"));
        System.out.println("The API does not accept creating, deleting, updating or getting posts without login first.");
    }
}