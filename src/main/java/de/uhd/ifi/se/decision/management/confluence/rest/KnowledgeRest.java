package de.uhd.ifi.se.decision.management.confluence.rest;

import java.util.List;

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

import de.uhd.ifi.se.decision.management.confluence.model.KnowledgeElement;
import de.uhd.ifi.se.decision.management.confluence.oauth.JiraClient;
import de.uhd.ifi.se.decision.management.confluence.persistence.KnowledgePersistenceManager;

/**
 * REST resource: Enables importing decision knowledge from Jira and storing it.
 */
@Path("/knowledge")
public class KnowledgeRest {

	@Path("/storeKnowledgeElements")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response storeKnowledgeElements(@Context HttpServletRequest request, @QueryParam("pageId") int pageId,
			@QueryParam("macroId") String macroId, String jsonString) {

		if (pageId == 0 || macroId == null || macroId.isEmpty() || jsonString == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		boolean result = handlePostRequestResult(pageId, macroId, jsonString);
		if (result) {
			return Response.ok().build();
		}
		return Response.serverError().build();
	}

	public boolean handlePostRequestResult(int pageId, String macroId, String jsonString) {
		boolean result = true;

		List<KnowledgeElement> elements = KnowledgeElement.parseJsonString(jsonString);

		try {
			// first remove issues from this page
			if (elements.size() > 0) {
				KnowledgePersistenceManager.removeKnowledgeElements(pageId, macroId);
			}

			for (KnowledgeElement element : elements) {
				element.setPageId(pageId);
				element.setMacroId(macroId);
				KnowledgePersistenceManager.addKnowledgeElement(element);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return result;
	}

	@Path("/getStoredKnowledgeElements")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getStoredKnowledgeElements(@QueryParam("pageId") int pageId,
			@QueryParam("macroId") String macroId) {
		if (pageId == 0 || macroId == null || macroId.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		try {
			List<KnowledgeElement> storedElements = KnowledgePersistenceManager.getElements(pageId, macroId);
			return Response.status(Response.Status.OK).entity(storedElements).build();
		} catch (Exception e) {
			return Response.serverError().build();
		}
	}

	@Path("/getKnowledgeElementsFromJira")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getKnowledgeElementsFromJira(@QueryParam("projectKey") String projectKey,
			@QueryParam("query") String query) {
		try {
			List<KnowledgeElement> elements = JiraClient.instance.getDecisionKnowledgeFromJira(query, projectKey);
			return Response.status(Response.Status.OK).entity(elements).build();
		} catch (Exception e) {
			return Response.serverError().build();
		}
	}

	@Path("/getProjectsFromJira")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getProjectsFromJira() {
		String jiraProjects = JiraClient.instance.getJiraProjectsAsJson();
		return Response.status(Response.Status.OK).entity(jiraProjects).build();
	}
}