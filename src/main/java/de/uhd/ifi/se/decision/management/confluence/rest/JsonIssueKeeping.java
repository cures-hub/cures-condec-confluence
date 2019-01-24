package de.uhd.ifi.se.decision.management.confluence.rest;

import com.atlassian.bandana.BandanaContext;
import com.atlassian.bandana.BandanaManager;
import com.atlassian.confluence.setup.bandana.ConfluenceBandanaContext;
import com.atlassian.spring.container.ContainerManager;

import java.util.ArrayList;


public class JsonIssueKeeping {
	// We are using the Confluence BandanaManager for persistent storage. For information, see:
	// http://docs.atlassian.com/atlassian-bandana/0.2.0/com/atlassian/bandana/BandanaManager.html
	private BandanaManager bandanaManager;
	// The context for the BandanaManager.
	private final BandanaContext bandanaContext;

	private static JsonIssueKeeping INSTANCE;

	public static JsonIssueKeeping getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new JsonIssueKeeping();
		}
		return INSTANCE;
	}

	// Our constructor is private, such that it can only be called from within our getInstance
	// method.
	private JsonIssueKeeping() {
		ContainerManager.autowireComponent(this);
		this.bandanaContext = new ConfluenceBandanaContext("jsonissuekeeping");
	}

	public void addIssue(JsonIssue jsonIssue) {
		bandanaManager.setValue(this.bandanaContext, jsonIssue.getId(), jsonIssue);
	}

	public void removeIssue(String id) {
		bandanaManager.removeValue(this.bandanaContext, id);
	}

	public ArrayList getJsonArrayFromPageId(int pageId,String macroId) {
		ArrayList myJsonArray = new ArrayList();

		for (String id : this.bandanaManager.getKeys(this.bandanaContext)) {
			JsonIssue jsonIssue = (JsonIssue) this.bandanaManager.getValue(this.bandanaContext, id);
			//add only if the page id and Macro id corresponds // if macro id is null return all from page
			if (jsonIssue.getPageId() == pageId && (jsonIssue.getMacroId().equals(macroId))||macroId==null) {
				myJsonArray.add(jsonIssue);
			}
		}
		return myJsonArray;
	}

	public void removeJsonIssuesFromPageId(int pageId, String macroId) {
		for (String id : this.bandanaManager.getKeys(this.bandanaContext)) {
			JsonIssue jsonIssue = (JsonIssue) this.bandanaManager.getValue(this.bandanaContext, id);
			//remove only if the page id and Macro id corresponds remove all where macroId is null
			String issueMacroId=jsonIssue.getMacroId();

			if (isNullOrEmpty(issueMacroId)||(!isNullOrEmpty(issueMacroId) && jsonIssue.getPageId() == pageId && issueMacroId.equals(macroId))) {
				this.removeIssue(jsonIssue.getId());
			}


		}

	}

	// Getters and setters for the BandanaManager are called by Confluence (injection).
	public BandanaManager getBandanaManager() {
		return bandanaManager;
	}

	public void setBandanaManager(BandanaManager bandanaManager) {
		this.bandanaManager = bandanaManager;
	}
	public static boolean isNullOrEmpty(String myString)
	{
		return myString == null || "".equals(myString);
	}

}