package org.alphadev.storage.mongo;

class MongoConstant {

	private MongoConstant() {
	}

	public static final String DB_NAME = "wordsmith";
	public static final String SESSION_ID_KEY = "sessionId";

	public static class Collection {
		private Collection() {
		}

		public static final String REVERSIBLE_TEXT_INPUT = "reversible-text-input";
	}
}
