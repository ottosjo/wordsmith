package org.alphadev;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TextValidator {

	public static final int MAX_TEXT_LENGTH = 1024;

	public ValidationResult validate(String text) {
		return join(
				validateContent(text),
				validateTextLength(text)
		);
	}

	/**
	 * The text is required to contain at least one letter or digit.
	 */
	private static ValidationResult validateContent(final String text) {
		for (final char c : text.toCharArray()) {
			if (Character.isLetterOrDigit(c)) {
				return ValidationResult.valid();
			}
		}
		return ValidationResult.invalid("The text is required to contain at least one letter or number, please try again.");
	}

	/**
	 * The max text length is limited by {@link #MAX_TEXT_LENGTH}
	 */
	private ValidationResult validateTextLength(final String text) {
		if (text.length() > MAX_TEXT_LENGTH) {
			return ValidationResult.invalid(String.format(
					"Text length is %d exceeding the maximum length %d, please provide a smaller text",
					text.length(), MAX_TEXT_LENGTH));
		}
		return ValidationResult.valid();
	}

	/**
	 * Combines multiple validation results into one final result.
	 * The provided results are not altered.
	 */
	private ValidationResult join(ValidationResult... results) {
		var r = ValidationResult.valid();
		for (var result : results) {
			r.join(result);
		}
		return r;
	}
}
