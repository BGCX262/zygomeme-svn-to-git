package com.zygomeme.york.reporter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.zygomeme.york.dynamicmodels.DynamicModelHistory;
import com.zygomeme.york.dynamicmodels.DynamicModelNode;
import com.zygomeme.york.dynamicmodels.DynamicNodeMemento;
import com.zygomeme.york.dynamicmodels.ErrorItem;
import com.zygomeme.york.dynamicmodels.ModelErrorReport;

/**
 * **********************************************************************
 *   This file forms part of the ZygoMeme York project - an analysis and
 *   modelling platform.
 *  
 *   Copyright (c) 2009 ZygoMeme Ltd., email: coda@zygomeme.com
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * **********************************************************************
 * 
 * Class to write the Model History out to a HTML file, nicely formatting it as it goes.
 * KIS solution - does not separate the HTML template into a other files. 
 * Look and Feel of the report can be changed by editing the CSS file "history_style.css"   
 * 
 */
public class DynamicModelHistoryReporter {

	public DynamicModelHistoryReporter(){
	}
	
	public void writeReport(DynamicModelHistory history, String fileName) throws IOException{

		// Eventually the HTML loaded from a template file so that the user can edit the L&F. 
		// The user will be able to select which of a number of templates to use. This feature
		// enhancement is available customer demand/feedback. The CSS is already available 
		// for editing. 
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
			
			out.write("<title>ZygoMeme - Report </title>");

			out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"history_style.css\">");
			out.write("<div id='main_column'>\n<div id='main'>");
			out.write("<div id='content_wrapper'><div id='content'><br>\n<div class='article'>\n");
			
			// Summary
			out.write("<h2><a href='file:///" + fileName + "'>Summary</a></h2>\n");
			
			if(history.getStatus() == DynamicModelHistory.StatusType.SUCCESS){
				out.write("<div>Model successfully completed at time point t+" + history.getTimePointCount() + " with no errors.</div>");				
			}
			else{
				out.write("<div>The model did not complete its run. </div>");
				outputErrorReport(history, out);				
			}
			
			// Time points
			int t = 0;
			int timePointCount = history.getHistory().size();
			
			if(timePointCount > 0){
				outputInitialState(history, out);
			}
			
			for(t = 1; t < timePointCount; t++){
				Map<String, DynamicNodeMemento> snapshot = history.getHistory().get(t);
				out.write("<div id='timepoint'><h3>Time point: t+" + t + "</h3>\n"); 
				for(String idString: snapshot.keySet()){
					DynamicNodeMemento memento = snapshot.get(idString);
					DynamicModelNode node = memento.getNode();
					if(history.getStartNodeIds().contains(idString)){
						out.write("<div id='node'>\"" + idString + "\" (start node)</div><div id='explanation'>\n");
					}
					else{
						out.write("<div id='node'>\"" + idString + "\"</div><div id='explanation'>\n");
					}
					Map<String, Double> inputs = memento.getInputs();
					out.write("Inputs:<br/>\n");
					if(inputs.size() > 0){
						for(String inputIds: inputs.keySet()){
							out.write("<div id='attribute_value'>" + inputIds + " = " + inputs.get(inputIds) + "</div>\n");
						}
					}
					else{
						out.write("<div id='attribute_value'>none</div>\n");
					}
					out.write("Expression: <br/>\n");
					if(node.getExpression() != null){
						out.write("<div id='attribute_value'>" + node.getIdString() + " = " + node.getExpression() + "</div>\n");
					}
					else{
						out.write("<div id='attribute_value'>none</div>\n");
					}
					
					out.write("Output:<br/><div id='attribute_value'>" + node.getIdString() + " = " + memento.getOutputValue() + "</div>\n");				
					out.write("</div><br/>\n");
				}
				out.write("</div><hr/>\n");
			}
			out.write("</div>\n");
			out.write("<br/>\n");
			out.write("</div>\n");			
			out.write("</div>\n");
			out.write("</div>\n");

			out.close();
		} catch (IOException e) {
			throw e;
		}
		
	}
	
	private void outputInitialState(DynamicModelHistory history, BufferedWriter out) throws IOException{
		
		Map<String, DynamicNodeMemento> snapshot = history.getHistory().get(0);
		out.write("<div id='timepoint'><h3>Time point: t+0 (initial state)</h3>\n"); 
		for(String idString: snapshot.keySet()){
			DynamicNodeMemento memento = snapshot.get(idString);
			DynamicModelNode node = memento.getNode();
			if(history.getStartNodeIds().contains(idString)){
				out.write("<div id='node'>\"" + idString + "\" (start node)</div><div id='explanation'>\n");
			}
			else{
				out.write("<div id='node'>\"" + idString + "\"</div><div id='explanation'>\n");
			}

			out.write("Initial Value: <br/>\n");
			out.write("<div id='attribute_value'>" + node.getInitialValue() + "</div>\n");
			
			out.write("Output:<br/><div id='attribute_value'>" + node.getIdString() + " = " + memento.getOutputValue() + "</div>\n");				
			out.write("</div><br/>\n");
		}
		out.write("</div><hr/>\n");
	}
	
	private void outputErrorReport(DynamicModelHistory history, BufferedWriter out) throws IOException{
		
		ModelErrorReport errorReport = history.getErrorReport();
		List<ErrorItem> errors = errorReport.getReport();
		
		try {			
			int i = 1;
			for(ErrorItem item: errors){
				out.write("<div id='error'>Error: #" + i + "</div><div id='error_message'>\n");
				if(item.getNodeId() != null){
					out.write("Error with node \"" + item.getNodeId() + "\"<br/>");
				}
				out.write(item.getMessage() + "</div>\n");
				i++;
			}			
			out.write("</div>\n");
		} catch (IOException ioe) {
			throw ioe;
		}
	}
}
