package de.uhd.ifi.se.decision.management.confluence.macro;

import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

import com.atlassian.confluence.macro.MacroExecutionException;

public class TestDecisionKnowledgeImportMacro {

	@Test
	public void testConstructor() {
		assertNotNull(new DecisionKnowledgeImportMacro(null));
	}

	@Test
	@Ignore
	public void testExecute() {
		DecisionKnowledgeImportMacro macro = new DecisionKnowledgeImportMacro(null);
		try {
			macro.execute(null, null, null);
		} catch (MacroExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
