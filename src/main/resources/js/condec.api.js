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
		var url = this.restPrefix + "/knowledge/getProjectsFromJira";
		getJSON(url, function(error, projects) {
			if (error === null && !checkForError(projects)) {
				callback(projects);
			}
		});
	};

	/*
	 * external references: condec.knowledge.import
	 */
	ConDecAPI.prototype.getStoredKnowledgeElements = function getStoredKnowledgeElements(pageId, macroId, callback) {
		var url = this.restPrefix + "/knowledge/getStoredKnowledgeElements?pageId=" + pageId + "&macroId=" + macroId;
		getJSON(url, function(error, elements) {
			if (error === null && !checkForError(elements)) {
				callback(elements);
			}
		});
	};

	/*
	 * external references: condec.knowledge.import
	 */
	ConDecAPI.prototype.storeKnowledgeElements = function storeKnowledgeElements(userInput, pageId, macroId) {
		var jsonArray = "";
        try {
        	jsonArray = JSON.parse(userInput);
        } catch (e) {
            showFlag("error", "Your input could not be parsed. " + e);
        }
		var url = this.restPrefix + "/knowledge/storeKnowledgeElements?pageId=" + pageId + "&macroId=" + macroId;
		postJSON(url, jsonArray, function(error, result) {
			if (error === null) {				
				showFlag("success", "The stand-up table was successfully updated.");
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
	
	/*
	 * external references: condec.knowledge.import
	 */
	ConDecAPI.prototype.showFlag = showFlag;

	// export ConDecAPI
	global.conDecAPI = new ConDecAPI();
})(window);