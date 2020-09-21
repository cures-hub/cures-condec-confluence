package de.uhd.ifi.se.decision.management.confluence.oauth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
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
	 * @return all Jira projects that the user is allowed to access as a set of
	 *         project keys.
	 */
	public Set<String> getJiraProjects() {
		String projectsAsJsonString = getJiraProjectsAsJson();
		if (projectsAsJsonString.isEmpty()) {
			return new HashSet<String>();
		}
		return parseJiraProjectsJson(projectsAsJsonString);
	}

	/**
	 * @return all Jira projects that the user is allowed to access as a JSON
	 *         string.
	 */
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

	/**
	 * Retrieves the decision knowledge elements from Jira that are associated to a
	 * set of Jira issues.
	 * 
	 * @param jiraIssueKeys
	 *            as a set of strings.
	 * @return list of decision knowledge elements.
	 */
	public List<KnowledgeElement> getDecisionKnowledgeFromJira(Set<String> jiraIssueKeys) {
		String queryWithJiraIssues = JiraClient.getJiraCallQuery(jiraIssueKeys);
		String projectKey = JiraClient.retrieveProjectKey(jiraIssueKeys);
		return getDecisionKnowledgeFromJira(queryWithJiraIssues, projectKey);
	}

	/**
	 * Retrieves the decision knowledge elements from Jira that match a certain
	 * query and the project key.
	 * 
	 * @param query
	 *            JQL query.
	 * @param projectKey
	 *            of the Jira project.
	 * @return list of decision knowledge elements.
	 */
	public List<KnowledgeElement> getDecisionKnowledgeFromJira(String query, String projectKey) {
		String jsonString = getDecisionKnowledgeFromJiraAsJsonString(query, projectKey);
		return KnowledgeElement.parseJsonString(jsonString);
	}

	private String getDecisionKnowledgeFromJiraAsJsonString(String query, String projectKey) {
		return postResponseFromJiraWithApplicationLink("rest/condec/latest/knowledge/knowledgeElements.json",
				encodeUserInputQuery(query), projectKey);
	}

	/**
	 * @param message
	 *            that might contain a Jira issue key, e.g., a commit message,
	 *            branch name, or pull request title.
	 * @return list of all mentioned Jira issue keys in a message in upper case
	 *         letters (is ordered by their appearance in the message).
	 */
	static Set<String> getJiraIssueKeys(String message) {
		Set<String> keys = new LinkedHashSet<String>();
		String[] words = message.split("[\\s,:]+");
		String projectKey = null;
		for (String word : words) {
			word = word.toUpperCase(Locale.ENGLISH);
			if (word.contains("-")) {
				if (projectKey == null) {
					projectKey = word.split("-")[0];
				}
				if (word.startsWith(projectKey)) {
					keys.add(word);
				}
			}
		}
		return keys;
	}

	/**
	 * @param jiraIssueKeys
	 *            as a set of strings.
	 * @return potential Jira project key (e.g. CONDEC).
	 */
	public static String retrieveProjectKey(Set<String> jiraIssueKeys) {
		Set<String> projectKeys = JiraClient.instance.getJiraProjects();
		if (jiraIssueKeys == null || jiraIssueKeys.isEmpty()) {
			return "";
		}
		for (String jiraIssueKey : jiraIssueKeys) {
			String potentialProjectKey = jiraIssueKey.split("-")[0];
			if (isProjectKeyExisting(potentialProjectKey, projectKeys)) {
				return potentialProjectKey;
			}
		}
		return "";
	}

	public static boolean isProjectKeyExisting(String projectKey, Set<String> projectKeys) {
		return !projectKey.isEmpty() && projectKeys.contains(projectKey);
	}

	public static String getJiraCallQuery(Set<String> jiraIssueKeys) {
		String query = "?jql=key in (";
		Iterator<String> iterator = jiraIssueKeys.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			query += key;
			if (iterator.hasNext()) {
				query += ",";
			}
		}
		query += ")";
		return encodeUserInputQuery(query);
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
