package de.uhd.ifi.se.decision.management.confluence.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import de.uhd.ifi.se.decision.management.confluence.model.impl.DecisionKnowledgeElementImpl;

/**
 * Interface for decision knowledge elements
 */
@JsonDeserialize(as = DecisionKnowledgeElementImpl.class)
public interface DecisionKnowledgeElement {

	public static List<DecisionKnowledgeElement> parseJsonString(String jsonString) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writerWithDefaultPrettyPrinter();

		List<DecisionKnowledgeElement> elements = new ArrayList<DecisionKnowledgeElement>();

		try {
			elements = objectMapper.readValue(jsonString, objectMapper.getTypeFactory()
					.constructCollectionType(List.class, DecisionKnowledgeElementImpl.class));
		} catch (JsonMappingException e) {
			try {
				List<DecisionKnowledgeElement[]> myelements = objectMapper.readValue(jsonString, objectMapper
						.getTypeFactory().constructCollectionType(List.class, DecisionKnowledgeElementImpl[].class));
				elements = Arrays.asList(myelements.get(0));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return elements;
	}

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

	String getDescription();

	void setDescription(String description);
}