package de.uhd.ifi.se.decision.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;

import java.util.Map;

//import com.atlassian.confluence.json.json.JsonObject;
//import com.atlassian.confluence.json.json.JsonArray;
//import java.text.ParseException;
//
//import com.google.gson.JsonObject;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;
//import com.google.gson.JsonArray;

import org.json.*;
import org.json.JSONObject;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.webresource.api.assembler.PageBuilderService;
import org.springframework.beans.factory.annotation.Autowired;

@Scanned
public class DecisionKnowledgeImportMacro implements Macro {

    private PageBuilderService pageBuilderService;

    @Autowired
    public DecisionKnowledgeImportMacro(@ComponentImport PageBuilderService pageBuilderService) {
        this.pageBuilderService = pageBuilderService;
    }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        pageBuilderService.assembler().resources().requireWebResource("de.uhd.ifi.se.decision.macro:myConfluenceMacro-resources");

        String output = "<div>";
       if (map.get("yourJsonArray") != null) {
            JSONObject obj = new JSONObject();

            JSONArray jsonArray = new JSONArray(map.get("yourJsonArray"));

        //    if (map.get("whichOne").equals("Table")) {

                output = output + "<table class='aui'>";
                String tableHeader = generateTableHeader();
                output = output + tableHeader;

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject myObj = jsonArray.getJSONObject(i);
                    output = output + generateTableRow(myObj);
                }
       //     }

//            if (map.get("whichOne").equals("Tree")) {
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    //generate Table
//                    output= output +"<table class='aui'>";
//                    JSONObject myObj = jsonArray.getJSONObject(i);
//                    JSONObject myInfo= myObj.getJSONObject("graph").getJSONObject("rootElement");
//                    output = output + generateTableRow(myInfo);
//                    JSONArray children = myObj.getJSONObject("nodeStructure").getJSONArray("children");
//                    for (int j = 0; j < children.length(); j++) {
//                        //generate subTable
//                        String subTable = generateTableRow(children.getJSONObject(j));
//                        output = output + subTable;

                        //                  }
//                }
//                output = "<table><div>" + generateParentTable();

            }


        output = output + "</table></div>";
        return output;
    }

    public String generateTableRow(JSONObject myObject) {
        Object myId = myObject.has("id") == true ? myObject.get("id") : "";
        Object myKey = myObject.has("key") == true ? myObject.get("key") : "";
        Object myStatus = myObject.has("status") == true ? myObject.get("status") : "";
        Object myIssueType = myObject.has("type") == true ? myObject.get("type") : "";
        Object myPriority = myObject.has("priority") == true ? myObject.get("priority") : "";
        Object myAssignee = myObject.has("assignee") == true ? myObject.get("assignee") : "";
        Object mySummary = myObject.has("summary") == true ? myObject.get("summary") : "";
        Object myDescription = myObject.has("description") == true ? myObject.get("description") : "";
        Object myLinkToJira = myObject.has("link") == true ? myObject.get("link") : "";

        return "<tr>"
                + "<td><a target='_blank' href='" + myLinkToJira + "'>" + myKey + "</a></td>"
                + "<td>" + myIssueType + "</td>"
//                + "<td>" + myStatus + "</td>"
//                + "<td>" + myPriority + "</td>"
//                + "<td>" + myAssignee + "</td>"
                + "<td>" + mySummary + "</td>"
                + "<td>" + myDescription + "</td>"
                + "</tr>";

    }

    public String generateTableHeader() {
        return "<tr>" +
                "<th>Id</th>" +
                "<th>Issue Type</th>" +
//                "<th>Status</th>" +
//                "<th>Priority</th>" +
//                "<th>Assignee</th>" +
                "<th>Summary</th>" +
                "<th>Description</th>" +
                "</tr>";
    }
//    public String generateParentTable(myObj) {
//        String table = "<tr>";
//        table = table + generateTableRow(myObj);
//        return table;
//    }


    public BodyType getBodyType() {
        return BodyType.NONE;
    }

    public OutputType getOutputType() {
        return OutputType.BLOCK;
    }
}