package de.uhd.ifi.se.decision.management.confluence.persistence;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.bandana.BandanaManager;

import de.uhd.ifi.se.decision.management.confluence.model.DecisionKnowledgeElement;

/**
 * Interface to store the knowledge imported from Jira.
 */
public interface KnowledgePersistenceManager {

	void addDecisionKnowledgeElement(DecisionKnowledgeElement decisionKnowledgeElement);

	void removeDecisionKnowledgeElement(String id);

	List<DecisionKnowledgeElement> getElementsFromPageIdAndMacroId(int pageId, String macroId);

	List<ArrayList<DecisionKnowledgeElement>> getElementsGroupedFromPageIdAndMacroId(int pageId, String macroId);

	void removeDecisionKnowledgeElement(int pageId, String macroId);

	// Getters and setters for the BandanaManager are called by Confluence
	// (injection).
	BandanaManager getBandanaManager();

	void setBandanaManager(BandanaManager bandanaManager);
}