package de.uhd.ifi.se.decision.management.confluence.persistence;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.uhd.ifi.se.decision.management.confluence.persistence.impl.KnowledgePersistenceManagerImpl;

public class MainPluginUnitTests {

	@Test
	public void testIsNullOrEmptyFalse() {
		boolean isEmpty = KnowledgePersistenceManagerImpl.isNullOrEmpty("abcd");
		assertEquals(false, isEmpty);
	}

	@Test
	public void testIsNullOrEmptyTrue() {
		boolean isEmpty = KnowledgePersistenceManagerImpl.isNullOrEmpty("");
		assertEquals(true, isEmpty);
	}

	@Test
	public void testIsNullOrEmptyNullTrue() {
		boolean isEmpty = KnowledgePersistenceManagerImpl.isNullOrEmpty(null);
		assertEquals(true, isEmpty);
	}

}