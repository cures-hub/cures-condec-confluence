package de.uhd.ifi.se.decision.management.confluence.macro;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.atlassian.confluence.macro.Macro.BodyType;
import com.atlassian.confluence.macro.Macro.OutputType;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.sal.api.component.ComponentLocator;

import de.uhd.ifi.se.decision.management.confluence.mocks.MockBandanaManager;
import de.uhd.ifi.se.decision.management.confluence.mocks.MockComponentLocator;
import de.uhd.ifi.se.decision.management.confluence.mocks.MockConversionContext;
import de.uhd.ifi.se.decision.management.confluence.persistence.KnowledgePersistenceManager;

public class TestDecisionKnowledgeImportMacro {

	public DecisionKnowledgeImportMacro macro;

	@Before
	public void setUp() throws Exception {
		ComponentLocator.setComponentLocator(new MockComponentLocator());
		macro = new DecisionKnowledgeImportMacro();
		KnowledgePersistenceManager.setBandanaManager(new MockBandanaManager());
	}

	@Test
	public void testConstructor() {
		assertNotNull(new DecisionKnowledgeImportMacro());
	}

	@Ignore
	@Test(expected = ExceptionInInitializerError.class)
	public void testExecute() {
		try {
			macro.execute(null, null, new MockConversionContext());
		} catch (MacroExecutionException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetBodyType() {
		assertEquals(BodyType.NONE, macro.getBodyType());
	}

	@Test
	public void testGetOutputType() {
		assertEquals(OutputType.BLOCK, macro.getOutputType());
	}
}