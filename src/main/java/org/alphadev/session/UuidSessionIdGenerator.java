package org.alphadev.session;

import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UuidSessionIdGenerator implements SessionIdGenerator {

	public String generate() {
		return UUID.randomUUID().toString();
	}
}
