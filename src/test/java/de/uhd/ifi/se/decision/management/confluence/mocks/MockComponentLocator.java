package de.uhd.ifi.se.decision.management.confluence.mocks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.atlassian.sal.api.component.ComponentLocator;

public class MockComponentLocator extends ComponentLocator {

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T getComponentInternal(Class<T> iface) {
		return (T) new MockApplicationLinkService();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T getComponentInternal(Class<T> iface, String componentKey) {
		return (T) new MockApplicationLinkService();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> Collection<T> getComponentsInternal(Class<T> iface) {
		List<T> components = new ArrayList<>();
		components.add((T) new MockApplicationLinkService());
		return components;
	}

}
