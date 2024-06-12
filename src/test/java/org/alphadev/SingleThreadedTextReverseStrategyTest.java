package org.alphadev;

import org.alphadev.text.reverse.SingleThreadedTextReverseStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SingleThreadedTextReverseStrategyTest {

	@Test
	void testReverseTwoSentencesWithNumbers() {
		var strat = new SingleThreadedTextReverseStrategy();
		var actual = strat.reverse("Det var en gång en apa som åt 57 bananer, då fick han ont i magen. Sen blev apan l33t.");
		Assertions.assertEquals("teD rav ne gnåg ne apa mos tå 75 renanab, åd kcif nah tno i negam. neS velb napa t33l.", actual);
	}
}