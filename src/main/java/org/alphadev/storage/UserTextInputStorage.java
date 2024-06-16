package org.alphadev.storage;

import java.util.List;

import org.alphadev.text.reverse.TextReverseItem;

public interface UserTextInputStorage {

	List<TextReverseItem> read(String sessionId);

	boolean write(String sessionId, String text, String reversedText);
}
