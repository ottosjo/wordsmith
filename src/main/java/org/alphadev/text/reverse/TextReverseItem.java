package org.alphadev.text.reverse;

import org.bson.codecs.pojo.annotations.BsonProperty;

/**
 * timestamp specified in epoch millis
 */
public record TextReverseItem(
		@BsonProperty("sessionId") String sessionId,
		@BsonProperty("timestamp") Long timestamp,
		@BsonProperty("text") String text,
		@BsonProperty("reversedText") String reversedText) {

}
