package de.uhd.ifi.se.decision.management.confluence.mocks;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.applinks.api.ApplicationId;
import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.ApplicationLinkService;
import com.atlassian.applinks.api.ApplicationType;
import com.atlassian.applinks.api.TypeNotInstalledException;

/**
 * Mock class for the ApplicationLinkService.
 */
public class MockApplicationLinkService implements ApplicationLinkService {

	private List<ApplicationLink> applicationLinks;

	public MockApplicationLinkService() {
		this.applicationLinks = new ArrayList<ApplicationLink>();
		this.applicationLinks.add(new MockApplicationLink());
	}

	@Override
	public ApplicationLink getApplicationLink(ApplicationId arg0) throws TypeNotInstalledException {
		return applicationLinks.get(0);
	}

	@Override
	public Iterable<ApplicationLink> getApplicationLinks() {
		return applicationLinks;
	}

	@Override
	public Iterable<ApplicationLink> getApplicationLinks(Class<? extends ApplicationType> arg0) {
		return applicationLinks;
	}

	@Override
	public ApplicationLink getPrimaryApplicationLink(Class<? extends ApplicationType> arg0) {
		return applicationLinks.get(0);
	}

}
