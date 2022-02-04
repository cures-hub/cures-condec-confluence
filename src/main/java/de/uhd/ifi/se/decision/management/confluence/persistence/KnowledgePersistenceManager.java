package de.uhd.ifi.se.decision.management.confluence.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

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

	@ConfluenceImport
	private static BandanaManager bandanaManager;
	private static BandanaContext bandanaContext;

	@Inject
	public KnowledgePersistenceManager(BandanaManager bandanaManager) {
		setBandanaManager(bandanaManager);
		setBandanaContext(new ConfluenceBandanaContext("knowledge"));
	}

	public static void addKnowledgeElement(KnowledgeElement knowledgeElement) {
		bandanaManager.setValue(bandanaContext, knowledgeElement.getId(), knowledgeElement);
	}

	public static void removeKnowledgeElement(String id) {
		bandanaManager.removeValue(bandanaContext, id);
	}

	public static List<KnowledgeElement> getElements(int pageId, String macroId) {
		List<KnowledgeElement> elements = new ArrayList<KnowledgeElement>();
		if (pageId == 0 || macroId == null) {
			return elements;
		}

		for (String id : bandanaManager.getKeys(bandanaContext)) {
			KnowledgeElement knowledgeElement = readElementFromBandana(id);
			if (knowledgeElement == null) {
				continue;
			}
			// add only if the page id and Macro id corresponds
			// if macro id is null return all from page
			if (knowledgeElement.getPageId() == pageId && knowledgeElement.getMacroId() != null
					&& (knowledgeElement.getMacroId().equals(macroId)) || macroId == null) {
				elements.add(knowledgeElement);
			}
		}
		return elements;
	}

	public static void removeKnowledgeElements(int pageId, String macroId) {
		for (String id : bandanaManager.getKeys(bandanaContext)) {
			KnowledgeElement knowledgeElement = readElementFromBandana(id);
			if (knowledgeElement == null) {
				continue;
			}
			// remove only if the page id and Macro id corresponds
			// remove all where macroId is null
			String issueMacroId = knowledgeElement.getMacroId();

			if (isNullOrEmpty(issueMacroId) || (!isNullOrEmpty(issueMacroId) && knowledgeElement.getPageId() == pageId
					&& issueMacroId.equals(macroId))) {
				removeKnowledgeElement(knowledgeElement.getId());
			}
		}
	}

	private static KnowledgeElement readElementFromBandana(String key) {
		Object storedObject = bandanaManager.getValue(bandanaContext, key);
		if (!(storedObject instanceof KnowledgeElement)) {
			return null;
		}
		return (KnowledgeElement) storedObject;
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