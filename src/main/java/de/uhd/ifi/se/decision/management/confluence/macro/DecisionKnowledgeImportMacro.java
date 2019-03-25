package de.uhd.ifi.se.decision.management.confluence.macro;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.applinks.api.ApplicationLinkService;
import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.content.render.xhtml.storage.macro.MacroId;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.confluence.xhtml.api.MacroDefinition;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.webresource.api.assembler.PageBuilderService;

import de.uhd.ifi.se.decision.management.confluence.rest.DecisionKnowledgeElementKeeping;

@Scanned
public class DecisionKnowledgeImportMacro implements Macro {

	private PageBuilderService pageBuilderService;
	@Autowired
	public DecisionKnowledgeImportMacro(@ComponentImport PageBuilderService pageBuilderService,
			@ComponentImport ApplicationLinkService applicationLinkService) {
		this.pageBuilderService = pageBuilderService;

	}

	public DecisionKnowledgeImportMacro() {
	}

	public String execute(Map<String, String> map, String s, ConversionContext conversionContext)
			throws MacroExecutionException {
		pageBuilderService.assembler().resources().requireWebResource(
				"de.uhd.ifi.se.decision.management.confluence.macro:issue-import-macro-web-resources");
		int pageId = Integer.parseInt(conversionContext.getEntity().getIdAsString());
		// Retrieve the instance of our JsonKeeping.
		DecisionKnowledgeElementKeeping decisionKnowledgeElementKeeping = DecisionKnowledgeElementKeeping.getInstance();

		// get macroId

		MacroDefinition macroDefinition = (MacroDefinition) conversionContext.getProperty("macroDefinition");
		com.atlassian.fugue.Option<MacroId> option = macroDefinition.getMacroId();

		String macroId = option.get().getId();
		if (macroId != null && !macroId.isEmpty()) {
		}
		// Save all issues in an ArrayList data structure.
		ArrayList<?> jsonIssueArray = decisionKnowledgeElementKeeping.getElementsGroupedFromPageIdAndMacroId(pageId,
				macroId);
		// Create a new context for rendering...

		Map<String, Object> renderContext = MacroUtils.defaultVelocityContext();
		renderContext.put("jsonArrays", jsonIssueArray);

		return VelocityUtils.getRenderedTemplate("/templates/jsonTable.vm", renderContext);

	}

	public BodyType getBodyType() {
		return BodyType.NONE;
	}

	public OutputType getOutputType() {
		return OutputType.BLOCK;
	}

}