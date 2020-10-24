package de.uhd.ifi.se.decision.management.confluence.macro;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
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

	private DecisionKnowledgeImportMacro macro;
	private Map<String, String> map;

	@Before
	public void setUp() throws Exception {
		ComponentLocator.setComponentLocator(new MockComponentLocator());
		macro = new DecisionKnowledgeImportMacro();
		KnowledgePersistenceManager.setBandanaManager(new MockBandanaManager());
		map = new HashMap<String, String>();
		map.put("project", "CONDEC");
		map.put("substring", "");
		map.put("startDate", "");
		map.put("endDate", "");
		map.put("unresolvedDecisionProblems", "true");
		map.put("resolvedDecisionProblems", "true");
		map.put("decisions", "true");
		map.put("alternatives", "true");
		map.put("arguments", "false");
		map.put("freeze", "false");
	}

	@Test
	public void testConstructor() {
		assertNotNull(new DecisionKnowledgeImportMacro());
	}

	@Test(expected = Exception.class)
	public void testExecute() {
		try {
			macro.execute(map, null, new MockConversionContext());
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