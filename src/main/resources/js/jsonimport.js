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
	function postIssueArray(jsonArray, callback) {
		postJSON(AJS.Data.get("context-path") + "/rest/jsonIssues/1.0/issueRest/add-issue-array", jsonArray,
			function (error, result) {
				if (error === null) {
					callback(result);
					showFlag("success", "Json Issues updated");
				} else {
					showFlag("error", "An Server Error occured." + error);
				}
			});
	}
	var updateMacro = function () {

		// Standard sizes are 400, 600, 800 and 960 pixels wide
		var dialog = new AJS.Dialog({
			width: 550,
			height: 600,
			id: "example-dialog",
			closeOnOutsideClick: true
		});
		//get json from restpoint
		var pageId = parseInt(AJS.params.pageId,10);
		getJSON(AJS.Data.get("context-path") + "/rest/jsonIssues/1.0/issueRest/getIssues?pageId="+pageId,function(error,data){
			if(error==null){
				var prefillValue=JSON.stringify(data);
				var allTextAreas = $(".jsonDialogMacroContainer");
				var selectedTextArea=$((allTextAreas)[allTextAreas.length - 1]);
				selectedTextArea.val(prefillValue);
				var table="<h4>Current Issues</h4><br><table><tr><th>Key</th><th>Summary</th><th>Type</th></tr>";
				data.map(function(obj){
					var tableRow="<tr><td><a target='_blank' href='"+obj["link"]+"'>"+obj["key"]+"</a></td>";
					tableRow+="<td>"+obj["summary"]+"</td>";
					tableRow+="<td>"+obj["type"]+"</td>";
					tableRow+="</tr>";
					table+=tableRow;
				});
				table+="</table>";
				selectedTextArea.append("<br>"+table);
			}
		});
		dialog.addPanel("Panel 1", "Test Connection to jira</button><h4>Paste here your jsonArray from Jira, existing issues from this page will be overwritten</h4><br>" +
			"<div class='jsonDialogMacroContainer'><textarea rows='4' cols='70' class='jsonPasteTextArea'></textarea></div>", "panel-body");


		dialog.addLink("Cancel", function (dialog) {
			dialog.hide();
		}, "#");

		dialog.addHeader("Dialog");

		dialog.addButton("ok", function (dialog) {
			//get all textareas
			var allTextAreas = $(".jsonPasteTextArea");

			var userInput = $((allTextAreas)[allTextAreas.length - 1]).val();

			try {
				var userObject = JSON.parse(userInput);
			} catch (e) {
				showFlag("error", "Error parsing your input." + e);
			}
			userObject["pageId"] = pageId;
			postIssueArray(userObject, function (some) {
			});

			dialog.hide();
		});
		dialog.addButton("Test Connection", function (dialog) {
			//get all textareas
            getJSON(AJS.Data.get("context-path") + "/rest/jsonIssues/1.0/issueRest/getIssuesFromJira?pageId="+pageId,function(error,data){
                if(error==null){
                    var prefillValue=JSON.stringify(data);
					console.log("resultFromJira",prefillValue)
                }else{
                    console.log("resultFromJiraError",error)

                }
            });		});
		dialog.show();

	};





	AJS.Confluence.PropertyPanel.Macro.registerButtonHandler("updateButton", function (e, macroNode) {
		updateMacro();
	});


})();

