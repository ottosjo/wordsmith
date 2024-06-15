package org.alphadev.text.reverse;

import static io.quarkus.runtime.util.StringUtil.isNullOrEmpty;

import org.alphadev.session.SessionIdGenerator;
import org.alphadev.storage.UserTextInputStorage;

import jakarta.inject.Inject;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

@Path("/reverse")
public class ReverseTextResource {

	@Inject
	TextReverseStrategy textReverseStrategy;
	@Inject
	TextValidator textValidator;
	@Inject
	SessionIdGenerator sessionIdGenerator;
	@Inject
	UserTextInputStorage storage;

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public Response reverse(@FormParam("user-input") String text, @CookieParam("session-id") String sessionId) {
		var validationResult = textValidator.validate(text);
		if (validationResult.isValid()) {
			return handleTextReverseRequest(sessionId, text);
		} else {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity(validationResult.getMessages())
					.build();
		}
	}

	private Response handleTextReverseRequest(String sessionId, final String text) {
		var reversed = textReverseStrategy.reverse(text);
		var res = Response.ok()
				.entity(reversed);
		if (isNullOrEmpty(sessionId)) {
			sessionId = handleMissingSessionId(res);
		}
		var success = storage.write(sessionId, text);
		// TODO - handle failure
		return res.build();
	}

	private String handleMissingSessionId(final Response.ResponseBuilder res) {
		String sessionId;
		sessionId = sessionIdGenerator.generate();
		var cookie = createCookie(sessionId);
		res.cookie(cookie);
		return sessionId;
	}

	private static NewCookie createCookie(final String sessionId) {
		return new NewCookie.Builder("sessionId")
				.value(sessionId)
				.secure(true)
				.maxAge(-1)
				.build();
	}
}
