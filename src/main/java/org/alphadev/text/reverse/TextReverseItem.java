package org.alphadev.text.reverse;

import java.time.Instant;

public record TextReverseItem(String sessionId, Instant time, String text, String reversedText) {

}
