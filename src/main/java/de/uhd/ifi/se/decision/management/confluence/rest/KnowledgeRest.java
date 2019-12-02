package de.uhd.ifi.se.decision.management.confluence.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

/**
 * REST resource: Enables importing decision knowledge from Jira and storing it.
 */
public interface KnowledgeRest {

	Response storeKnowledgeElements(HttpServletRequest request, int pageId, String macroId, String jsonObjectString);

	Response getStoredKnowledgeElements(int pageId, String macroId);

	Response getKnowledgeElementsFromJira(String projectKey, String query);

	Response getProjectsFromJira();
}