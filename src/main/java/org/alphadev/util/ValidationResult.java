package org.alphadev.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ValidationResult {

	private boolean isValid = true;
	/**
	 * Additional validation rules will not be processed if isFinal is true
	 */
	private boolean isFinal = false;
	private final List<String> messages = new ArrayList<>();

	private ValidationResult() {
	}

	public static ValidationResult valid() {
		return new ValidationResult();
	}

	public static ValidationResult invalid(String message) {
		return invalid(message, false);
	}

	public static ValidationResult invalid(String message, boolean isFinal) {
		var v = new ValidationResult();
		v.isFinal = isFinal;
		v.isValid = false;
		v.addMessage(message);
		return v;
	}

	private void addMessage(final String message) {
		messages.add(message);
	}

	public void join(ValidationResult other) {
		this.isValid = this.isValid && other.isValid;
		this.messages.addAll(other.messages);
	}

	public boolean isValid() {
		return isValid;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public List<String> getMessages() {
		return messages;
	}

	@Override
	public String toString() {
		return "ValidationResult{" +
				"isValid=" + isValid +
				", isFinal=" + isFinal +
				", messages=" + Set.copyOf(messages) +
				'}';
	}
}
