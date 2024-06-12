package org.alphadev.text.reverse;

import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SingleThreadedTextReverseStrategy implements TextReverseStrategy {

	private static final Set<Character> NON_REVERSED_CHARS = Set.of('.', ',');

	@Override
	public String reverse(final String text) {

		var builder = new StringBuilder(text.length());
		var words = text.split(" ");
		for (final String word : words) {
			var reversed = reverseChars(word);
			builder.append(reversed);
			builder.append(" ");
		}
		builder.deleteCharAt(builder.length() - 1); // ugly removal of last space added in the for-loop
		return builder.toString();
	}

	private static boolean isReversible(final char c) {
		return !NON_REVERSED_CHARS.contains(c);
	}

	/**
	 * Uses two pointers (indices) to swap the reversible characters in a word
	 */
	private static char[] reverseChars(final String word) {
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
