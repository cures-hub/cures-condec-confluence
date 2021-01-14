package de.uhd.ifi.se.decision.management.confluence.rest;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse.Status;

public class TestStoreKnowledgeElements {
	private KnowledgeRest knowledgeRest;

	@Before
	public void setUp() {
		knowledgeRest = new KnowledgeRest();
	}

	@Test
	public void testInvalidRequest() {
		assertEquals(Status.BAD_REQUEST.getStatusCode(),
				knowledgeRest.storeKnowledgeElements(null, 1, "", "").getStatus());
	}

	@Test
	public void testValidRequestJsonStringEmpty() {
		assertEquals(Status.OK.getStatusCode(), knowledgeRest.storeKnowledgeElements(null, 1, "1", "").getStatus());
	}

	@Test
	public void testValidRequest() {
		assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), knowledgeRest
				.storeKnowledgeElements(null, 1, "1", "[[{'key' : 'CONDEC-1', 'type':'issue'}]]").getStatus());
	}
}
