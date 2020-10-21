package de.uhd.ifi.se.decision.management.confluence.macro;

import java.text.ParseException;
// dom
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.content.render.xhtml.storage.macro.MacroId;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.confluence.xhtml.api.MacroDefinition;
import com.atlassian.fugue.Option;

import de.uhd.ifi.se.decision.management.confluence.model.KnowledgeElement;
import de.uhd.ifi.se.decision.management.confluence.oauth.JiraClient;
import de.uhd.ifi.se.decision.management.confluence.persistence.KnowledgePersistenceManager;

public class DecisionKnowledgeImportMacro implements Macro {

	@Override
	public String execute(Map<String, String> map, String s, ConversionContext conversionContext)
			throws MacroExecutionException {
		int pageId = Integer.parseInt(conversionContext.getEntity().getIdAsString());
		String macroId = getMacroId(conversionContext);

		List<KnowledgeElement> knowledgeElements = KnowledgePersistenceManager.getElements(pageId, macroId);

		boolean freeze = false;
		if ("true".equals(map.get("freeze"))) {
			freeze = true;
		}

		if (knowledgeElements.isEmpty() || !freeze) {
			String projectKey = map.get("project");
			String searchTerm = map.get("substring");

			long startDate = 0L;
			long endDate = 0L;

			if (map.get("startDate") == null || map.get("startDate").isBlank()
					|| map.get("startDate").equals("yyyy-MM-ddThh:mm")) {
				System.out.println(map.get("startDate"));
				LocalDate localStartDate = LocalDate.now().minusDays(14);
				System.out.println(localStartDate);
				startDate = convertToUnixTimeStamp(localStartDate.toString());
				System.out.println(startDate);
			} else
				startDate = convertToUnixTimeStamp(map.get("startDate"));

			if (map.get("endDate") == null || map.get("endDate").isBlank()
					|| map.get("endDate").equals("yyyy-MM-ddThh:mm")) {
				System.out.println(map.get("endDate"));
				endDate = Instant.now().getEpochSecond() * 1000;
				System.out.println(endDate);
			} else
				endDate = convertToUnixTimeStamp(map.get("endDate"));

			if (searchTerm == null) {
				searchTerm = "";
			}
			if (projectKey != null && !projectKey.isBlank()) {
				knowledgeElements = JiraClient.instance.getDecisionKnowledgeFromJira(searchTerm, projectKey, startDate,
						endDate);
				KnowledgePersistenceManager.removeKnowledgeElements(pageId, macroId);
				for (KnowledgeElement element : knowledgeElements) {
					element.setPageId(pageId);
					element.setMacroId(macroId);
					KnowledgePersistenceManager.addKnowledgeElement(element);
				}
			}
		}

		Map<String, Object> renderContext = MacroUtils.defaultVelocityContext();
		renderContext.put("knowledgeElements", knowledgeElements);

		return VelocityUtils.getRenderedTemplate("/templates/standUpTable.vm", renderContext);
	}

	private long convertToUnixTimeStamp(String dateString) {
		long unixTimeStamp = 0L;

		if (dateString.contains("T")) {
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
				Date date = simpleDateFormat.parse(dateString);
				unixTimeStamp = date.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = simpleDateFormat.parse(dateString);
				unixTimeStamp = date.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return unixTimeStamp;
	}

	private String getMacroId(ConversionContext conversionContext) {
		String macroId = "0";
		try {
			MacroDefinition macroDefinition = (MacroDefinition) conversionContext.getProperty("macroDefinition");
			Option<MacroId> option = macroDefinition.getMacroId();
			macroId = option.get().getId();
			System.out.println(macroId);
		} catch (Exception e) {
		}
		return macroId;
	}

	@Override
	public BodyType getBodyType() {
		return BodyType.NONE;
	}

	@Override
	public OutputType getOutputType() {
		return OutputType.BLOCK;
	}

}