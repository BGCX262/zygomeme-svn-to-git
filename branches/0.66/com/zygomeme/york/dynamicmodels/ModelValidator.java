package com.zygomeme.york.dynamicmodels;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.zygomeme.york.Node;
import com.zygomeme.york.dynamicmodels.DynamicModelHistory.StatusType;

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
 * Validates the given model generating an error report for any problems 
 * found.
 *
 */
public class ModelValidator {

	private Logger logger = Logger.getLogger(ModelValidator.class);
	
	public void validate(DynamicModel model, ModelErrorReport report){

		Set<String> nodeIds = model.getNodeIds();
		Map<String, Node> nodeMap = model.getNodeMap();

		// Check that the output nodes that each node refers to are present in the model
		for(String nodeId: nodeIds){
			Node node = nodeMap.get(nodeId);
			logger.info("validate() node:" + node);
			for(String toNodeId: node.getToNodeIds()){
				if(!nodeMap.containsKey(toNodeId)){
					logger.info(toNodeId + " does not exist");
					report.addError(ModelErrorReport.ErrorType.MISSING_TO_NODE, nodeId);					
				}
			}
		}

		// Check to see if the expressions are valid
		ExpressionValidator equationValidator = new ExpressionValidator();
		String expression;
		for(Node node: nodeMap.values()){
			expression = ((DynamicModelNode)node).getExpression();
			if(expression != null && expression.length() > 0){
				List<ErrorItem> errors = equationValidator.validateEquation(expression, nodeMap.keySet(), node);
				if(errors.size() > 0){
					report.addErrors(errors);
				}
			}
		}

		logger.info("validate() ErrorReport:" + report);
		if(report.getStatus() == StatusType.ERROR){
			JOptionPane.showMessageDialog(null, "There are errors", "Warning", JOptionPane.ERROR_MESSAGE);	
			report.setStatus(StatusType.ERROR);
		}
	}
}
