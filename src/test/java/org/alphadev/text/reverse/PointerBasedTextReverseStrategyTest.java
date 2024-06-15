package org.alphadev.text.reverse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PointerBasedTextReverseStrategyTest {

	@Test
	void testReverseWhiteSpace() {
		var strat = new PointerBasedTextReverseStrategy();
		var actual = strat.reverse(" ");
		Assertions.assertEquals(" ", actual);
	}

	@Test
	void testReverseWord() {
		var strat = new PointerBasedTextReverseStrategy();
		var actual = strat.reverse("word");
		Assertions.assertEquals("drow", actual);
	}

	@Test
	void testReverseWhiteSpaces() {
		var strat = new PointerBasedTextReverseStrategy();
		var actual = strat.reverse("   Spaces  behålls   här. ");
		Assertions.assertEquals("   secapS  sllåheb   räh. ", actual);
	}

	@Test
	void testReverseTabs() {
		var strat = new PointerBasedTextReverseStrategy();
		var actual = strat.reverse("Kaffe\tär\tgott.");
		Assertions.assertEquals("effaK\trä\tttog.", actual);
	}

	@Test
	void testReverseTwoSentencesWithNumbers() {
		var strat = new PointerBasedTextReverseStrategy();
		var actual = strat.reverse("Det var en gång en apa som åt 57 bananer, då fick han ont i magen. Sen blev apan l33t.");
		Assertions.assertEquals("teD rav ne gnåg ne apa mos tå 75 renanab, åd kcif nah tno i negam. neS velb napa t33l.", actual);
	}

	@Test
	void testReverseDecimalNumber() {
		var strat = new PointerBasedTextReverseStrategy();
		var actual = strat.reverse("En dollar är ca 10,63 kronor.");
		Assertions.assertEquals("nE rallod rä ac 01,36 ronork.", actual);
	}
}