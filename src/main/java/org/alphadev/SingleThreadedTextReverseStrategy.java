package org.alphadev;

import java.util.Set;

import io.quarkus.arc.impl.Sets;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SingleThreadedTextReverseStrategy implements TextReverseStrategy {

	public static final Set<Character> NON_REVERSED_CHARS = Sets.of('.', ',');

	@Override
	public String reverse(final String text) {
		// TODO - check if text is valid/legal?

		var builder = new StringBuilder(text.length());
		var words = text.split(" ");
		for (final String word : words) {
			// TODO - check is word is valid/legal?

			var reversed = reverseChars(word);
			builder.append(reversed);
			builder.append(" ");
		}

		// remove the last space, right?
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}

	private static boolean isReversible(final char c) {
		return !NON_REVERSED_CHARS.contains(c);
	}

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
