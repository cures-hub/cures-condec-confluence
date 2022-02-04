package de.uhd.ifi.se.decision.management.confluence.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.atlassian.sal.api.component.ComponentLocator;

import de.uhd.ifi.se.decision.management.confluence.mocks.MockBandanaManager;
import de.uhd.ifi.se.decision.management.confluence.mocks.MockComponentLocator;
import de.uhd.ifi.se.decision.management.confluence.model.KnowledgeElement;

public class TestKnowledgePersistenceManager {

	private KnowledgeElement element;
	private String jsonString;

	@Before
	public void setUp() {
		ComponentLocator.setComponentLocator(new MockComponentLocator());
		jsonString = "[{\"link\":\"https://jira-se.ifi.uni-heidelberg.de/browse/ISE2019-103\",\"pageId\":122191874,\"summary\":\"link type to devices\",\"type\":\"Sub-task\",\"key\":\"ISE2019-103\",\"id\":\"1911281118291129ISE2019-103\",\"macroId\":\"28e02c88-0754-40de-9df1-ac403833dd38\",\"description\":\"Link new created type to different devices inside the pop up field.\\r\\n{issue}Should the user be able to add the new masterdata type to other devices while creating it?{issue}\\r\\n{pro}Most of the time, masterdata types aren`t created just for one single device. Adjusting it during creation is intuitive.{pro}\"}]";
		List<KnowledgeElement> elements = KnowledgeElement.parseJsonString(jsonString);
		element = elements.get(0);
		KnowledgePersistenceManager.setBandanaManager(new MockBandanaManager());
	}

	@Test
	public void testConstructor() {
		KnowledgePersistenceManager persistenceManager = new KnowledgePersistenceManager(new MockBandanaManager());
		assertNotNull(persistenceManager);
	}

	@Test
	public void testAddElementsViaJson() {
		KnowledgePersistenceManager.addKnowledgeElements(jsonString, 23);
		List<KnowledgeElement> storedElements = KnowledgePersistenceManager.getElements(23);
		assertEquals(element, storedElements.get(0));
	}

	@Test
	public void testGetElementsInvalidPageId() {
		KnowledgePersistenceManager.addKnowledgeElements(jsonString, 23);
		List<KnowledgeElement> storedElements = KnowledgePersistenceManager.getElements(0);
		assertEquals(0, storedElements.size());
	}

	@Test
	public void testGetElementsInvalidMacroId() {
		List<KnowledgeElement> storedElements = KnowledgePersistenceManager.getElements(42);
		assertEquals(0, storedElements.size());
	}

	@Test
	public void testRemoveElements() {
		List<KnowledgeElement> elements = new ArrayList<>();
		elements.add(element);
		KnowledgePersistenceManager.addKnowledgeElements(elements, 23);
		KnowledgePersistenceManager.removeKnowledgeElements(23);
		List<KnowledgeElement> storedElements = KnowledgePersistenceManager.getElements(23);
		assertEquals(0, storedElements.size());
	}

	@Test
	public void testIsNullOrEmptyFalse() {
		boolean isEmpty = KnowledgePersistenceManager.isNullOrEmpty("abcd");
		assertEquals(false, isEmpty);
	}

	@Test
	public void testIsNullOrEmptyTrue() {
		boolean isEmpty = KnowledgePersistenceManager.isNullOrEmpty("");
		assertEquals(true, isEmpty);
	}

	@Test
	public void testIsNullOrEmptyNullTrue() {
		boolean isEmpty = KnowledgePersistenceManager.isNullOrEmpty(null);
		assertEquals(true, isEmpty);
	}
}