package com.zygomeme.york.propertiesdialog;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import com.zygomeme.york.dynamicmodels.LoopConfigurationBean;

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
 *
 * Panel for the properties dialog. This panel is used for spinner based input.  
 * 
 */

public class SpinnerListPanel implements MappedPanelCreator{

	private String frameTitle = "";
	private String key = "";
	private MappedPanel mappedPanel;
	private List<String> names;

	public SpinnerListPanel(List<String> names, int columnWidth, String frameTitle){
		this.frameTitle = frameTitle;
		this.names = names;
	}

	public String getKey(){
		return key;
	}
	
	public MappedPanel getPanel(){

		// Titled border
		mappedPanel = new MappedPanel();

		// Create spinner
		for(String name: names){
			addSpinnerRange(name, mappedPanel);
		}

		mappedPanel.setBorder(BorderFactory.createTitledBorder(frameTitle));

		mappedPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		mappedPanel.setPreferredSize(new Dimension(400, 200));
		return mappedPanel;
	}

	private void addSpinnerRange(String variableName, MappedPanel mappedPanel){

		JPanel panel = new JPanel();
		
		JLabel variableLabel = new JLabel(variableName);
		variableLabel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		panel.add(variableLabel);

		// Create spinner
		JLabel label1 = new JLabel("start:");
		label1.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		panel.add(label1);

		JSpinner start = new JSpinner();
		start.setValue(new Integer(0));		
		start.setPreferredSize(new Dimension(80, 20));
		panel.add(start);
		mappedPanel.mapComponent(start, variableName + ":" + LoopConfigurationBean.ParamType.START); // The key is the variableName with ".start" appended

		// Create spinner
		JLabel label2 = new JLabel("stop:");
		label2.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		panel.add(label2);

		JSpinner stop = new JSpinner();
		stop.setValue(new Integer(100));		
		stop.setPreferredSize(new Dimension(80, 20));
		panel.add(stop);
		mappedPanel.mapComponent(stop, variableName + ":" + LoopConfigurationBean.ParamType.STOP);
		
		// Create spinner
		JLabel label3 = new JLabel("step:");
		label3.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		panel.add(label3);

		JSpinner step = new JSpinner();
		step.setValue(new Integer(10));		
		step.setPreferredSize(new Dimension(80, 20));
		panel.add(step);
		mappedPanel.mapComponent(step, variableName + ":" + LoopConfigurationBean.ParamType.STEP);

		mappedPanel.add(panel);
	}
	
	public void setResults() {

		Map<String, LoopConfigurationBean> configurationMap = new HashMap<String, LoopConfigurationBean>();
		for(String id: mappedPanel.getMappedComponents().keySet()){
			JSpinner spinner = ((JSpinner)mappedPanel.getMappedComponents().get(id));

			String nodeId = id.split(":")[0].trim();
			String paramId = id.split(":")[1].trim();
			
			if(!configurationMap.containsKey(nodeId)){
				configurationMap.put(nodeId, new LoopConfigurationBean(nodeId));
			}

			// Convert the value and place in the configuration bean
			Object obj = spinner.getValue();
			if(obj instanceof Integer){
				configurationMap.get(nodeId).set(LoopConfigurationBean.ParamType.valueOf(paramId), (Integer)obj);
			}
			if(obj instanceof Double){
				configurationMap.get(nodeId).set(LoopConfigurationBean.ParamType.valueOf(paramId), (Double)obj);
			}

			// Complains if not a integer or double
			if(!(obj instanceof Double) && !(obj instanceof Integer)){
				JOptionPane.showMessageDialog(null, obj + " is not a valid number", "Error", JOptionPane.ERROR_MESSAGE);
				configurationMap = null;
				return;
			}
		}
		// Place the completed bean in the results map for the client code to read
		mappedPanel.setResult("config", configurationMap);
	}
}
