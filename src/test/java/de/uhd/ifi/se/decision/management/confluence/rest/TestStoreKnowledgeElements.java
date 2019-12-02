package de.uhd.ifi.se.decision.management.confluence.rest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse.Status;

import de.uhd.ifi.se.decision.management.confluence.rest.impl.KnowledgeRestImpl;

public class TestStoreKnowledgeElements {
	private KnowledgeRest knowledgeRest;

	@Before
	public void setUp() {
		knowledgeRest = new KnowledgeRestImpl();
	}

	@Test
	public void testInvalidRequest() {
		assertEquals(Status.BAD_REQUEST.getStatusCode(),
				knowledgeRest.storeKnowledgeElements(null, 1, "", "").getStatus());
	}

	@Test
	@Ignore
	public void testValidRequest() {
		assertEquals(Status.OK.getStatusCode(), knowledgeRest.storeKnowledgeElements(null, 1, "1", "").getStatus());
	}
}
