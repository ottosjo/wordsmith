package org.alphadev.config;

import static java.util.Objects.isNull;

import org.eclipse.microprofile.config.Config;

import jakarta.enterprise.context.ApplicationScoped;

// TODO - could implement a custom ConfigSource etc, but not sure that is the right path...
@ApplicationScoped
public class WordsmithConfig {

	private final Config config;
	private final SecretProvider secretProvider;

	public WordsmithConfig(Config config, SecretProvider secretProvider) {
		this.config = config;
		this.secretProvider = secretProvider;
	}

	public String getValue(String key) {
		return config.getConfigValue(key).getValue();
	}

	public String getSecret(String key) {
		var val = getValue(key);
		if (isNull(val)) {
			val = secretProvider.getSecret(key);
		}
		return val;
	}
}
