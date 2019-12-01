package de.uhd.ifi.se.decision.management.confluence;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.uhd.ifi.se.decision.management.confluence.persistence.impl.KnowledgePersistenceManagerImpl;

public class MainPluginUnitTests {

	@Test
	public void testIsNullOrEmpty_false() {
		Boolean isEmpty = KnowledgePersistenceManagerImpl.isNullOrEmpty("abcd");
		assertEquals("Is not Empty!", false, isEmpty);
	}

	@Test
	public void testIsNullOrEmpty_true() {
		Boolean isEmpty = KnowledgePersistenceManagerImpl.isNullOrEmpty("");
		assertEquals("Is Empty!", true, isEmpty);
	}

	@Test
	public void testIsNullOrEmpty_NullTrue() {
		Boolean isEmpty = KnowledgePersistenceManagerImpl.isNullOrEmpty(null);
		assertEquals("Is null!", true, isEmpty);
	}

}