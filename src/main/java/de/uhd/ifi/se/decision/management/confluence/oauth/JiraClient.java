package de.uhd.ifi.se.decision.management.confluence.oauth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.json.JSONArray;

import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.ApplicationLinkRequest;
import com.atlassian.applinks.api.ApplicationLinkRequestFactory;
import com.atlassian.applinks.api.ApplicationLinkResponseHandler;
import com.atlassian.applinks.api.ApplicationLinkService;
import com.atlassian.applinks.api.CredentialsRequiredException;
import com.atlassian.applinks.api.application.jira.JiraApplicationType;
import com.atlassian.sal.api.component.ComponentLocator;
import com.atlassian.sal.api.net.Request;
import com.atlassian.sal.api.net.Response;
import com.atlassian.sal.api.net.ResponseException;

import de.uhd.ifi.se.decision.management.confluence.model.KnowledgeElement;

/**
 * Class responsible for the communication between Confluence and Jira via
 * application links and REST API.
 */
public class JiraClient {

	private ApplicationLink jiraApplicationLink;

	/**
	 * The singleton instance of the JiraClient. Please use this instance.
	 */
	public static JiraClient instance = new JiraClient();

	public JiraClient() {
		ApplicationLinkService applicationLinkService = ComponentLocator.getComponent(ApplicationLinkService.class);
		// TODO
		// @issue There might be more than one application links to Jira. Currently, we
		// only support the first link. How can we support all links?
		this.jiraApplicationLink = applicationLinkService.getPrimaryApplicationLink(JiraApplicationType.class);
	}

	/**
	 * @return all Jira projects that the user is allowed to access as a JSON
	 *         string.
	 */
	public String getJiraProjectsAsJson() {
		return getResponseFromJiraWithApplicationLink("rest/api/2/project");
	}

	/**
	 * @param searchTerm
	 *            substring that the knowledge elements must contain.
	 * @param projectKey
	 *            of the Jira project.
	 * @param status
	 * @param knowledgeTypes
	 * @return list of knowledge elements that match a certain query and the project
	 *         key.
	 */
	public List<KnowledgeElement> getDecisionKnowledgeFromJira(String searchTerm, String projectKey, long startDate,
			long endDate, List<String> knowledgeTypes, List<String> status) {
		String jsonString = getDecisionKnowledgeFromJiraAsJsonString(searchTerm, projectKey, startDate, endDate,
				knowledgeTypes, status);
		return KnowledgeElement.parseJsonString(jsonString);
	}

	private String getResponseFromJiraWithApplicationLink(String jiraUrl) {
		ApplicationLinkRequest request = createRequest(Request.MethodType.GET, jiraUrl);
		if (request == null) {
			return "";
		}
		return receiveResponseFromJiraWithApplicationLink(request);
	}

	private String postResponseFromJiraWithApplicationLink(String jiraUrl, String searchTerm, String projectKey,
			long startDate, long endDate, String knowledgeTypes, String status) {
		ApplicationLinkRequest request = createRequest(Request.MethodType.POST, jiraUrl);
		if (request == null) {
			return "";
		}
		request.setRequestBody("{\"projectKey\":\"" + projectKey + "\",\"searchTerm\":\"" + searchTerm + "\","
				+ "\"startDate\":\"" + startDate + "\",\"endDate\":\"" + endDate + "\",\"knowledgeTypes\":"
				+ knowledgeTypes + ",\"status\":" + status + "}", MediaType.APPLICATION_JSON);
		return receiveResponseFromJiraWithApplicationLink(request);
	}

	private ApplicationLinkRequest createRequest(Request.MethodType type, String url) {
		if (jiraApplicationLink == null) {
			return null;
		}
		ApplicationLinkRequestFactory requestFactory = jiraApplicationLink.createAuthenticatedRequestFactory();
		try {
			return requestFactory.createRequest(type, url);
		} catch (CredentialsRequiredException e) {
		}
		return null;
	}

	private String receiveResponseFromJiraWithApplicationLink(ApplicationLinkRequest request) {
		String responseBody = "";
		try {
			request.addHeader("Content-Type", "application/json");
			responseBody = request.executeAndReturn(new ApplicationLinkResponseHandler<String>() {
				@Override
				public String credentialsRequired(final Response response) throws ResponseException {
					return response.getResponseBodyAsString();
				}

				@Override
				public String handle(final Response response) throws ResponseException {
					return response.getResponseBodyAsString();
				}
			});
		} catch (ResponseException e) {
			responseBody = e.getMessage();
		}
		return responseBody;
	}

	private String getDecisionKnowledgeFromJiraAsJsonString(String query, String projectKey, long startDate,
			long endDate, List<String> knowledgeTypes, List<String> status) {
		return postResponseFromJiraWithApplicationLink("rest/condec/latest/knowledge/knowledgeElements.json",
				encodeUserInputQuery(query), projectKey, startDate, endDate, convertToJsonArray(knowledgeTypes),
				convertToJsonArray(status));
	}

	public static String convertToJsonArray(List<String> list) {
		return new JSONArray(list).toString();
	}

	private static String encodeUserInputQuery(String query) {
		String encodedUrl = "";
		try {
			encodedUrl = URLEncoder.encode(query, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodedUrl;
	}
}
