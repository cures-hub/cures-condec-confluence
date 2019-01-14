package de.uhd.ifi.se.decision.management.confluence.rest;

//import com.atlassian.applinks.api.ApplicationLinkRequestFactory;
//import com.atlassian.sal.api.net.Request;
//import com.atlassian.sal.api.net.ResponseException;
//import com.atlassian.sal.api.net.ResponseHandler;
import de.uhd.ifi.se.decision.management.confluence.oauth.ApiLinkService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
//import java.net.HttpURLConnection;
import java.util.ArrayList;


@Path("/issueRest")
public class JsonIssueResource {
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/add-issue-array")
	public Response addIssue(String jsonObjectString, @Context HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject(jsonObjectString);
		Boolean useObjectUrl=false;
		String url = "";

		if(((String) jsonObject.get("url")).equals("USE_OBJECT_URL")){
			useObjectUrl=true;
		}else{
			url = (String) jsonObject.get("url");
		}
		int pageId = (int) jsonObject.get("pageId");

		JSONArray jsonArray = (JSONArray) jsonObject.get("data");
		//first remove issues from this page
		try {
			JsonIssueKeeping jsonIssueKeeping = JsonIssueKeeping.getInstance();

			if (jsonArray.length() > 0) {
				jsonIssueKeeping.removeJsonIssuesFromPageId(pageId);
			}

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject myObj = jsonArray.getJSONObject(i);
				String completeKey = (String) myObj.get("key");
				String concatKey = completeKey;
				//check if completeKey has :
				if (completeKey.indexOf(":") > -1) {
					concatKey = concatKey.split(":")[0];
				}
				String link="";

				if(useObjectUrl){
					if(myObj.has("url")){
						link=(String)myObj.get("url");
					}
					if(myObj.has("link")){
						link=(String)myObj.get("link");
					}
				}else{
					link = url + concatKey;
				}
				int myPageId = pageId;
				String mySummary = myObj.has("summary")? (String) myObj.get("summary") : "";
				String myType = myObj.has("type") ? (String) myObj.get("type") : "";
				String myKey = completeKey;

				JsonIssue jsonIssue = new JsonIssue(link, myPageId, mySummary, myType, myKey);
				jsonIssueKeeping.addIssue(jsonIssue);
			}
		} catch (Exception e) {
			return Response.serverError().build();
		}

		return Response.ok().build();
	}

	@Path("/getIssues")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getDecisionKnowledgeElement(@QueryParam("pageId") int pageId) {
		try {
			JsonIssueKeeping jsonIssueKeeping = JsonIssueKeeping.getInstance();
			ArrayList jsonArray = jsonIssueKeeping.getJsonArrayFromPageId(pageId);
			return Response.status(Response.Status.OK).entity(jsonArray).build();


		} catch (Exception e) {
			return Response.serverError().build();
		}
	}
	@Path("/getIssuesFromJira")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getDecisionKnowledgeElement(@QueryParam("query") String query) {
		try {
			String jsonString=	ApiLinkService.makeGetRequestToJira(query);

			return Response.status(Response.Status.OK).entity(jsonString).build();

		} catch (Exception e) {
			return Response.serverError().build();
		}
	}
}