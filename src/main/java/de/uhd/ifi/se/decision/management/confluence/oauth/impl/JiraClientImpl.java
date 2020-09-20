package de.uhd.ifi.se.decision.management.confluence.oauth.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;

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

import de.uhd.ifi.se.decision.management.confluence.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.confluence.oauth.JiraClient;

/**
 * Class responsible for the communication between Confluence and Jira via
 * application links.
 */
public class JiraClientImpl implements JiraClient {

	private ApplicationLink jiraApplicationLink;

	public JiraClientImpl() {
		ApplicationLinkService applicationLinkService = ComponentLocator.getComponent(ApplicationLinkService.class);
		// TODO
		// @issue There might be more than one application links to Jira. Currently, we
		// only support the first link. How can we support all links?
		this.jiraApplicationLink = applicationLinkService.getPrimaryApplicationLink(JiraApplicationType.class);
	}

	@Override
	public Set<String> getJiraProjects() {
		String projectsAsJsonString = getJiraProjectsAsJson();
		if (projectsAsJsonString.isEmpty()) {
			return new HashSet<String>();
		}
		return parseJiraProjectsJson(projectsAsJsonString);
	}

	@Override
	public String getJiraProjectsAsJson() {
		return getResponseFromJiraWithApplicationLink("rest/api/2/project");
	}

	public Set<String> parseJiraProjectsJson(String projectsAsJsonString) {
		Set<String> projectKeys = new HashSet<String>();
		try {
			JSONArray projectArray = new JSONArray(projectsAsJsonString);
			for (Object project : projectArray) {
				JSONObject projectMap = (JSONObject) project;
				String projectKey = (String) projectMap.get("key");
				projectKeys.add(projectKey.toUpperCase());
			}
		} catch (Exception e) {
			projectKeys.add(projectsAsJsonString);
		}
		return projectKeys;
	}

	private String getResponseFromJiraWithApplicationLink(String jiraUrl) {
		String responseBody = "";
		if (jiraApplicationLink == null) {
			return responseBody;
		}
		try {
			ApplicationLinkRequestFactory requestFactory = jiraApplicationLink.createAuthenticatedRequestFactory();
			ApplicationLinkRequest request = requestFactory.createRequest(Request.MethodType.GET, jiraUrl);
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
		} catch (CredentialsRequiredException | ResponseException e) {
			responseBody = e.getMessage();
		}
		return responseBody;
	}

	private String postResponseFromJiraWithApplicationLink(String jiraUrl, String query, String projectKey) {
		String responseBody = "";
		if (jiraApplicationLink == null) {
			return responseBody;
		}
		try {
			ApplicationLinkRequestFactory requestFactory = jiraApplicationLink.createAuthenticatedRequestFactory();
			ApplicationLinkRequest request = requestFactory.createRequest(Request.MethodType.POST, jiraUrl);
			request.addHeader("Content-Type", "application/json");
			request.setRequestBody("{\"projectKey\":\"" + projectKey + "\",\"searchTerm\":\"" + query + "\"}",
					MediaType.APPLICATION_JSON);

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
		} catch (CredentialsRequiredException | ResponseException e) {
			responseBody = e.getMessage();
		}
		return responseBody;
	}

	@Override
	public List<DecisionKnowledgeElement> getDecisionKnowledgeFromJira(Set<String> jiraIssueKeys) {
		String queryWithJiraIssues = JiraClient.getJiraCallQuery(jiraIssueKeys);
		String projectKey = JiraClient.retrieveProjectKey(jiraIssueKeys);
		return getDecisionKnowledgeFromJira(queryWithJiraIssues, projectKey);
	}

	@Override
	public List<DecisionKnowledgeElement> getDecisionKnowledgeFromJira(String query, String projectKey) {
		String jsonString = getDecisionKnowledgeFromJiraAsJsonString(query, projectKey);
		return DecisionKnowledgeElement.parseJsonString(jsonString);
	}

	private String getDecisionKnowledgeFromJiraAsJsonString(String query, String projectKey) {
		return postResponseFromJiraWithApplicationLink("rest/condec/latest/knowledge/knowledgeElements.json",
				encodeUserInputQuery(query), projectKey);
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
