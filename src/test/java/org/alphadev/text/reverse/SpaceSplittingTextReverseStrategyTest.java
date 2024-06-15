package org.alphadev.text.reverse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SpaceSplittingTextReverseStrategyTest {

	/**
	 * Keeping this test as an example of how we do not want the implementation to behave.
	 * The spaces should be kept, right?
	 */
	@Test
	void testReverseWhiteSpaces() {
		var strat = new SpaceSplittingTextReverseStrategy();
		var actual = strat.reverse("    ");
		Assertions.assertEquals("", actual);
	}

	/**
	 * Keeping this test as an example of how we do not want the implementation to behave.
	 * The entire sentence is reversed instead of the separate words.
	 */
	@Test
	void testReverseTabs() {
		var strat = new SpaceSplittingTextReverseStrategy();
		var actual = strat.reverse("Kaffe\tär\tgott.");
		Assertions.assertEquals("ttog\trä\teffaK.", actual);
	}

	@Test
	void testReverseTwoSentencesWithNumbers() {
		var strat = new SpaceSplittingTextReverseStrategy();
		var actual = strat.reverse("Det var en gång en apa som åt 57 bananer, då fick han ont i magen. Sen blev apan l33t.");
		Assertions.assertEquals("teD rav ne gnåg ne apa mos tå 75 renanab, åd kcif nah tno i negam. neS velb napa t33l.", actual);
	}
}