package org.alphadev.text.reverse;

import java.util.Set;

import org.alphadev.text.reverse.TextReverseStrategyQualifier.Strategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Use this implementation instead of the {@link SpaceSplittingTextReverseStrategy}
 * This treats whitespaces properly, and should be more efficient as well.
 */
@TextReverseStrategyQualifier(Strategy.POINTER_BASED)
@ApplicationScoped
public class PointerBasedTextReverseStrategy implements TextReverseStrategy {

	private static final Set<Character> SPLIT_CHARS = Set.of('.', ',', ' ', '\t', '\n', '\r');

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
		var result = text.toCharArray();
		var i = 0;
		var j = 0;
		while (i < text.length()) {
			if (isReversible(result[j])) {
				j++;
			} else {
				reverseCharsBetween(result, i, j - 1);
				j++;
				i = j;
			}
		}
		return new String(result);
	}

	private static void reverseCharsBetween(final char[] result, final int i, final int j) {
		var a = i;
		var b = j;
		while (a < b) {
			var temp = result[a];
			result[a] = result[b];
			result[b] = temp;
			a++;
			b--;
		}
	}

	private static boolean isReversible(final char c) {
		return !SPLIT_CHARS.contains(c);
	}
}
