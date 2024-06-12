package org.alphadev;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

	private boolean isValid = true;
	private final List<String> messages = new ArrayList<>();

	private ValidationResult() {
	}

	public static ValidationResult valid() {
		return new ValidationResult();
	}

	public static ValidationResult invalid(String message) {
		var v = new ValidationResult();
		v.setInvalid();
		v.addMessage(message);
		return v;
	}

	public void setInvalid() {
		isValid = false;
	}

	public void addMessage(final String message) {
		messages.add(message);
	}

	public void join(ValidationResult other) {
		this.isValid = this.isValid && other.isValid;
		this.messages.addAll(other.messages);
	}

	public boolean isValid() {
		return isValid;
	}

	public List<String> getMessages() {
		return messages;
	}
}
