package de.uhd.ifi.se.decision.management.confluence.model.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.uhd.ifi.se.decision.management.confluence.model.DecisionKnowledgeElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DecisionKnowledgeElementImpl implements DecisionKnowledgeElement {

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

	public DecisionKnowledgeElementImpl(String link, int pageId, String summary, String type, String key,
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
		SimpleDateFormat format = new SimpleDateFormat("yyMMddhhmmssMs");
		String datetime = format.format(dNow);
		this.id = datetime + key;
	}

	public DecisionKnowledgeElementImpl() {
	}

	@Override
	public int getPageId() {
		return pageId;
	}

	@Override
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getSummary() {
		return summary;
	}

	@Override
	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String getLink() {
		return link;
	}

	@Override
	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String getMacroId() {
		return macroId;
	}

	@Override
	public void setMacroId(String macroId) {
		this.macroId = macroId;
	}

	@Override
	public int getGroup() {
		return this.group;
	}

	@Override
	public void setGroup(Integer group) {
		this.group = group;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}
}