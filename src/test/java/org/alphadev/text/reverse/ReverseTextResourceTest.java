package org.alphadev.text.reverse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.alphadev.MockUserTextInputStorage;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class ReverseTextResourceTest {

	/**
	 * Replace usage of MongoUserTextInputStorage
	 */
	@Inject
	MockUserTextInputStorage textInputStorage;

	@Test
	void testReverseWithNullTextReturnsStatusCode400() {
		given()
				.when()
				.post("/reverse")
				.then()
				.statusCode(400)
				.body(is("[The text is empty (null), please provide a text.]"));
	}

	@Test
	void testReverseReturnsStatusCode200WithReversedTextInBody() {
		given()
				.when()
				.contentType("multipart/form-data")
				.multiPart("user-input", "This is a text.")
				.post("/reverse")
				.then()
				.statusCode(200)
				.body(is("sihT si a txet."));
	}
}