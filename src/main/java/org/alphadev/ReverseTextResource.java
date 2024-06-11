package org.alphadev;

import jakarta.inject.Inject;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/reverse")
public class ReverseTextResource {

	@Inject
	TextReverseStrategy textReverseStrategy;

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String reverse(@FormParam("user-input") String text) {
		return textReverseStrategy.reverse(text);
	}
}
