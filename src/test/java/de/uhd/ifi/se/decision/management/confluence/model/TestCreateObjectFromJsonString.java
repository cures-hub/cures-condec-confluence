package de.uhd.ifi.se.decision.management.confluence.model;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class TestCreateObjectFromJsonString {

	@Test
	public void testParseJsonString() {
		String jsonString = "[{\"link\":\"https://jira-se.ifi.uni-heidelberg.de/browse/ISE2019-103\",\"pageId\":122191874,\"summary\":\"link type to devices\",\"type\":\"Sub-task\",\"key\":\"ISE2019-103\",\"id\":\"1911281118291129ISE2019-103\",\"macroId\":\"28e02c88-0754-40de-9df1-ac403833dd38\",\"group\":0,\"description\":\"Link new created type to different devices inside the pop up field.\\r\\n{issue}Should the user be able to add the new masterdata type to other devices while creating it?{issue}\\r\\n{pro}Most of the time, masterdata types aren`t created just for one single device. Adjusting it during creation is intuitive.{pro}\"}]";

		List<DecisionKnowledgeElement> elements = DecisionKnowledgeElement.parseJsonString(jsonString);
		DecisionKnowledgeElement element = elements.get(0);
		assertEquals("link type to devices", element.getSummary());
		assertEquals(122191874, element.getPageId());
		assertEquals("ISE2019-103", element.getKey());
		assertEquals("https://jira-se.ifi.uni-heidelberg.de/browse/ISE2019-103", element.getLink());
	}
}
