package org.alphadev.text.reverse;

import static java.util.Objects.isNull;

import java.util.function.Supplier;

import org.alphadev.util.ValidationResult;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TextValidator {

	public static final int MAX_TEXT_LENGTH = 1024;

	public ValidationResult validate(String text) {
		return join(
				() -> validateTextNotNull(text),
				() -> validateTextContent(text),
				() -> validateTextLength(text)
		);
	}

	/**
	 * The text is required to contain at least one letter or digit.
	 */
	private static ValidationResult validateTextNotNull(final String text) {
		return isNull(text)
				? ValidationResult.invalid("The text is empty (null), please provide a text.", true)
				: ValidationResult.valid();
	}

	/**
	 * The text is required to contain at least one letter or digit.
	 */
	private static ValidationResult validateTextContent(final String text) {
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
	 */
	private ValidationResult join(Supplier<ValidationResult>... results) {
		var joinedResult = ValidationResult.valid();
		for (var result : results) {
			var r = result.get();
			joinedResult.join(r);
			if (r.isFinal()) {
				break;
			}
		}
		return joinedResult;
	}
}
