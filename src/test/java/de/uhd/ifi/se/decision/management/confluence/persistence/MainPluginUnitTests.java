package de.uhd.ifi.se.decision.management.confluence.persistence;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MainPluginUnitTests {

	@Test
	public void testIsNullOrEmptyFalse() {
		boolean isEmpty = KnowledgePersistenceManager.isNullOrEmpty("abcd");
		assertEquals(false, isEmpty);
	}

	@Test
	public void testIsNullOrEmptyTrue() {
		boolean isEmpty = KnowledgePersistenceManager.isNullOrEmpty("");
		assertEquals(true, isEmpty);
	}

	@Test
	public void testIsNullOrEmptyNullTrue() {
		boolean isEmpty = KnowledgePersistenceManager.isNullOrEmpty(null);
		assertEquals(true, isEmpty);
	}

}