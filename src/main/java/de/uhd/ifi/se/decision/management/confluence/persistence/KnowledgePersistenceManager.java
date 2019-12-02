package de.uhd.ifi.se.decision.management.confluence.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.atlassian.bandana.BandanaContext;
import com.atlassian.bandana.BandanaManager;
import com.atlassian.confluence.setup.bandana.ConfluenceBandanaContext;
import com.atlassian.plugin.spring.scanner.annotation.imports.ConfluenceImport;

import de.uhd.ifi.se.decision.management.confluence.model.DecisionKnowledgeElement;

/*
 * @issue How to store the knowledge in Confluence?
 * @decision Use the Confluence BandanaManager for persistent storage! 
 * See http://docs.atlassian.com/atlassian-bandana/0.2.0/com/atlassian/bandana/BandanaManager.html
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

	public static void addDecisionKnowledgeElement(DecisionKnowledgeElement decisionKnowledgeElement) {
		bandanaManager.setValue(bandanaContext, decisionKnowledgeElement.getId(), decisionKnowledgeElement);
	}

	public static void removeDecisionKnowledgeElement(String id) {
		bandanaManager.removeValue(bandanaContext, id);
	}

	public static List<DecisionKnowledgeElement> getElements(int pageId, String macroId) {
		List<DecisionKnowledgeElement> elements = new ArrayList<DecisionKnowledgeElement>();

		for (String id : bandanaManager.getKeys(bandanaContext)) {
			DecisionKnowledgeElement decisionKnowledgeElement = (DecisionKnowledgeElement) bandanaManager
					.getValue(bandanaContext, id);
			// add only if the page id and Macro id corresponds // if macro id is null
			// return all from page
			if (decisionKnowledgeElement != null && decisionKnowledgeElement.getPageId() == pageId
					&& decisionKnowledgeElement.getMacroId() != null
					&& (decisionKnowledgeElement.getMacroId().equals(macroId)) || macroId == null) {
				elements.add(decisionKnowledgeElement);
			}
		}
		return elements;
	}

	public static void removeDecisionKnowledgeElements(int pageId, String macroId) {
		for (String id : bandanaManager.getKeys(bandanaContext)) {
			DecisionKnowledgeElement decisionKnowledgeElement = (DecisionKnowledgeElement) bandanaManager
					.getValue(bandanaContext, id);
			// remove only if the page id and Macro id corresponds remove all where macroId
			// is null
			String issueMacroId = decisionKnowledgeElement.getMacroId();

			if (isNullOrEmpty(issueMacroId) || (!isNullOrEmpty(issueMacroId)
					&& decisionKnowledgeElement.getPageId() == pageId && issueMacroId.equals(macroId))) {
				removeDecisionKnowledgeElement(decisionKnowledgeElement.getId());
			}
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