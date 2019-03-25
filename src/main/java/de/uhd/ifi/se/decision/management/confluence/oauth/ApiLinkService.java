package de.uhd.ifi.se.decision.management.confluence.oauth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.inject.Inject;
import javax.inject.Named;

import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.ApplicationLinkRequest;
import com.atlassian.applinks.api.ApplicationLinkRequestFactory;
import com.atlassian.applinks.api.ApplicationLinkResponseHandler;
import com.atlassian.applinks.api.ApplicationLinkService;
import com.atlassian.applinks.api.CredentialsRequiredException;
import com.atlassian.applinks.api.application.jira.JiraApplicationType;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.net.Request;
import com.atlassian.sal.api.net.Response;
import com.atlassian.sal.api.net.ResponseException;

@Named("ComponentUtil")
public class ApiLinkService {

	@ComponentImport
	static private ApplicationLinkService applicationLinkService;

	@Inject
	public ApiLinkService(ApplicationLinkService oApplicationLinkService) {
		applicationLinkService = oApplicationLinkService;
	}

	static public String makeGetRequestToJira(String query, String projectKey) {
		// sanitise query
		String encodedQuery = encodeUserInputQuery(query);
		return getResponseFromJiraWithApplicationLink(
				"rest/decisions/latest/decisions/getElements.json?allTrees=true&projectKey=" + projectKey + "&query="
						+ encodedQuery);
	}

	static public String getCurrentActiveJiraProjects() {
		return getResponseFromJiraWithApplicationLink("rest/api/2/project");
	}

	private static String getResponseFromJiraWithApplicationLink(String jiraUrl) {
		String responseBody = "";
		try {
			ApplicationLink jiraApplicationLink = applicationLinkService
					.getPrimaryApplicationLink(JiraApplicationType.class);
			ApplicationLinkRequestFactory requestFactory = jiraApplicationLink.createAuthenticatedRequestFactory();
			ApplicationLinkRequest request = requestFactory.createRequest(Request.MethodType.GET, jiraUrl);
			request.addHeader("Content-Type", "application/json");
			responseBody = request.executeAndReturn(new ApplicationLinkResponseHandler<String>() {
				public String credentialsRequired(final Response response) throws ResponseException {
					return response.getResponseBodyAsString();
				}

				public String handle(final Response response) throws ResponseException {
					return response.getResponseBodyAsString();
				}
			});
		} catch (CredentialsRequiredException | ResponseException e) {
			e.printStackTrace();
		}
		return responseBody;
	}

	public ApplicationLinkService getApplicationLinkService() {
		return applicationLinkService;
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
