package de.uhd.ifi.se.decision.management.confluence.rest;

//import java.net.HttpURLConnection;
import java.util.ArrayList;

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

import org.json.JSONArray;
import org.json.JSONObject;

import de.uhd.ifi.se.decision.management.confluence.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.confluence.oauth.JiraClient;

@Path("/issueRest")
public class DecisionKnowledgeElementResource {
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/add-issue-array")
	public Response addIssue(@QueryParam("pageId") int pageId, @QueryParam("macroId") String macroId,
			String jsonObjectString, @Context HttpServletRequest request) {

		Boolean result = handlePostRequestResult(pageId, macroId, jsonObjectString);
		if (result) {
			return Response.ok().build();
		} else {
			return Response.serverError().build();
		}
	}

	public Boolean handlePostRequestResult(int pageId, String macroId, String jsonObjectString) {
		Boolean result = true;
		try {
			JSONObject jsonObject = new JSONObject(jsonObjectString);
			Boolean useObjectUrl = false;
			String url = "";

			if (((String) jsonObject.get("url")).equals("USE_OBJECT_URL")) {
				useObjectUrl = true;
			} else {
				url = (String) jsonObject.get("url");
			}

			JSONArray listOfArrays = (JSONArray) jsonObject.get("data");
			// first remove issues from this page

			DecisionKnowledgeElementKeeping decisionKnowledgeElementKeeping = DecisionKnowledgeElementKeeping
					.getInstance();
			if (listOfArrays.length() > 0) {
				decisionKnowledgeElementKeeping.removeDecisionKnowledgeElement(pageId, macroId);
			}

			for (int j = 0; j < listOfArrays.length(); j++) {
				JSONArray jsonArray = listOfArrays.getJSONArray(j);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject myObj = jsonArray.getJSONObject(i);
					DecisionKnowledgeElement decisionKnowledgeElement = handleInnerForLoopAndCreateElement(myObj,
							useObjectUrl, url, pageId, j, macroId);
					decisionKnowledgeElementKeeping.addDecisionKnowledgeElement(decisionKnowledgeElement);
				}
			}
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	// FIXME: This is too complex.
	private DecisionKnowledgeElement handleInnerForLoopAndCreateElement(JSONObject myObj, Boolean useObjectUrl,
			String globalUrl, int pageId, int group, String macroId) {
		String completeKey = (String) myObj.get("key");
		String concatKey = completeKey;
		// check if completeKey has :
		if (completeKey.indexOf(":") > -1) {
			concatKey = concatKey.split(":")[0];
		}
		String link = "";

		if (useObjectUrl) {
			if (myObj.has("url")) {
				link = (String) myObj.get("url");
			}
			if (myObj.has("link")) {
				link = (String) myObj.get("link");
			}
		} else {
			link = globalUrl + concatKey;
		}
		int myPageId = pageId;
		String mySummary = myObj.has("summary") ? (String) myObj.get("summary") : "";
		String myType = myObj.has("type") ? (String) myObj.get("type") : "";
		String description = myObj.has("description") ? (String) myObj.get("description") : "";
		String myKey = completeKey;
		return new DecisionKnowledgeElement(link, myPageId, mySummary, myType, myKey, description, group, macroId);
	}

	@Path("/getIssues")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getDecisionKnowledgeElement(@QueryParam("pageId") int pageId,
			@QueryParam("macroId") String macroId) {
		try {
			DecisionKnowledgeElementKeeping decisionKnowledgeElementKeeping = DecisionKnowledgeElementKeeping
					.getInstance();
			ArrayList<DecisionKnowledgeElement> jsonArray = decisionKnowledgeElementKeeping
					.getElementsFromPageIdAndMacroId(pageId, macroId);
			return Response.status(Response.Status.OK).entity(jsonArray).build();

		} catch (Exception e) {
			return Response.serverError().build();
		}
	}

	@Path("/getIssuesFromJira")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getDecisionKnowledgeElement(@QueryParam("projectKey") String projectKey,
			@QueryParam("query") String query) {
		try {
			String jsonString = JiraClient.instance.getDecisionKnowledgeFromJira(query, projectKey);

			return Response.status(Response.Status.OK).entity(jsonString).build();

		} catch (Exception e) {
			return Response.serverError().build();
		}
	}

	@Path("/getProjectsFromJira")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getProjectsFromJira() {
		try {
			String jiraProjects = JiraClient.instance.getJiraProjectsAsJson();
			return Response.status(Response.Status.OK).entity(jiraProjects).build();
		} catch (Exception e) {
			return Response.serverError().build();
		}
	}
}