package de.uhd.ifi.se.decision.management.confluence.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.uhd.ifi.se.decision.management.confluence.oauth.JiraClient;
import de.uhd.ifi.se.decision.management.confluence.persistence.KnowledgePersistenceManager;

/**
 * REST resource: Enables importing decision knowledge from Jira and storing it
 * using the {@link KnowledgePersistenceManager}.
 */
@Path("/knowledge")
public class KnowledgeRest {

	@Path("/storeKnowledgeElements/{pageId}")
	@POST
	public Response storeKnowledgeElements(@Context HttpServletRequest request, @PathParam("pageId") int pageId,
			String jsonString) {
		if (pageId == 0 || jsonString == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		KnowledgePersistenceManager.removeKnowledgeElements(pageId);
		KnowledgePersistenceManager.addKnowledgeElements(jsonString, pageId);
		return Response.ok().build();
	}

	@Path("/storedKnowledgeElements/{pageId}")
	@GET
	public Response getStoredKnowledgeElements(@PathParam("pageId") int pageId) {
		if (pageId == 0) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		return Response.ok(KnowledgePersistenceManager.getElementsAsJsonString(pageId)).build();
	}

	@Path("/projectsFromJira")
	@GET
	public Response getProjectsFromJira() {
		String jiraProjectsJsonResponse = JiraClient.instance.getJiraProjectsAsJson();
		return Response.ok(jiraProjectsJsonResponse).build();
	}
}