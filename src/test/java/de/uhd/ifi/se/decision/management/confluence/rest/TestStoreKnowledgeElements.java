package de.uhd.ifi.se.decision.management.confluence.rest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.atlassian.sal.api.component.ComponentLocator;
import com.sun.jersey.api.client.ClientResponse.Status;

import de.uhd.ifi.se.decision.management.confluence.mocks.MockBandanaManager;
import de.uhd.ifi.se.decision.management.confluence.mocks.MockComponentLocator;
import de.uhd.ifi.se.decision.management.confluence.persistence.KnowledgePersistenceManager;

public class TestStoreKnowledgeElements {
	private KnowledgeRest knowledgeRest;

	@Before
	public void setUp() {
		ComponentLocator.setComponentLocator(new MockComponentLocator());
		KnowledgePersistenceManager.setBandanaManager(new MockBandanaManager());
		knowledgeRest = new KnowledgeRest();
	}

	@Test
	public void testInvalidRequestPageIdZero() {
		assertEquals(Status.BAD_REQUEST.getStatusCode(), knowledgeRest.storeKnowledgeElements(null, 0, "").getStatus());
	}

	@Test
	public void testInvalidRequestJsonStringNull() {
		assertEquals(Status.BAD_REQUEST.getStatusCode(),
				knowledgeRest.storeKnowledgeElements(null, 42, null).getStatus());
	}

	@Test
	public void testValidRequestJsonStringEmpty() {
		assertEquals(Status.OK.getStatusCode(), knowledgeRest.storeKnowledgeElements(null, 1, "").getStatus());
	}

	@Test
	public void testValidRequest() {
		assertEquals(Status.OK.getStatusCode(),
				knowledgeRest.storeKnowledgeElements(null, 1, "[{'key' : 'CONDEC-1', 'type':'issue'}]").getStatus());
	}
}
