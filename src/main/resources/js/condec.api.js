/*
 This module implements the communication with the ConDec Java REST API.

 Requires
 * nothing
    
 Is required by
 * condec.knowledge.import.js
  
 Is referenced in HTML by
 * nowhere
 */
(function(global) {
	var ConDecAPI = function ConDecAPI() {
		this.restPrefix = AJS.Data.get("context-path") + "/rest/condec/latest";
	};

	/*
	 * external references: condec.knowledge.import
	 */
	ConDecAPI.prototype.getProjectsFromJira = function getProjectsFromJira(callback) {
		console.log("conDecApi getProjectsFromJira");
		var url = this.restPrefix + "/issueRest/getProjectsFromJira";

		getJSON(url, function(error, data) {
			if (error === null && !checkForError(data)) {
				callback(data);
			}
		});
	};

	/*
	 * external references: condec.knowledge.import
	 */
	ConDecAPI.prototype.getIssuesFromJira = function getIssuesFromJira(projectKey, userInput, callback) {
		var url = this.restPrefix + "/issueRest/getIssuesFromJira?projectKey=" + projectKey + "&query=?" + userInput;
		getJSON(url, function(error, data) {
			if (error == null && data && !checkForError(data)) {
				callback(data);
			}
		});
	};

	/*
	 * external references: condec.knowledge.import
	 */
	ConDecAPI.prototype.getIssues = function getIssues(pageId, sMacroId, callback) {
		var url = this.restPrefix + "/issueRest/getIssues?pageId=" + pageId + "&macroId=" + sMacroId;
		getJSON(url, function(error, data) {
			if (error == null && !checkForError(data)) {
				callback(data);
			}
		});
	};

	/*
	 * external references: condec.knowledge.import
	 */
	ConDecAPI.prototype.postIssueArray = function postIssueArray(jsonArray, pageId, macroId, callback) {
		postJSON(AJS.Data.get("context-path") + "/rest/condec/latest/issueRest/add-issue-array?pageId=" + pageId
				+ "&macroId=" + macroId, jsonArray, function(error, result) {
			if (error === null) {
				callback(result);
				showFlag("success", "Json Issues updated");
			} else {
				showFlag("error", "An Server Error occured." + error);
			}
		});
	};
	
	function checkForError(oData) {
		var bResult = false;
		if (oData["error"] !== undefined) {
			showFlag("error", "An Error occurred in the Jira connection: " + oData["error"]);
			bResult = true;
		}
		return bResult;
	}

	function getJSON(url, callback) {
		var xhr = new XMLHttpRequest();
		xhr.open("GET", url, true);
		xhr.setRequestHeader("Content-type", "application/json; charset=utf-8");
		xhr.setRequestHeader("Accept", "application/json");
		xhr.responseType = "json";
		xhr.onload = function() {
			var status = xhr.status;
			if (status === 200) {
				callback(null, xhr.response);
			} else {
				showFlag("error", xhr.response.error, status);
				callback(status);
			}
		};
		xhr.send();
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
				showFlag("error", xhr.response.error, status);
				callback(status, xhr.response);
			}
		};
		xhr.send(JSON.stringify(data));
	}

	function showFlag(type, message, status) {
		if (status === null || status === undefined) {
			status = "";
		}
		AJS.flag({
			type : type,
			close : "auto",
			title : type.charAt(0).toUpperCase() + type.slice(1) + " " + status,
			body : message
		});
	}

	// export ConDecAPI
	global.conDecAPI = new ConDecAPI();
})(window);
