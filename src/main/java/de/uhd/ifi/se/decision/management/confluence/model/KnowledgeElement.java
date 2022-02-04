package de.uhd.ifi.se.decision.management.confluence.model;

import java.io.Serializable;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KnowledgeElement implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(KnowledgeElement.class);

	private String link;
	private String summary;
	private String type;
	private String key;
	private String id;
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
	 *           manually using an ObjectMapper!
	 * @con This is not very elegant and hard to understand. There might be an
	 *      easier way of mapping JSON Strings into objects.
	 * @alternative We could use GSON for (de)serialization.
	 * @con GSON only (de)serializes attributes, it is hard to add further getters
	 *      and setters.
	 */
	public static List<KnowledgeElement> parseJsonString(String jsonString) {
		ObjectMapper objectMapper = createObjectMapper();
		List<KnowledgeElement> elements = new ArrayList<KnowledgeElement>();
		try {
			elements = objectMapper.readValue(jsonString, new TypeReference<List<KnowledgeElement>>() {
			});
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return elements;
	}

	public static String toJsonString(List<KnowledgeElement> elements) {
		ObjectMapper objectMapper = createObjectMapper();
		String jsonString = "";
		try {
			jsonString = objectMapper.writeValueAsString(elements);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return jsonString;
	}

	private static ObjectMapper createObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.enable(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES);
		objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.writerWithDefaultPrettyPrinter();
		return objectMapper;
	}

	public String getId() {
		return id;
	}

	@JsonProperty
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

	@JsonProperty
	@JsonAlias("url")
	public void setLink(String link) {
		this.link = link;
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