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
		var url = this.restPrefix + "/knowledgeRest/getProjectsFromJira";
		getJSON(url, function(error, data) {
			if (error === null && !checkForError(data)) {
				callback(data);
			}
		});
	};

	/*
	 * external references: condec.knowledge.import
	 */
	ConDecAPI.prototype.getKnowledgeElementsFromJira = function getKnowledgeElementsFromJira(projectKey, userInput, callback) {
		var url = this.restPrefix + "/knowledgeRest/getKnowledgeElementsFromJira?projectKey=" + projectKey + "&query=?" + userInput;
		getJSON(url, function(error, data) {
			if (error === null && !checkForError(data)) {				
				if (data.length === 0) {
					showFlag("error", "No search results were found.");
				} 
				callback(data);
			} else {
				showFlag("success", "Results were found!");
			}
		});
	};

	/*
	 * external references: condec.knowledge.import
	 */
	ConDecAPI.prototype.getKnowledgeElements = function getKnowledgeElements(pageId, sMacroId, callback) {
		var url = this.restPrefix + "/knowledgeRest/getKnowledgeElements?pageId=" + pageId + "&macroId=" + sMacroId;
		getJSON(url, function(error, data) {
			if (error === null && !checkForError(data)) {
				callback(data);
			}
		});
	};

	/*
	 * external references: condec.knowledge.import
	 */
	ConDecAPI.prototype.storeKnowledgeElements = function storeKnowledgeElements(jsonArray, pageId, macroId, callback) {
		var url = this.restPrefix + "/knowledgeRest/storeKnowledgeElements?pageId=" + pageId + "&macroId=" + macroId;
		postJSON(url, jsonArray, function(error, result) {
			if (error === null) {				
				showFlag("success", "The stand-up table was successfully updated.");
				callback(result);
			} else {
				showFlag("error", "A server error occured: " + error);
			}
		});
	};

	function checkForError(data) {
		var hasError = false;
		if (data !== null && data["error"] !== undefined) {
			showFlag("error", "An error occurred in the Jira connection: " + data["error"]);
			hasError = true;
		}
		return hasError;
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
		xhr.onload = function() {
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