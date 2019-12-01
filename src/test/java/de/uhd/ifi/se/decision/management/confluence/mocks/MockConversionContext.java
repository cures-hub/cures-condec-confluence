package de.uhd.ifi.se.decision.management.confluence.mocks;

import java.util.Set;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.content.render.xhtml.XhtmlTimeoutException;
import com.atlassian.confluence.core.ContentEntityObject;
import com.atlassian.confluence.impl.content.render.xhtml.analytics.MarshallerMetricsConsumer;
import com.atlassian.confluence.pages.ContentTree;
import com.atlassian.confluence.renderer.PageContext;
import com.atlassian.confluence.setup.settings.GlobalDescription;
import com.atlassian.util.concurrent.Timeout;

public class MockConversionContext implements ConversionContext {

	@Override
	public void addMarshallerMetricsConsumer(MarshallerMetricsConsumer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkTimeout() throws XhtmlTimeoutException {
		// TODO Auto-generated method stub

	}

	@Override
	public void disableAsyncRenderSafe() {
		// TODO Auto-generated method stub

	}

	@Override
	public ContentTree getContentTree() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable ContentEntityObject getEntity() {
		return new GlobalDescription();
	}

	@Override
	public Set<MarshallerMetricsConsumer> getMarshallerMetricsConsumers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOutputDeviceType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @NonNull String getOutputType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageContext getPageContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getProperty(String arg0) {
		return null;
	}

	@Override
	public Object getProperty(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPropertyAsString(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSpaceKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timeout getTimeout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasProperty(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAsyncRenderSafe() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDiffOrEmail() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeMarshallerMetricsConsumer(MarshallerMetricsConsumer arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object removeProperty(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProperty(String arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

}
