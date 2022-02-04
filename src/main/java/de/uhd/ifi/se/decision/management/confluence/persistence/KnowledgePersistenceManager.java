package de.uhd.ifi.se.decision.management.confluence.persistence;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.bandana.BandanaContext;
import com.atlassian.bandana.BandanaManager;
import com.atlassian.confluence.setup.bandana.ConfluenceBandanaContext;
import com.atlassian.plugin.spring.scanner.annotation.imports.ConfluenceImport;

import de.uhd.ifi.se.decision.management.confluence.model.KnowledgeElement;

/**
 * @issue How to store the knowledge in Confluence?
 * @decision Use the Confluence BandanaManager for persistent storage! See
 *           http://docs.atlassian.com/atlassian-bandana/0.2.0/com/atlassian/bandana/BandanaManager.html
 */
@Named
public class KnowledgePersistenceManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(KnowledgePersistenceManager.class);

	@ConfluenceImport
	private static BandanaManager bandanaManager;
	private static BandanaContext bandanaContext;

	@Inject
	public KnowledgePersistenceManager(BandanaManager bandanaManager) {
		setBandanaManager(bandanaManager);
		setBandanaContext(new ConfluenceBandanaContext("knowledge"));
	}

	public static void addKnowledgeElements(List<KnowledgeElement> elements, int pageId) {
		String jsonString = KnowledgeElement.toJsonString(elements);
		bandanaManager.setValue(bandanaContext, pageId + "", jsonString);
	}

	public static void addKnowledgeElements(String jsonString, int pageId) {
		bandanaManager.setValue(bandanaContext, pageId + "", jsonString);
	}

	public static void addKnowledgeElement(KnowledgeElement knowledgeElement) {
		LOGGER.error("add: " + knowledgeElement.getId() + knowledgeElement.getClass().getName());
		bandanaManager.setValue(bandanaContext, knowledgeElement.getId(), knowledgeElement);
	}

	public static void removeKnowledgeElement(String id) {
		bandanaManager.removeValue(bandanaContext, id);
	}

	public static List<KnowledgeElement> getElements(int pageId) {
		return KnowledgeElement.parseJsonString(getElementsAsJsonString(pageId));
	}

	public static String getElementsAsJsonString(int pageId) {
		String jsonString = "";
		LOGGER.error("keys: " + bandanaManager.getKeys(bandanaContext).toString());

		for (String id : bandanaManager.getKeys(bandanaContext)) {
			if (!id.equals(pageId + "")) {
				continue;
			}
			Object storedObject = bandanaManager.getValue(bandanaContext, id);
			if (!(storedObject instanceof String)) {
				LOGGER.error(storedObject.getClass().getName());
				bandanaManager.removeValue(bandanaContext, id);
				continue;
			}

			jsonString = (String) storedObject;
		}
		return jsonString;
	}

	public static void removeKnowledgeElements(int pageId) {
		for (KnowledgeElement element : getElements(pageId)) {
			removeKnowledgeElement(element.getPageId() + "");
		}
	}

	public static void setBandanaManager(BandanaManager bandanaManager) {
		KnowledgePersistenceManager.bandanaManager = bandanaManager;
	}

	public static void setBandanaContext(BandanaContext bandanaContext) {
		KnowledgePersistenceManager.bandanaContext = bandanaContext;
	}

	public static boolean isNullOrEmpty(String myString) {
		return myString == null || myString.isEmpty();
	}
}