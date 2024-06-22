package org.alphadev.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;

@ApplicationScoped
public class WordsmithConfigProducer {

	@Produces
	public Config createConfig() {
		return ConfigProvider.getConfig();
	}

}
