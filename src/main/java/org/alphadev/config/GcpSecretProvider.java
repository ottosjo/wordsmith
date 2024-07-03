package org.alphadev.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Store GCP secrets using the command line:
 * printf "my-secret-value" | gcloud secrets create my-secret-key --data-file=-
 */
@ApplicationScoped
public class GcpSecretProvider implements SecretProvider {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public String getSecret(final String key) {
		var projectId = "wordsmith-1234";
		var version = "latest";
		var secretVersionName = SecretVersionName.of(projectId, key, version);
		// TODO - create client once?
		try (var client = SecretManagerServiceClient.create()) {
			var response = client.accessSecretVersion(secretVersionName);
			return response.getPayload().getData().toStringUtf8();
		} catch (Exception e) {
			log.error("Failed to fetch secret '{}'", key, e);
		}
		return null;
	}
}
