package de.uhd.ifi.se.decision.management.confluence.model;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import de.uhd.ifi.se.decision.management.confluence.model.impl.DecisionKnowledgeElementImpl;

/**
 * Interface for decision knowledge elements
 */
@JsonDeserialize(as = DecisionKnowledgeElementImpl.class)
public interface DecisionKnowledgeElement {

	int getPageId();

	void setPageId(int pageId);

	String getId();

	void setId(String id);

	String getSummary();

	void setSummary(String summary);

	String getType();

	void setType(String type);

	String getKey();

	void setKey(String key);

	String getLink();

	void setLink(String link);

	String getMacroId();

	void setMacroId(String macroId);

	int getGroup();

	void setGroup(Integer group);

	String getDescription();

	void setDescription(String description);
}