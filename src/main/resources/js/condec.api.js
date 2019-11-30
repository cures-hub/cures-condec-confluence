/*
 This module implements the communication with the ConDec Java REST API.

 Requires
 * nothing
    
 Is required by
 * condec.knowledge.overview.js
  
 Is referenced in HTML by
 * nowhere
 */
(function(global) {
	var ConDecAPI = function ConDecAPI() {
		this.restPrefix = AJS.contextPath() + "/rest/condec/latest";
	};

	/*
	 * external references: condec.knowledge.overview
	 */
	ConDecAPI.prototype.getProjectsFromJira = function getDecisionKnowledgeFromJira(callback) {
		console.log("conDecApi getProjectsFromJira");
		var url = this.restPrefix + "/issueRest/getProjectsFromJira";

		getJSON(url + "/getProjectsFromJira", function(error, data) {
			showFlag("error", error);
			if (error == null) {
				var jqlError = checkForError(data);
				if (!jqlError) {
					var jqlInputField = $(".jqlInputFieldContainer");
					var selectedJqlInputField = $((jqlInputField)[jqlInputField.length - 1]);
					if (data && data.length > 0) {
						var radioBoxes = '<form class="aui">\n' + '    <div class="field-group">\n'
								+ '        <label for="select-example">Select Project</label>'
								+ '        <select class="projectSelect">';

						data.map(function(oProject) {
							radioBoxes += addRadioBoxForProject(oProject)
						});
						radioBoxes += '</select></select></div></form>';

						selectedJqlInputField.append(radioBoxes)
					} else {
						selectedJqlInputField.append("<h4>No Projects were Found</h4>")

					}
				}
			} else {
				showFlag("error", error);
			}
		});
	};

	function getResponseAsReturnValue(url) {
		var xhr = new XMLHttpRequest();
		xhr.open("GET", url, false);
		xhr.setRequestHeader("Content-type", "application/json; charset=utf-8");
		xhr.send();
		var status = xhr.status;
		if (status === 200) {
			try {
				console.log(xhr.response);
				var parsedResponse = JSON.parse(xhr.response);
				return parsedResponse;
			} catch (error) {
				console.log(error);
				return null;
			}
		}
		showFlag("error", xhr.response.error, status);
		return null;
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
