package org.alphadev.text.reverse;

import static io.quarkus.runtime.util.StringUtil.isNullOrEmpty;

import org.alphadev.session.SessionIdGenerator;
import org.alphadev.storage.UserTextInputStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

@Path("/reverse")
public class ReverseTextResource {

	// TODO - the resource is getting large with too many dependencies, move stuff to a service class
	@Inject
	@TextReverseStrategyQualifier(TextReverseStrategyQualifier.Strategy.POINTER_BASED)
	TextReverseStrategy textReverseStrategy;
	@Inject
	TextValidator textValidator;
	@Inject
	SessionIdGenerator sessionIdGenerator;
	@Inject
	UserTextInputStorage storage;

	private final Logger log = LoggerFactory.getLogger(getClass());

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public Response reverse(@FormParam("user-input") String text, @CookieParam("sessionId") String sessionId) {
		log.info("Got request to reverse for sessionId={} and text={}", sessionId, text);
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

	@GET
	@Path("/history")
	@Produces(MediaType.TEXT_PLAIN)
	public Response history(@CookieParam("sessionId") String sessionId) {
		log.info("Fetching historical reverse text requests...");
		if (isNullOrEmpty(sessionId)) {
			log.info("Session id is null...");
			return Response.ok().entity("").build();
		}
		var items = storage.read(sessionId);
		log.info("Retrieved {} text items from storage", items.size());
		var b = new StringBuilder();
		for (var item : items) {
			var r = String.format("<tr><td>%s</td><td>%s</td><td>%s</td></tr>",
					item.timestamp(), item.text(), item.reversedText());
			// TODO - delegate the formatting to a utility class
			// TODO - format the date/timestamp properly...
			// TODO - sort the items by date/timestamp desc?
			b.append(r);
		}
		return Response.ok().entity(b.toString()).build();
	}

	private Response handleTextReverseRequest(String sessionId, final String text) {
		var reversedText = textReverseStrategy.reverse(text);
		var res = Response.ok()
				.entity(reversedText);
		if (isNullOrEmpty(sessionId)) {
			sessionId = handleMissingSessionId(res);
		}
		var success = storage.write(sessionId, text, reversedText);
		if (success) {
			log.info("Successfully persisted reversed text");
		} else {
			log.warn("Failed to persist reversed text");
			// TODO - display error message to user?
		}
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
				//.secure(true)
				.maxAge(-1)
				.build();
	}
}
