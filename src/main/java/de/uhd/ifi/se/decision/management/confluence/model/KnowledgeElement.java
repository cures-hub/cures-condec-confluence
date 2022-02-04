package de.uhd.ifi.se.decision.management.confluence.model;

import java.io.Serializable;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KnowledgeElement implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(KnowledgeElement.class);

	private String link;
	private int pageId;
	private String summary;
	private String type;
	private String key;
	private String id;
	private String macroId;
	private String description;
	private String creator;
	private String latestUpdatingDate;
	private String status;
	private List<String> groups;
	private String latestAuthor;

	/**
	 * @issue How can we convert a JSON string into a list of KnowledgeElement
	 *        objects?
	 * @decision Convert a JSON string into a list of KnowledgeElement objects
	 *           manually using the GSON library!
	 * @con This is not very elegant and hard to understand. There might be an
	 *      easier way of mapping JSON Strings into objects.
	 */
	public static List<KnowledgeElement> parseJsonString(String jsonString) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writerWithDefaultPrettyPrinter();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		List<KnowledgeElement> elements = new ArrayList<KnowledgeElement>();
		try {
			elements = objectMapper.readValue(jsonString, new TypeReference<List<KnowledgeElement>>() {
			});
		} catch (Exception e) {
			System.out.println(e.getMessage());
			LOGGER.error(e.getMessage());
		}

		return elements;
	}

	public static String toJsonString(List<KnowledgeElement> elements) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		objectMapper.writerWithDefaultPrettyPrinter();
		String jsonString = "";
		try {
			jsonString = objectMapper.writeValueAsString(elements);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return jsonString;
	}

	public KnowledgeElement() {
		// generate unique id
		Date dNow = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyMMddhhmmssMs");
		String datetime = format.format(dNow);
		this.id = datetime + key;
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
		if (link != null) {
			return URLDecoder.decode(link, Charset.defaultCharset());
		}
		return "";
	}

	public void setLink(String link) {
		this.link = link;
	}

	@JsonProperty("url")
	public void setUrl(String link) {
		setLink(link);
	}

	@XmlElement
	public String getMacroId() {
		return macroId;
	}

	@JsonProperty
	public void setMacroId(String macroId) {
		this.macroId = macroId;
	}

	public String getDescription() {
		return description != null ? description : getSummary();
	}

	@JsonProperty
	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreator() {
		return creator;
	}

	@JsonProperty
	public void setCreator(String name) {
		this.creator = name;
	}

	public String getUpdatingDate() {
		if (latestUpdatingDate == null) {
			return "";
		}
		Date date = new Date(Long.parseLong(latestUpdatingDate));
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	@JsonProperty
	public void setLatestUpdatingDate(String epochTime) {
		this.latestUpdatingDate = epochTime;
	}

	@JsonProperty
	public void setUpdatingDate(String formatedDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = format.parse(formatedDate);
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
		}
		this.latestUpdatingDate = date.getTime() + "";
	}

	public String getLatestAuthor() {
		return latestAuthor;
	}

	@JsonProperty
	public void setLatestAuthor(String latestAuthor) {
		this.latestAuthor = latestAuthor;
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

	@JsonProperty
	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	public List<String> getGroups() {
		return groups;
	}

	public String getGroupsAsString() {
		return StringUtils.join(groups, ", ");
	}

	public boolean equals(Object object) {
		return ((KnowledgeElement) object).getSummary().equals(getSummary());
	}
}