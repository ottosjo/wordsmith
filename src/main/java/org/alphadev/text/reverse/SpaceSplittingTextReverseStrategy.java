package org.alphadev.text.reverse;

import java.util.Set;

import org.alphadev.text.reverse.TextReverseStrategyQualifier.Strategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;

@TextReverseStrategyQualifier(Strategy.SPACE_SPLITTING)
@ApplicationScoped
public class SpaceSplittingTextReverseStrategy implements TextReverseStrategy {

	private static final Set<Character> NON_REVERSED_CHARS = Set.of('.', ',');

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public String reverse(final String text) {
		try {
			return reverseText(text);
		} catch (Exception ex) {
			log.error("Failed to convert the following string: '{}'", text, ex);
			return "";
		}
	}

	private static String reverseText(final String text) {
		var builder = new StringBuilder(text.length());
		var words = text.split(" ");

		for (int i = 0; i < words.length; i++) {
			var reversed = reverseChars(words[i]);
			builder.append(reversed);
			if (i != words.length - 1) {
				builder.append(" ");
			}
		}
		return builder.toString();
	}

	private static boolean isReversible(final char c) {
		return !NON_REVERSED_CHARS.contains(c);
	}

	/**
	 * Uses two pointers (indices) to swap the reversible characters in a word
	 */
	private static char[] reverseChars(final String word) {
		if (word.isEmpty()) {
			return new char[0];
		}
		var w = word.toCharArray();
		var i = 0;
		var j = word.length() - 1;
		while (i < j) {
			if (!isReversible(w[i])) {
				i++;
			} else if (!isReversible(w[j])) {
				j--;
			} else {
				var letter = w[i];
				w[i] = w[j];
				w[j] = letter;
				i++;
				j--;
			}
		}
		return w;
	}
}
