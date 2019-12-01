(function() {

	var macroName = "decision-knowledge-import-macro";

	function showFlag(type, message) {
		AJS.flag({
			type : type,
			close : "auto",
			title : type.charAt(0).toUpperCase() + type.slice(1),
			body : message
		});
	}
	/** *****************HTML creating Functions*********************** */
	function createJsonTable(obj) {
		var url = obj["url"] || obj["link"] || "";
		var tableRow = "<tr><td><a target='_blank' href='" + url + "'>" + obj["key"] + "</a></td>";
		tableRow += "<td>" + obj["summary"] + "</td>";
		tableRow += "<td>" + obj["type"] + "</td>";
		tableRow += "</tr>";
		return tableRow;
	}
	
	function itiOverSingleArray(aObj) {
		var tableRows = "";
		aObj.map(function(obj) {
			tableRows += createJsonTable(obj);
		});
		// add empty rows
		tableRows += "<tr><th> </th><th> </th><th> </th></tr>";
		return tableRows;
	}

	function getHTMLTableHeader() {
		return "<h4>Current Knowledge Elements</h4><br><table><tr><th>Key</th><th>Summary</th><th>Type</th></tr>";
	}

	function addRadioBoxForProject(oProject) {
		return '<option value="' + oProject["key"] + '">' + oProject["name"] + '</option>';
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
					showFlag("error", "No Search results where found");
				} else {
					showFlag("success", "Results found!");
				}
				selectedHiddenField.val(JSON.stringify(data));
				var table = getHTMLTableHeader();
				data.map(function(aObj) {
					table += itiOverSingleArray(aObj);
				});
				table += "</table>";
				selectedResultField[0].innerHTML = table;
			});
		} else {
			showFlag("error", "No project was found or the connection to jira is broken.");
		}
	}

	var updateMacro = function(macroId) {

		// Standard sizes are 400, 600, 800 and 960 pixels wide
		var dialog = new AJS.Dialog({
			width : 550,
			height : 600,
			id : "example-dialog",
			closeOnOutsideClick : true
		});
		// get json from restpoint
		var pageId = parseInt(AJS.params.pageId, 10);
		conDecAPI.getKnowledgeElements(pageId, macroId, function(data) {
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
			var table = getHTMLTableHeader();
			data.map(function(obj) {
				table += createJsonTable(obj);
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
						+ '        <label for="select-example">Select Project</label>'
						+ '        <select class="projectSelect">';

				data.map(function(oProject) {
					radioBoxes += addRadioBoxForProject(oProject);
				});
				radioBoxes += '</select></select></div></form>';

				selectedJqlInputField.append(radioBoxes);
			} else {
				selectedJqlInputField.append("<h4>No projects were found.</h4>");
			}
		});
		dialog
				.addPanel(
						"Direct",
						"<h4>Here you can use JQL if the connection to jira exists, using JQL overwrittes previous data from this macro</h4><br>"
								+ "<div class='jsonDialogMacroContainer'><div class='jqlInputFieldContainer'></div>"
								+ "<div class='field-group'><label for='text-input'>Search query</label><input class='jqlInputField text medium-field' placeholder='jql='/><button class='jqlSearchButton aui-button aui-button-primary'><span class='aui-icon aui-icon-small aui-iconfont-search'>Search</span></button></div></div><div class='jqlResultField'></div>"
								+ "<textarea class='hiddenJqlIssueSaver' style='display:none'></textarea>",
						"panel-body");
		dialog
				.addPanel(
						"Manual",
						"<h4>Paste here your jsonArray from Jira, existing issues from this macro</h4><br>"
								+ "<div class='jsonDialogMacroContainer'><textarea rows='4' cols='50' class='jsonPasteTextArea'></textarea></div><div class='jsonResultField'>",
						"panel-body");

		$(".jqlSearchButton").on("click", function() {
			jqlCallToBackend();
		});
		dialog.addLink("Cancel", function(dialog) {
			dialog.hide();
		}, "#");

		dialog.addHeader("Dialog");

		dialog.addLink("Use Manual Data", function(dialog) {
			// get all textareas
			var allTextAreas = $(".jsonPasteTextArea");

			var userInput = $((allTextAreas)[allTextAreas.length - 1]).val();

			try {
				var parsedUserInput = JSON.parse(userInput);
				if (Array.isArray(parsedUserInput)) {
					// the object does not come from jira manually, but directly
					parsedUserInput = {
						data : parsedUserInput,
						url : "USE_OBJECT_URL",
						pageId : pageId
					};
					conDecAPI.storeKnowledgeElements(parsedUserInput, pageId, macroId, function(some) {
					});
				} else {
					parsedUserInput["pageId"] = pageId;
				}
			} catch (e) {
				showFlag("error", "Error parsing your input." + e);
			}
			
			dialog.hide();
		}, "aui-button");

		dialog.addButton("Use Direct Data", function(dialog) {
			var hiddenFields = $(".hiddenJqlIssueSaver");

			var savedJsonString = $((hiddenFields)[hiddenFields.length - 1]).val();
			if (savedJsonString.length > 0) {
				try {
					var aSaved = JSON.parse(savedJsonString);
					var userObject = {
						data : aSaved,
						url : "USE_OBJECT_URL"
					};
				} catch (e) {
					showFlag("error", "A parsing error occured." + e);
				}
				var pageId = parseInt(AJS.params.pageId, 10);
				userObject["pageId"] = pageId;
				conDecAPI.storeKnowledgeElements(userObject, pageId, macroId, function(some) {
				});
				dialog.hide();
			} else {
				showFlag("error", "No search results were found.");
			}
		});
		
		dialog.show();
	};

	AJS.Confluence.PropertyPanel.Macro.registerButtonHandler("updateButton", function(e, macroNode) {
		var macroId = macroNode.getAttribute("data-macro-id");
		if (macroId && macroId !== "") {
			updateMacro(macroId);
		} else {
			showFlag("error", "Please save the page first before updating the macro.");
		}
	});

})();
