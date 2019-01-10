package de.uhd.ifi.se.decision.management.confluence.oauth;

import com.atlassian.applinks.api.*;
import com.atlassian.applinks.api.application.jira.JiraApplicationType;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.net.Request;

import javax.inject.Inject;

public class ApiLinkService {

    @ComponentImport
    private static ApplicationLinkService applicationLinkService;

    @Inject
    public ApiLinkService(ApplicationLinkService oApplicationLinkService) {
        applicationLinkService = oApplicationLinkService;
    }

   static public void makeGetRequestToJira() {
        try {
            ApplicationLink jiraApplicationLink = applicationLinkService.getPrimaryApplicationLink(JiraApplicationType.class);
            ApplicationLinkRequestFactory requestFactory = jiraApplicationLink.createAuthenticatedRequestFactory();
            ApplicationLinkRequest request = requestFactory.createRequest(Request.MethodType.GET, "rest/api/2/search?jql=");
        } catch (CredentialsRequiredException e) {
            e.printStackTrace();
        }
    }
}


