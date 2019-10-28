package de.uhd.ifi.se.decision.management.confluence.mocks;

import java.net.URI;
import java.util.List;
import java.util.Map;

import com.atlassian.applinks.api.ApplicationId;
import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.ApplicationLinkRequest;
import com.atlassian.applinks.api.ApplicationLinkRequestFactory;
import com.atlassian.applinks.api.ApplicationLinkResponseHandler;
import com.atlassian.applinks.api.ApplicationType;
import com.atlassian.applinks.api.CredentialsRequiredException;
import com.atlassian.applinks.api.auth.AuthenticationProvider;
import com.atlassian.sal.api.net.Request.MethodType;
import com.atlassian.sal.api.net.RequestFilePart;
import com.atlassian.sal.api.net.Response;
import com.atlassian.sal.api.net.ResponseException;
import com.atlassian.sal.api.net.ResponseHandler;
import com.atlassian.sal.api.net.ReturningResponseHandler;

/**
 * Mock class the for an application link to a Jira server.
 */
public class MockApplicationLink implements ApplicationLink {

	public static boolean IS_DOCUMENTATION_COMPLETE = true;

	public String mockResponseByUrl(String url) {
		if (url.endsWith("project")) {
			return "[ { 'key' : 'CONDEC' } ]";
		}
		if (IS_DOCUMENTATION_COMPLETE) {
			return "[[{'type':'issue'}, {'type':'decision'}]]";
		}
		return "[[{'key' : 'CONDEC-1', 'type':'issue'}]]";
	}

	@Override
	public ApplicationId getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApplicationType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URI getDisplayUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URI getRpcUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPrimary() {
		return true;
	}

	@Override
	public boolean isSystem() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ApplicationLinkRequestFactory createAuthenticatedRequestFactory() {
		return new ApplicationLinkRequestFactory() {

			@Override
			public URI getAuthorisationURI(URI callback) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public URI getAuthorisationURI() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ApplicationLinkRequest createRequest(MethodType methodType, String url)
					throws CredentialsRequiredException {
				return new ApplicationLinkRequest() {

					@Override
					public ApplicationLinkRequest setConnectionTimeout(int connectionTimeout) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public ApplicationLinkRequest setSoTimeout(int soTimeout) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public ApplicationLinkRequest setUrl(String url) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public ApplicationLinkRequest setRequestBody(String requestBody) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public ApplicationLinkRequest setRequestBody(String requestBody, String contentType) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public ApplicationLinkRequest setFiles(List<RequestFilePart> files) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public ApplicationLinkRequest setEntity(Object entity) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public ApplicationLinkRequest addRequestParameters(String... params) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public ApplicationLinkRequest addBasicAuthentication(String hostname, String username,
							String password) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public ApplicationLinkRequest addHeader(String headerName, String headerValue) {
						return this;
					}

					@Override
					public ApplicationLinkRequest setHeader(String headerName, String headerValue) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public ApplicationLinkRequest setFollowRedirects(boolean follow) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Map<String, List<String>> getHeaders() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public void execute(ResponseHandler<? super Response> responseHandler) throws ResponseException {
						// TODO Auto-generated method stub

					}

					@Override
					public String execute() throws ResponseException {
						// TODO Auto-generated method stub
						return null;
					}

					@SuppressWarnings("unchecked")
					@Override
					public <RET> RET executeAndReturn(ReturningResponseHandler<? super Response, RET> responseHandler)
							throws ResponseException {
						return (RET) mockResponseByUrl(url);
					}

					@Override
					public <R> R execute(ApplicationLinkResponseHandler<R> responseHandler) throws ResponseException {
						// TODO Auto-generated method stub
						return null;
					}

				};
			}
		};
	}

	@Override
	public ApplicationLinkRequestFactory createAuthenticatedRequestFactory(
			Class<? extends AuthenticationProvider> providerClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApplicationLinkRequestFactory createImpersonatingAuthenticatedRequestFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApplicationLinkRequestFactory createNonImpersonatingAuthenticatedRequestFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object putProperty(String key, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object removeProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}
}