package de.uhd.ifi.se.decision.management.confluence.macro;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.sal.api.component.ComponentLocator;

import de.uhd.ifi.se.decision.management.confluence.mocks.MockComponentLocator;
import de.uhd.ifi.se.decision.management.confluence.mocks.MockConversionContext;
import de.uhd.ifi.se.decision.management.confluence.mocks.MockPageBuilderService;

public class TestDecisionKnowledgeImportMacro {

	public DecisionKnowledgeImportMacro macro;

	@Before
	public void setUp() throws Exception {
		ComponentLocator.setComponentLocator(new MockComponentLocator());
		macro = new DecisionKnowledgeImportMacro(new MockPageBuilderService());
	}

	@Test
	public void testConstructor() {
		assertNotNull(new DecisionKnowledgeImportMacro(null));
	}

	@Test(expected = NullPointerException.class)
	public void testExecute() {
		try {
			macro.execute(null, null, new MockConversionContext());
		} catch (MacroExecutionException e) {
			e.printStackTrace();
		}
	}

}
