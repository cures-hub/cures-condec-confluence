package de.uhd.ifi.se.decision.management.confluence.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.google.gson.Gson;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KnowledgeElement implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(KnowledgeElement.class);

	@XmlElement
	private String link;
	@XmlElement
	private int pageId;
	@XmlElement
	private String summary;
	@XmlElement
	private String type;
	@XmlElement
	private String key;
	@XmlElement
	private String id;
	@XmlElement
	private String macroId;
	@XmlElement
	private String description;
	@XmlElement
	private String creator;
	@XmlElement
	private String updatingDate;
	@XmlElement
	private String status;
	@XmlElement
	private List<String> groups;

	/**
	 * @issue How can we convert a JSON string into a list of KnowledgeElement
	 *        objects?
	 * @decision Convert a JSON string into a list of KnowledgeElement objects
	 *           manually using the GSON library!
	 * @con This is not very elegant and hard to understand. Their might be an
	 *      easier way of mapping JSON Strings into objects.
	 */
	public static List<KnowledgeElement> parseJsonString(String jsonString) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writerWithDefaultPrettyPrinter();

		List<KnowledgeElement> elements = new ArrayList<KnowledgeElement>();

		try {
			elements = objectMapper.readValue(jsonString,
					objectMapper.getTypeFactory().constructCollectionType(List.class, KnowledgeElement.class));
		} catch (JsonMappingException e) {
			try {
				Gson g = new Gson();
				KnowledgeElement[] myelements = g.fromJson(jsonString.substring(1, jsonString.length() - 1),
						KnowledgeElement[].class);
				elements = Arrays.asList(myelements);
			} catch (Exception e1) {
				LOGGER.error(e1.getMessage());
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		return elements;
	}

	public KnowledgeElement(String link, int pageId, String summary, String type, String key, String description,
			String macroId) {
		this.pageId = pageId;
		this.summary = summary;
		this.type = type;
		this.key = key;
		this.link = link;
		this.description = description;
		this.macroId = macroId;

		// generate unique id
		Date dNow = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyMMddhhmmssMs");
		String datetime = format.format(dNow);
		this.id = datetime + key;
	}

	public KnowledgeElement() {
	}

	public int getPageId() {
		return pageId;
	}

	public void setPageId(int pageId) {
		this.pageId = pageId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@JsonProperty("url")
	public void setUrl(String link) {
		this.setLink(link);
	}

	public String getMacroId() {
		return macroId;
	}

	public void setMacroId(String macroId) {
		this.macroId = macroId;
	}

	public String getDescription() {
		return description != null ? description : getSummary();
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreator() {
		return creator;
	}

	@JsonProperty("creator")
	public void setCreator(String name) {
		this.creator = name;
	}

	public String getUpdatingDate() {
		return updatingDate;
	}

	@JsonProperty("latestUpdatingDate")
	@JsonAlias("updatingDate")
	public void setUpdatingDate(String epochTime) {
		Date date = new Date(Long.parseLong(epochTime));
		this.updatingDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	public String getStatus() {
		if (status == null) {
			return "undefined";
		}
		return status;
	}

	public String getStatusColor() {
		String status = getStatus();
		if ("unresolved".equals(status) || "challenged".equals(status)) {
			return "crimson";
		}
		if ("discarded".equals(status) || "rejected".equals(status)) {
			return "gray";
		}
		return "black";
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("groups")
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	public String getGroups() {
		if (groups == null) {
			groups = new ArrayList<>();
		}
		return StringUtils.join(groups, ", ");
	}
}