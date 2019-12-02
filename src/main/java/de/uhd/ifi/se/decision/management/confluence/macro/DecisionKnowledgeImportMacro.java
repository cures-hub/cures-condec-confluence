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
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.webresource.api.assembler.PageBuilderService;

import de.uhd.ifi.se.decision.management.confluence.model.DecisionKnowledgeElement;
import de.uhd.ifi.se.decision.management.confluence.persistence.KnowledgePersistenceManager;
import de.uhd.ifi.se.decision.management.confluence.persistence.impl.KnowledgePersistenceManagerImpl;

public class DecisionKnowledgeImportMacro implements Macro {

	private PageBuilderService pageBuilderService;

	@Autowired
	public DecisionKnowledgeImportMacro(@ComponentImport PageBuilderService pageBuilderService) {
		this.pageBuilderService = pageBuilderService;
	}

	@Override
	public String execute(Map<String, String> map, String s, ConversionContext conversionContext)
			throws MacroExecutionException {
		pageBuilderService.assembler().resources().requireWebResource(
				"de.uhd.ifi.se.decision.management.confluence.macro:decision-knowledge-import-resources");
		int pageId = Integer.parseInt(conversionContext.getEntity().getIdAsString());
		String macroId = getMacroId(conversionContext);

		// Save all issues in an ArrayList data structure.
		KnowledgePersistenceManager persistenceManager = KnowledgePersistenceManagerImpl.getInstance();
		List<DecisionKnowledgeElement> knowledgeElements = persistenceManager.getElements(pageId, macroId);

		// Create a new context for rendering...
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