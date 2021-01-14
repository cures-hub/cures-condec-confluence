package de.uhd.ifi.se.decision.management.confluence.rest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.atlassian.sal.api.component.ComponentLocator;
import com.sun.jersey.api.client.ClientResponse.Status;

import de.uhd.ifi.se.decision.management.confluence.mocks.MockBandanaManager;
import de.uhd.ifi.se.decision.management.confluence.mocks.MockComponentLocator;
import de.uhd.ifi.se.decision.management.confluence.persistence.KnowledgePersistenceManager;

public class TestGetStoredKnowledgeElements {
	private KnowledgeRest knowledgeRest;

	@Before
	public void setUp() {
		ComponentLocator.setComponentLocator(new MockComponentLocator());
		KnowledgePersistenceManager.setBandanaManager(new MockBandanaManager());
		knowledgeRest = new KnowledgeRest();
	}

	@Test
	public void testInvalid() {
		assertEquals(Status.BAD_REQUEST.getStatusCode(), knowledgeRest.getStoredKnowledgeElements(0, "").getStatus());
	}

	@Test
	public void testValid() {
		assertEquals(Status.OK.getStatusCode(), knowledgeRest.getStoredKnowledgeElements(1, "1").getStatus());
	}
}
