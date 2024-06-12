package org.alphadev;

import jakarta.inject.Inject;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/reverse")
public class ReverseTextResource {

	@Inject
	TextReverseStrategy textReverseStrategy;

	@Inject
	TextValidator textValidator;

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public Response reverse(@FormParam("user-input") String text) {
		var result = textValidator.validate(text);
		if (result.isValid()) {
			var reversed = textReverseStrategy.reverse(text);
			return Response.ok()
					.entity(reversed)
					.build();
		} else {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity(result.getMessages())
					.build();
		}
	}
}
