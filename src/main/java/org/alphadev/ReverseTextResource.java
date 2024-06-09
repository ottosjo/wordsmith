package org.alphadev;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/reverse")
public class ReverseTextResource {

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String reverse(@FormParam("user-input") String text) {

		// TODO - inject the strategy and execute the reverse here

		return text;
	}
}
