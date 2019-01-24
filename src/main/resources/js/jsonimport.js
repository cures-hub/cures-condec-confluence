(function () {

	var macroName = "issue-import-macro";

	function showFlag(type, message) {
		AJS.flag({
			type: type,
			close: "auto",
			title: type.charAt(0).toUpperCase() + type.slice(1),
			body: message
		});
	}

	function postJSON(url, data, callback) {
		var xhr = new XMLHttpRequest();
		xhr.open("POST", url, true);
		xhr.setRequestHeader("Content-type", "application/json; charset=utf-8");
		xhr.setRequestHeader("Accept", "application/json");
		xhr.responseType = "json";
		xhr.onload = function () {
			var status = xhr.status;
			if (status === 200) {
				callback(null, xhr.response);
			} else {
				callback(status);
			}
		};
		xhr.send(JSON.stringify(data));
	}

	function getJSON(url, callback) {
		var xhr = new XMLHttpRequest();
		xhr.open("GET", url, true);
		xhr.setRequestHeader("Content-type", "application/json; charset=utf-8");
		xhr.responseType = "json";
		xhr.onload = function () {
			var status = xhr.status;
			if (status === 200) {
				callback(null, xhr.response);
			} else {
				callback(status);
			}
		};
		xhr.send();
	}

	function postIssueArray(jsonArray,pageId, macroId, callback ) {
		postJSON(AJS.Data.get("context-path") + "/rest/jsonIssues/1.0/issueRest/add-issue-array?pageId="+pageId+"&macroId="+macroId, jsonArray,
			function (error, result) {
				if (error === null) {
					callback(result);
					showFlag("success", "Json Issues updated");
				} else {
					showFlag("error", "An Server Error occured." + error);
				}
			});
	}

	var updateMacro = function (sMacroId) {

		// Standard sizes are 400, 600, 800 and 960 pixels wide
		var dialog = new AJS.Dialog({
			width: 550,
			height: 600,
			id: "example-dialog",
			closeOnOutsideClick: true
		});
		//get json from restpoint
		var pageId = parseInt(AJS.params.pageId, 10);
		getJSON(AJS.Data.get("context-path") + "/rest/jsonIssues/1.0/issueRest/getIssues?pageId=" + pageId+"&macroId="+sMacroId, function (error, data) {
			if (error == null) {
				var prefillValue = JSON.stringify(data);
				var allTextAreas = $(".jsonPasteTextArea");
				var selectedTextArea = $((allTextAreas)[allTextAreas.length - 1]);
				selectedTextArea.val(prefillValue);
				var allHiddenAreas = $(".jqlResultField");
				var selectedHiddenArea = $((allHiddenAreas)[allHiddenAreas.length - 1]);
				selectedHiddenArea.val(prefillValue);
				var resultFields = $(".jsonResultField");
				var selectedResultField = $((resultFields)[resultFields.length - 1]);
				var jqlResultFields = $(".jqlResultField");
				var selectedJQLResultField = $((jqlResultFields)[jqlResultFields.length - 1]);

				var table = getHTMLTableHeader();
				data.map(function (obj) {
					table += createJsonTable(obj);
				});
				table += "</table>";
				selectedResultField[0].innerHTML = table;
				selectedJQLResultField[0].innerHTML = table;
			}
		});
		dialog.addPanel("Manual", "<h4>Paste here your jsonArray from Jira, existing issues from this page will be overwritten</h4><br>" +
			"<div class='jsonDialogMacroContainer'><textarea rows='4' cols='50' class='jsonPasteTextArea'></textarea></div><div class='jsonResultField'>", "panel-body");
		dialog.addPanel("Direct", "<h4>Here you can use JQL if the connection to jira exists, using JQL overwrittes previous data from this page</h4><br>" +
			"<div class='jsonDialogMacroContainer'><input class='jqlInputField text medium-field' placeholder='some jql...'></input><button class='jqlSearchButton aui-button'><span class=\"aui-icon aui-icon-small aui-iconfont-search\">Search</span></button></div><div class='jqlResultField'></div>" +
			"<textarea class='hiddenJqlIssueSaver' style='display:none'></textarea>", "panel-body");

		$(".jqlSearchButton").on("click", function () {
			jqlCallToBackend();
		});
		dialog.addLink("Cancel", function (dialog) {
			dialog.hide();
		}, "#");

		dialog.addHeader("Dialog");

		dialog.addButton("Use Manual Data", function (dialog) {
			//get all textareas
			var allTextAreas = $(".jsonPasteTextArea");

			var userInput = $((allTextAreas)[allTextAreas.length - 1]).val();

			try {
				var parsedUserInput = JSON.parse(userInput);
				if (Array.isArray(parsedUserInput)) {
					//	the object does not come from jira manually, but directly
					parsedUserInput = {data: parsedUserInput, url: "USE_OBJECT_URL", pageId: pageId}
				} else {
					parsedUserInput["pageId"] = pageId;
				}

			} catch (e) {
				showFlag("error", "Error parsing your input." + e);
			}
			postIssueArray(parsedUserInput,pageId,sMacroId, function (some) {
			});
			dialog.hide();
		});

		dialog.addButton("Use Direct Data", function (dialog) {
			var hiddenFields = $(".hiddenJqlIssueSaver");

			var savedJsonString = $((hiddenFields)[hiddenFields.length - 1]).val();
			if (savedJsonString.length > 0) {
				try {
					var aSaved = JSON.parse(savedJsonString);
					var userObject = {data: aSaved, url: "USE_OBJECT_URL"};
				} catch (e) {
					showFlag("error", "An parsing error occured." + e);
				}
				var pageId = parseInt(AJS.params.pageId, 10);
				userObject["pageId"] = pageId;
				postIssueArray(userObject,pageId,sMacroId, function (some) {
				});
				dialog.hide();
			} else {
				showFlag("error", "No Search results where found");
			}

		});

		function jqlCallToBackend() {
			//get all input fields
			var allInputFields = $(".jqlInputField");
			var selectedUserInputField = $((allInputFields)[allInputFields.length - 1]);
			var userInput = selectedUserInputField.val();
			var resultFields = $(".jqlResultField");
			var selectedResultField = $((resultFields)[resultFields.length - 1]);

			var hiddenFields = $(".hiddenJqlIssueSaver");
			var selectedHiddenField = $((hiddenFields)[hiddenFields.length - 1]);

			getJSON(AJS.Data.get("context-path") + "/rest/jsonIssues/1.0/issueRest/getIssuesFromJira?query=" + userInput, function (error, data) {
				if (error == null) {
					if (data.length === 0) {
						showFlag("error", "No Search results where found");
					}
					selectedHiddenField.val(JSON.stringify(data));
					var table = getHTMLTableHeader();
					data.map(function (obj) {
						table += createJsonTable(obj);
					});
					table += "</table>";
					selectedResultField[0].innerHTML = table;
				} else {
					console.log("resultFromJiraError", error);
				}
			});
		}

		function createJsonTable(obj) {
			var url = obj["url"] || obj["link"] || "";
			var tableRow = "<tr><td><a target='_blank' href='" + url + "'>" + obj["key"] + "</a></td>";
			tableRow += "<td>" + obj["summary"] + "</td>";
			tableRow += "<td>" + obj["type"] + "</td>";
			tableRow += "</tr>";
			return tableRow;
		}

		function getHTMLTableHeader() {
			return "<h4>Current Issues</h4><br><table><tr><th>Key</th><th>Summary</th><th>Type</th></tr>";
		}

		dialog.show();

	};

	AJS.Confluence.PropertyPanel.Macro.registerButtonHandler("updateButton", function (e, macroNode) {
		var sMacroId = macroNode.getAttribute("data-macro-id");
		if (sMacroId && sMacroId !== "") {
			updateMacro(sMacroId);
		} else {
			showFlag("error", "Please save the page first before updating the Macro")
		}
	});


})();

