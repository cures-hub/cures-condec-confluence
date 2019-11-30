package de.uhd.ifi.se.decision.management.confluence.rest;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DecisionKnowledgeElement {

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
	private Integer group;
	@XmlElement
	private String description;

	public DecisionKnowledgeElement(String link, int pageId, String summary, String type, String key,
			String description, Integer group, String macroId) {
		this.pageId = pageId;
		this.summary = summary;
		this.type = type;
		this.key = key;
		this.link = link;
		this.description = description;
		this.group = group;
		this.macroId = macroId;

		// generate unique id
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
		String datetime = ft.format(dNow);
		this.id = datetime + key;
	}

	public DecisionKnowledgeElement() {
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

	public String getMacroId() {
		return macroId;
	}

	public void setMacroId(String macroId) {
		this.macroId = macroId;
	}

	public int getGroup() {
		return this.group;
	}

	public void setGroup(Integer group) {
		this.group = group;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}