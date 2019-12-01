package de.uhd.ifi.se.decision.management.confluence.persistence;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.uhd.ifi.se.decision.management.confluence.persistence.impl.KnowledgePersistenceManagerImpl;

public class MainPluginUnitTests {

	@Test
	public void testIsNullOrEmpty_false() {
		boolean isEmpty = KnowledgePersistenceManagerImpl.isNullOrEmpty("abcd");
		assertEquals(false, isEmpty);
	}

	@Test
	public void testIsNullOrEmpty_true() {
		boolean isEmpty = KnowledgePersistenceManagerImpl.isNullOrEmpty("");
		assertEquals(true, isEmpty);
	}

	@Test
	public void testIsNullOrEmpty_NullTrue() {
		boolean isEmpty = KnowledgePersistenceManagerImpl.isNullOrEmpty(null);
		assertEquals(true, isEmpty);
	}

}