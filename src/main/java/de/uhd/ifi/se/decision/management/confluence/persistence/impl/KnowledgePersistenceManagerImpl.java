package de.uhd.ifi.se.decision.management.confluence.persistence.impl;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.bandana.BandanaContext;
import com.atlassian.bandana.BandanaManager;
import com.atlassian.confluence.setup.bandana.ConfluenceBandanaContext;
import com.atlassian.spring.container.ContainerManager;

import de.uhd.ifi.se.decision.management.confluence.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.confluence.persistence.KnowledgePersistenceManager;

public class KnowledgePersistenceManagerImpl implements KnowledgePersistenceManager {
	// We are using the Confluence BandanaManager for persistent storage. For
	// information, see:
	// http://docs.atlassian.com/atlassian-bandana/0.2.0/com/atlassian/bandana/BandanaManager.html
	private BandanaManager bandanaManager;
	// The context for the BandanaManager.
	private final BandanaContext bandanaContext;

	private static KnowledgePersistenceManager INSTANCE;

	public static KnowledgePersistenceManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new KnowledgePersistenceManagerImpl();
		}
		return INSTANCE;
	}

	// Our constructor is private, such that it can only be called from within our
	// getInstance
	// method.
	private KnowledgePersistenceManagerImpl() {
		ContainerManager.autowireComponent(this);
		this.bandanaContext = new ConfluenceBandanaContext("decisionknowledgeelementkeeping");
	}

	@Override
	public void addDecisionKnowledgeElement(DecisionKnowledgeElement decisionKnowledgeElement) {
		bandanaManager.setValue(this.bandanaContext, decisionKnowledgeElement.getId(), decisionKnowledgeElement);
	}

	@Override
	public void removeDecisionKnowledgeElement(String id) {
		bandanaManager.removeValue(this.bandanaContext, id);
	}

	@Override
	public List<DecisionKnowledgeElement> getElementsFromPageIdAndMacroId(int pageId, String macroId) {
		List<DecisionKnowledgeElement> myJsonArray = new ArrayList<DecisionKnowledgeElement>();

		for (String id : this.bandanaManager.getKeys(this.bandanaContext)) {
			DecisionKnowledgeElement decisionKnowledgeElement = (DecisionKnowledgeElement) this.bandanaManager
					.getValue(this.bandanaContext, id);
			// add only if the page id and Macro id corresponds // if macro id is null
			// return all from page
			if (decisionKnowledgeElement.getPageId() == pageId
					&& (decisionKnowledgeElement.getMacroId().equals(macroId)) || macroId == null) {
				myJsonArray.add(decisionKnowledgeElement);
			}
		}
		return myJsonArray;
	}

	@Override
	public List<ArrayList<DecisionKnowledgeElement>> getElementsGroupedFromPageIdAndMacroId(int pageId,
			String macroId) {
		List<DecisionKnowledgeElement> unsortedJsonIssues = getElementsFromPageIdAndMacroId(pageId, macroId);
		List<ArrayList<DecisionKnowledgeElement>> returnArray = new ArrayList<ArrayList<DecisionKnowledgeElement>>();

		for (int j = 0; j < unsortedJsonIssues.size(); j++) {
			ArrayList<DecisionKnowledgeElement> groupArray = new ArrayList<DecisionKnowledgeElement>();

			for (int i = 0; i < unsortedJsonIssues.size(); i++) {
				DecisionKnowledgeElement decisionKnowledgeElement = unsortedJsonIssues.get(i);
				if (decisionKnowledgeElement.getGroup() == j) {
					groupArray.add(decisionKnowledgeElement);
				}
			}
			if (groupArray.size() > 0) {
				returnArray.add(groupArray);
			}
		}
		return returnArray;
	}

	@Override
	public void removeDecisionKnowledgeElement(int pageId, String macroId) {
		for (String id : this.bandanaManager.getKeys(this.bandanaContext)) {
			DecisionKnowledgeElement decisionKnowledgeElement = (DecisionKnowledgeElement) this.bandanaManager
					.getValue(this.bandanaContext, id);
			// remove only if the page id and Macro id corresponds remove all where macroId
			// is null
			String issueMacroId = decisionKnowledgeElement.getMacroId();

			if (isNullOrEmpty(issueMacroId) || (!isNullOrEmpty(issueMacroId)
					&& decisionKnowledgeElement.getPageId() == pageId && issueMacroId.equals(macroId))) {
				this.removeDecisionKnowledgeElement(decisionKnowledgeElement.getId());
			}
		}
	}

	// Getters and setters for the BandanaManager are called by Confluence
	// (injection).
	@Override
	public BandanaManager getBandanaManager() {
		return bandanaManager;
	}

	@Override
	public void setBandanaManager(BandanaManager bandanaManager) {
		this.bandanaManager = bandanaManager;
	}

	public static boolean isNullOrEmpty(String myString) {
		return myString == null || "".equals(myString);
	}

}