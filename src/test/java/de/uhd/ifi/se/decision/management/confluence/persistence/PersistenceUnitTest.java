package de.uhd.ifi.se.decision.management.confluence.persistence;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import de.uhd.ifi.se.decision.management.confluence.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.confluence.persistence.impl.KnowledgePersistenceManagerImpl;
import de.uhd.ifi.se.decision.management.confluence.rest.impl.KnowledgeRestImpl;

@Ignore
public class PersistenceUnitTest {
	int pageId = 999999999;
	public String macroId = "88888888";
	public KnowledgeRestImpl rest = new KnowledgeRestImpl();
	// this part fails
	public KnowledgePersistenceManager keeping = KnowledgePersistenceManagerImpl.getInstance();

	public String jsonString = "{'data':[[{'id':51900,'summary':'BUG: Fix Macro ID bug','type':'Bug','documentationLocation':'i','key':'CONCONDEC-11','url':'https://jira-se.ifi.uni-heidelberg.de/browse/CONCONDEC-11','description':'To enable multiple macros without saving the page first we need to retrieve the macro id from the Java Macro constructor'}],[{'id':44001,'summary':'BUG: Injection of LinkService not working','description':'an Application link to the Jira site has to exist as a precondition.\r\nThe following is not working:\r\n    @Inject\r\n    public ApiLinkService(ApplicationLinkService oApplicationLinkService) {\r\n        applicationLinkService = oApplicationLinkService;\r\n    }\r\nwith call:\r\nApiLinkService al= new ApiLinkService(null);\r\n\r\nThe injection is not working, as \'al\' is always null.\r\n','type':'Bug','documentationLocation':'i','key':'CONCONDEC-6','url':'https://jira-se.ifi.uni-heidelberg.de/browse/CONCONDEC-6'},{'id':153135,'summary':'some other summary','description':'some description','type':'buug','documentationLocation':'i','key':'CONCONDEC-8','url':'https://jira-se.ifi.uni-heidelberg.de/browse/CONCONDEC-8'}]],'url':'USE_OBJECT_URL','pageId':786433}{'data':[[{'id':51900,'summary':'BUG: Fix Macro ID bug','type':'Bug','documentationLocation':'i','key':'CONCONDEC-11','url':'https://jira-se.ifi.uni-heidelberg.de/browse/CONCONDEC-11','description':'To enable multiple macros without saving the page first we need to retrieve the macro id from the Java Macro constructor'}],[{'id':44001,'summary':'BUG: Injection of LinkService not working','description':'an Application link to the Jira site has to exist as a precondition.\r\nThe following is not working:\r\n    @Inject\r\n    public ApiLinkService(ApplicationLinkService oApplicationLinkService) {\r\n        applicationLinkService = oApplicationLinkService;\r\n    }\r\nwith call:\r\nApiLinkService al= new ApiLinkService(null);\r\n\r\nThe injection is not working, as \'al\' is always null.\r\n','type':'Bug','documentationLocation':'i','key':'CONCONDEC-6','url':'https://jira-se.ifi.uni-heidelberg.de/browse/CONCONDEC-6'},{'id':153135,'summary':'some other summary','description':'some description','type':'buug','documentationLocation':'i','key':'CONCONDEC-8','url':'https://jira-se.ifi.uni-heidelberg.de/browse/CONCONDEC-8'}]],'url':'USE_OBJECT_URL','pageId':786433}";

	@Test
	public void testAddData() {
		// first step save the data
		Boolean handleRequest = rest.handlePostRequestResult(pageId, macroId, jsonString);
		assertEquals("Handle Request Failed!", true, handleRequest);
	}

	@Test
	public void testCheckData() {
		// assert the data is in the db
		List<ArrayList<DecisionKnowledgeElement>> elements = keeping.getElementsGroupedFromPageIdAndMacroId(pageId,
				macroId);
		assertEquals("Array size not equal the tested", 2, elements.size());
	}

	@Test
	public void testRemoveData() {
		// remove the data
		keeping.removeDecisionKnowledgeElement(pageId, macroId);
		// assert the data is not longer in the db
		List<ArrayList<DecisionKnowledgeElement>> elements = keeping.getElementsGroupedFromPageIdAndMacroId(pageId,
				macroId);
		assertEquals("Removing the data did not work", 0, elements.size());
	}
}