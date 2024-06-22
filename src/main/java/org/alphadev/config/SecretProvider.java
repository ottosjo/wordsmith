package org.alphadev.config;

public interface SecretProvider {
	String getSecret(String key);
}
