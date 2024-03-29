/**
 *  This module implements the dialog, which can be used for manual importing decision knowledge
 *  from Jira via a JSON string and to change the imported elements.
 *  The main dialog for importing decision knowledge from Jira
 *  is implemented in atlassian-plugin.xml
 */
AJS.bind("init.rte", function() {

	var macroName = "decision-knowledge-import-macro";

	var jsOverrides = {
		"fields": {
			"enum": {
				"project": function(params, options) {
					var field = AJS.MacroBrowser.ParameterFields["enum"](params, options);
					conDecAPI.getProjectsFromJira(function(projects) {
						var options = "";
						projects.map(function(project) {
							options += createOptionForProject(project);
						});
						field.input.append(options);
					});
					return field;
				}
			}
		}
	};

	function createOptionForProject(project) {
		return '<option value="' + project["key"] + '">' + project["name"] + '</option>';
	}

	AJS.MacroBrowser.setMacroJsOverride(macroName, jsOverrides);

	var updateMacro = function() {
		var dialog = $("#json-dialog");
		if (dialog !== null) {
			dialog.remove();
		}

		// Standard sizes are 400, 600, 800 and 960 pixels
		dialog = new AJS.Dialog({
			width: 960,
			height: 800,
			id: "json-dialog",
			closeOnOutsideClick: true
		});

		dialog.addPanel(
			"Edit JSON String",
			"<p>Paste a JSON String exported from Jira or manually edit the existing one. "
			+ "<mark>Make sure you enabled the 'freeze' option! Otherwise changes will not be saved!</mark></p>"
			+ "<form class='aui'><textarea class='textarea full-width-field' rows='30' id='jsonTextArea'></textarea></form>",
			"panel-body");

		// get knowledge elements from backend via REST 
		var pageId = parseInt(AJS.params.pageId, 10);
		conDecAPI.getStoredKnowledgeElements(pageId, function(elements) {
			$("#jsonTextArea").val(JSON.stringify(elements, undefined, "\t"));
		});

		dialog.addLink("Cancel", function(dialog) {
			dialog.hide();
		}, "#");

		dialog.addHeader("Knowledge Edit and Import Dialog");

		dialog.addButton("Update Knowledge", function(dialog) {
			var userInput = $("#jsonTextArea").val();
			conDecAPI.storeKnowledgeElements(userInput, pageId);
			dialog.hide();
		});

		dialog.show();
	};

	AJS.Confluence.PropertyPanel.Macro.registerButtonHandler("updateButton", function(e, macroNode) {
		updateMacro();
	});
});