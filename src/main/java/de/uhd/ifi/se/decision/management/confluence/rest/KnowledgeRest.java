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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uhd.ifi.se.decision.management.confluence.model.KnowledgeElement;
import de.uhd.ifi.se.decision.management.confluence.oauth.JiraClient;
import de.uhd.ifi.se.decision.management.confluence.persistence.KnowledgePersistenceManager;

/**
 * REST resource: Enables importing decision knowledge from Jira and storing it.
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
		List<KnowledgeElement> elements = KnowledgeElement.parseJsonString(jsonString);
		// first remove issues from this page
		if (elements.size() > 0) {
			KnowledgePersistenceManager.removeKnowledgeElements(pageId, macroId);
		}
		for (KnowledgeElement element : elements) {
			element.setPageId(pageId);
			element.setMacroId(macroId);
			KnowledgePersistenceManager.addKnowledgeElement(element);
		}
		LOGGER.info(elements.size() + " knowledge elements were stored in database");
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
		List<KnowledgeElement> storedElements = KnowledgePersistenceManager.getElements(pageId, macroId);
		return Response.status(Response.Status.OK).entity(storedElements).build();
	}

	@Path("/getProjectsFromJira")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getProjectsFromJira() {
		String jiraProjectsJsonResponse = JiraClient.instance.getJiraProjectsAsJson();
		return Response.status(Response.Status.OK).entity(jiraProjectsJsonResponse).build();
	}
}