/*
*
*  This module implements the dialog, which can be used for importing decision knowledge
*  from Jira and was manually implemented.
*  It's not sure, if it will be further developed.
*  The main dialog für importing decision knowledge from Jira
*  is implemented in atlassian-plugin.xml
*
* */
AJS.bind("init.rte", function() {
	
	var macroName = "decision-knowledge-import-macro";

	var jsOverrides = {
		"fields" : {
			"enum" : {
				"project" : function(params, options) {
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
	
	function createTableHeader() {
		return "<h4>Current Knowledge Elements</h4>" + "<table><tr><th>Key</th><th>Summary</th><th>Type</th><th>Author</th></tr>";
	}

	function itiOverSingleArray(elements) {
		var tableRows = "";
		elements.map(function(element) {
			tableRows += createTableRow(element);
		});
		// add empty rows
		tableRows += "<tr><th> </th><th> </th><th> </th><th> </th></tr>";
		return tableRows;
	}

	function createTableRow(element) {
		var url = element["url"] || element["link"] || "";
		var tableRow = "<tr><td><a target='_blank' href='" + url + "'>" + element["key"] + "</a></td>";
		tableRow += "<td>" + element["summary"] + "</td>";
		tableRow += "<td>" + element["type"] + "</td>";
		tableRow += "<td>" + element["creator"] + "</td>";
		tableRow += "</tr>";
		return tableRow;
	}

	function jqlCallToBackend() {
		// get all input fields
		var allInputFields = $(".jqlInputField");
		var selectedUserInputField = $((allInputFields)[allInputFields.length - 1]);
		var userInput = selectedUserInputField.val().replace(/\s/g, '');
		var resultFields = $(".jqlResultField");
		var selectedResultField = $((resultFields)[resultFields.length - 1]);

		var hiddenFields = $(".hiddenJqlIssueSaver");
		var selectedHiddenField = $((hiddenFields)[hiddenFields.length - 1]);

		// get project key
		var allProjectSelectForms = $(".projectSelect");
		var selectedProjectSelectForm = $((allProjectSelectForms)[allProjectSelectForms.length - 1]);
		var projectKey = selectedProjectSelectForm.val();

		if (projectKey && projectKey !== "") {
			conDecAPI.getKnowledgeElementsFromJira(projectKey, userInput, function(data) {
				if (data && data.length === 0) {
					conDecAPI.showFlag("error", "No Search results were found");
				} else {
					conDecAPI.showFlag("success", "Results found!");
				}
				selectedHiddenField.val(JSON.stringify(data));
				var table = createTableHeader();
				data.map(function(elements) {
					table += itiOverSingleArray(elements);
				});
				table += "</table>";
				selectedResultField[0].innerHTML = table;
			});
		} else {
			conDecAPI.showFlag("error", "No project was found or the connection to Jira is broken.");
		}
	}

	var updateMacro = function(macroId) {

		// Standard sizes are 400, 600, 800 and 960 pixels
		// wide
		var dialog = new AJS.Dialog({
			width : 960,
			height : 800,
			id : "example-dialog",
			closeOnOutsideClick : true
		});
		// get json from restpoint
		var pageId = parseInt(AJS.params.pageId, 10);
		conDecAPI.getStoredKnowledgeElements(pageId, macroId, function(data) {
			var prefillValue = JSON.stringify(data);
			var allTextAreas = $(".jsonPasteTextArea");
			var selectedTextArea = $((allTextAreas)[allTextAreas.length - 1]);
			selectedTextArea.val(prefillValue);
			var allHiddenAreas = $(".hiddenJqlIssueSaver");
			var selectedHiddenArea = $((allHiddenAreas)[allHiddenAreas.length - 1]);
			selectedHiddenArea.val(prefillValue);
			var resultFields = $(".jsonResultField");
			var selectedResultField = $((resultFields)[resultFields.length - 1]);
			var jqlResultFields = $(".jqlResultField");
			var selectedJQLResultField = $((jqlResultFields)[jqlResultFields.length - 1]);
			var table = createTableHeader();
			data.map(function(element) {
				table += createTableRow(element);
			});
			table += "</table>";
			selectedResultField[0].innerHTML = table;
			selectedJQLResultField[0].innerHTML = table;
		});

		conDecAPI.getProjectsFromJira(function(data) {
			var jqlInputField = $(".jqlInputFieldContainer");
			var selectedJqlInputField = $((jqlInputField)[jqlInputField.length - 1]);
			if (data && data.length > 0) {
				var radioBoxes = '<form class="aui">\n' + '    <div class="field-group">\n'
						+ '       <label for="select-example">Select Project</label>'
						+ '       <select class="projectSelect">';

				data.map(function(project) {
					radioBoxes += createOptionForProject(project);
				});
				radioBoxes += '</select></select></div></form>';
				selectedJqlInputField.append(radioBoxes);
			} else {
				selectedJqlInputField.append("<p>No projects were found.</p>");
			}
		});
		dialog
				.addPanel(
						"Import from Jira",
						"<p>If you chose to \"freeze\" the input, you can edit the stand-up table here.</p>"
								+ "<div class='jsonDialogMacroContainer'><div class='jqlInputFieldContainer'></div>"
								+ "<div class='field-group'><label for='text-input'>Search query:</label> <input class='jqlInputField text medium-long-field' placeholder='jql='/><button class='jqlSearchButton aui-button aui-button-primary'><span class='aui-icon aui-icon-small aui-iconfont-search'>Search</span></button></div></div><div class='jqlResultField'></div>"
								+ "<textarea class='hiddenJqlIssueSaver' style='display:none'></textarea>",
						"panel-body");
		dialog
				.addPanel(
						"Parse JSON String",
						"<p>Paste a JSON String exported from Jira or manually edit the existing one.</p>"
								+ "<div class='jsonDialogMacroContainer'><textarea rows='4' cols='50' class='jsonPasteTextArea'></textarea></div><div class='jsonResultField'>",
						"panel-body");

		$(".jqlSearchButton").on("click", function() {
			jqlCallToBackend();
		});

		dialog.addLink("Cancel", function(dialog) {
			dialog.hide();
		}, "#");

		dialog.addHeader("Knowledge Import Dialog");

		dialog.addLink("Use Manual Input", function(dialog) {
			var allTextAreas = $(".jsonPasteTextArea");
			var userInput = $((allTextAreas)[allTextAreas.length - 1]).val();
			conDecAPI.storeKnowledgeElements(userInput, pageId, macroId);
			dialog.hide();
		}, "aui-button");

		dialog.addButton("Use Imported Knowledge From Jira", function(dialog) {
			var hiddenFields = $(".hiddenJqlIssueSaver");
			var savedJsonString = $((hiddenFields)[hiddenFields.length - 1]).val();
			if (savedJsonString.length > 0) {
				var pageId = parseInt(AJS.params.pageId, 10);
				conDecAPI.storeKnowledgeElements(savedJsonString, pageId, macroId);
				dialog.hide();
			} else {
				conDecAPI.showFlag("error", "No search results were found.");
			}
		});

		dialog.show();
	};
	
	AJS.Confluence.PropertyPanel.Macro.registerButtonHandler("updateButton", function(e, macroNode) {
		var macroId = macroNode.getAttribute("data-macro-id");
		if (macroId && macroId !== "") {
			updateMacro(macroId);
		} else {
			conDecAPI.showFlag("error", "Please save the page first before updating the macro.");
		}
	});
});