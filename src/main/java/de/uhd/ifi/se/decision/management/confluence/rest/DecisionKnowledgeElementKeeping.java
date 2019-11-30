package de.uhd.ifi.se.decision.management.confluence.rest;

import java.util.ArrayList;

import com.atlassian.bandana.BandanaContext;
import com.atlassian.bandana.BandanaManager;
import com.atlassian.confluence.setup.bandana.ConfluenceBandanaContext;
import com.atlassian.spring.container.ContainerManager;

public class DecisionKnowledgeElementKeeping {
	// We are using the Confluence BandanaManager for persistent storage. For
	// information, see:
	// http://docs.atlassian.com/atlassian-bandana/0.2.0/com/atlassian/bandana/BandanaManager.html
	private BandanaManager bandanaManager;
	// The context for the BandanaManager.
	private final BandanaContext bandanaContext;

	private static DecisionKnowledgeElementKeeping INSTANCE;

	public static DecisionKnowledgeElementKeeping getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DecisionKnowledgeElementKeeping();
		}
		return INSTANCE;
	}

	// Our constructor is private, such that it can only be called from within our
	// getInstance
	// method.
	private DecisionKnowledgeElementKeeping() {
		ContainerManager.autowireComponent(this);
		this.bandanaContext = new ConfluenceBandanaContext("decisionknowledgeelementkeeping");
	}

	public void addDecisionKnowledgeElement(DecisionKnowledgeElement decisionKnowledgeElement) {
		bandanaManager.setValue(this.bandanaContext, decisionKnowledgeElement.getId(), decisionKnowledgeElement);
	}

	public void removeDecisionKnowledgeElement(String id) {
		bandanaManager.removeValue(this.bandanaContext, id);
	}

	public ArrayList<DecisionKnowledgeElement> getElementsFromPageIdAndMacroId(int pageId, String macroId) {
		ArrayList<DecisionKnowledgeElement> myJsonArray = new ArrayList<DecisionKnowledgeElement>();

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

	public ArrayList<ArrayList<DecisionKnowledgeElement>> getElementsGroupedFromPageIdAndMacroId(int pageId,
			String macroId) {
		ArrayList<DecisionKnowledgeElement> unsortedJsonIssues = getElementsFromPageIdAndMacroId(pageId, macroId);
		ArrayList<ArrayList<DecisionKnowledgeElement>> returnArray = new ArrayList<ArrayList<DecisionKnowledgeElement>>();

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
	public BandanaManager getBandanaManager() {
		return bandanaManager;
	}

	public void setBandanaManager(BandanaManager bandanaManager) {
		this.bandanaManager = bandanaManager;
	}

	public static boolean isNullOrEmpty(String myString) {
		return myString == null || "".equals(myString);
	}

}