package de.uhd.ifi.se.decision.management.confluence.rest;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import com.atlassian.sal.api.component.ComponentLocator;
import com.sun.jersey.api.client.ClientResponse.Status;

import de.uhd.ifi.se.decision.management.confluence.mocks.MockComponentLocator;
import de.uhd.ifi.se.decision.management.confluence.rest.impl.KnowledgeRestImpl;

public class TestGetKnowledgeElementsFromJira {

	private KnowledgeRest knowledgeRest;

	@Before
	public void setUp() {
		ComponentLocator.setComponentLocator(new MockComponentLocator());
		knowledgeRest = new KnowledgeRestImpl();
	}

	@Test
	public void testValid() {
		Response response = knowledgeRest.getKnowledgeElementsFromJira("CONDEC", "");
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
	}

}
