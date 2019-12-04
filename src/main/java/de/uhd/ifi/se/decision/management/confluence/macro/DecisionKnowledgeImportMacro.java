package de.uhd.ifi.se.decision.management.confluence.macro;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.content.render.xhtml.storage.macro.MacroId;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.confluence.xhtml.api.MacroDefinition;
import com.atlassian.fugue.Option;

import de.uhd.ifi.se.decision.management.confluence.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.confluence.oauth.JiraClient;
import de.uhd.ifi.se.decision.management.confluence.persistence.KnowledgePersistenceManager;

public class DecisionKnowledgeImportMacro implements Macro {

	@Autowired
	public DecisionKnowledgeImportMacro() {
	}

	@Override
	public String execute(Map<String, String> map, String s, ConversionContext conversionContext)
			throws MacroExecutionException {
		int pageId = Integer.parseInt(conversionContext.getEntity().getIdAsString());
		String macroId = getMacroId(conversionContext);

		List<DecisionKnowledgeElement> knowledgeElements = KnowledgePersistenceManager.getElements(pageId, macroId);

		String projectKey = map.get("project");
		String query = map.get("query");
		boolean freeze = false;
		if ("true".equals(map.get("freeze"))) {
			freeze = true;
		}

		if (projectKey != null && !projectKey.isEmpty()) {
			if (knowledgeElements.isEmpty() || !freeze) {
				knowledgeElements = JiraClient.instance.getDecisionKnowledgeFromJira(query, projectKey);
				KnowledgePersistenceManager.removeDecisionKnowledgeElements(pageId, macroId);
				for (DecisionKnowledgeElement element : knowledgeElements) {
					element.setPageId(pageId);
					element.setMacroId(macroId);
					KnowledgePersistenceManager.addDecisionKnowledgeElement(element);
				}
			}
		}

		Map<String, Object> renderContext = MacroUtils.defaultVelocityContext();
		renderContext.put("knowledgeElements", knowledgeElements);

		return VelocityUtils.getRenderedTemplate("/templates/standUpTable.vm", renderContext);
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