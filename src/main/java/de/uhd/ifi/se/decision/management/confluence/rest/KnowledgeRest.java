package de.uhd.ifi.se.decision.management.confluence.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uhd.ifi.se.decision.management.confluence.oauth.JiraClient;
import de.uhd.ifi.se.decision.management.confluence.persistence.KnowledgePersistenceManager;

/**
 * REST resource: Enables importing decision knowledge from Jira and storing it
 * using the {@link KnowledgePersistenceManager}.
 */
@Path("/knowledge")
public class KnowledgeRest {

	private static final Logger LOGGER = LoggerFactory.getLogger(KnowledgeRest.class);

	@Path("/storeKnowledgeElements")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response storeKnowledgeElements(@Context HttpServletRequest request, @QueryParam("pageId") int pageId,
			@QueryParam("macroId") String macroId, String jsonString) {

		if (pageId == 0 || macroId == null || macroId.isEmpty() || jsonString == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		KnowledgePersistenceManager.removeKnowledgeElements(pageId);
		KnowledgePersistenceManager.addKnowledgeElements(jsonString, pageId);
		return Response.ok().build();
	}

	@Path("/getStoredKnowledgeElements")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getStoredKnowledgeElements(@QueryParam("pageId") int pageId,
			@QueryParam("macroId") String macroId) {
		if (pageId == 0 || macroId == null || macroId.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		return Response.ok(KnowledgePersistenceManager.getElementsAsJsonString(pageId)).build();
	}

	@Path("/getProjectsFromJira")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getProjectsFromJira() {
		String jiraProjectsJsonResponse = JiraClient.instance.getJiraProjectsAsJson();
		return Response.ok(jiraProjectsJsonResponse).build();
	}
}