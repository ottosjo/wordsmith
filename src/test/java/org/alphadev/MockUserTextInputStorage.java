package org.alphadev;

import java.util.List;

import org.alphadev.storage.UserTextInputStorage;
import org.alphadev.text.reverse.TextReverseItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Singleton;

/**
 * This class may replace other implementations of UserTextInputStorage in test executions
 */
@Alternative
@Priority(1)
@Singleton
public class MockUserTextInputStorage implements UserTextInputStorage {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public List<TextReverseItem> read(final String sessionId) {
		log.info("Got request to read items for sessionId={}", sessionId);
		return List.of();
	}

	@Override
	public boolean write(final String sessionId, final String text, final String reversedText) {
		log.info("Got request to write sessionId={}, text='{}', reversedText='{}'", sessionId, text, reversedText);
		return true;
	}
}
