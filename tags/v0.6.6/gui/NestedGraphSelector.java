package com.zygomeme.york.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
 * Provides the functionality for the user to select which of a number of
 * model run results to display.   
 * 
 */
public class NestedGraphSelector extends JDialog implements ChangeListener, ActionListener
{

	private static final long serialVersionUID = -5628186872195013880L;
	
	private Map<JSlider, String> sliderMap = new HashMap<JSlider, String>();
	private Map<String, Integer> percentageMap = new HashMap<String, Integer>();
	private Map<String, Double> valueMap = new HashMap<String, Double>();
	private Map<String, Integer> indexMap = new HashMap<String, Integer>();
	private Map<String, LoopConfigurationBean> loopConfig;
	private JButton cancelButton;
	private JButton setButton;
	private boolean dialogCancelled = false;
	private double percentageStepSize;

	public NestedGraphSelector(YorkBrowser browserIn, Map<String, LoopConfigurationBean> loopConfigIn){
		super(browserIn, "Graph Selector", true);
		this.loopConfig = loopConfigIn;
	}
	
	private void init(){
		
		JPanel allSlidersPanel = new JPanel();
		allSlidersPanel.setLayout(new BoxLayout(allSlidersPanel, BoxLayout.Y_AXIS));
		for(String id: loopConfig.keySet()){
			allSlidersPanel.add(getNewSliderPanel(id, id, loopConfig.get(id)));			
		}
		
		// Add the button panel
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		setButton = new JButton("Set");
		setButton.addActionListener(this);
		
		// Lay out buttons from left to right.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(cancelButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(setButton);
		
		Container contentPane = getContentPane();
		
		contentPane.add(allSlidersPanel, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);
		pack();
		setSize(new Dimension(800, 400));
	}
	
	public void showDialog(){
		init();
		setVisible(true);	
	}
	
	private JPanel getNewSliderPanel(String label, String id, LoopConfigurationBean loopConfigBean){

		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.X_AXIS));
		sliderPanel.add(new JLabel(label + ": "));
				
		// Create the labels
		Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
		int i = 0;
		int labelCount = (int)((loopConfigBean.getStop() - loopConfigBean.getStart()) / loopConfigBean.getStep());
		System.out.println("LabelCount:" + labelCount);
		percentageStepSize = (100.0 / labelCount); 
		//for(double d = loopConfig.getStart(); d <= loopConfig.getStop(); d += loopConfig.getStep()){
		double d = loopConfigBean.getStart();
		for(int p = 0; p <= labelCount; p++){
			labels.put(i, new JLabel("" + d));
			i = (int)((((double)p) + 1.0) * percentageStepSize);
			d += loopConfigBean.getStep();
		}
		JSlider slider = new JSlider();
		slider.setLabelTable(labels);
		System.out.println("PercentageSteSize:" + percentageStepSize); 
		slider.setPaintLabels(true);
		slider.addChangeListener(this);		
		sliderPanel.add(slider);

		sliderMap.put(slider, id);
		
		return sliderPanel;
	}
		
	public void stateChanged(ChangeEvent e) {

		JSlider slider = (JSlider)e.getSource();
		String id = sliderMap.get(slider);
		LoopConfigurationBean loopConfigBean = loopConfig.get(id);
		double labelCount = ((loopConfigBean.getStop() - loopConfigBean.getStart()) / loopConfigBean.getStep());
		double realValue = ((labelCount / 10.0) * (double)slider.getValue()) + loopConfigBean.getStart();
		System.out.println("value:" + realValue);

        if (!slider.getValueIsAdjusting()) {
            percentageMap.put(id, slider.getValue());
            
            // Find nearest value
            int index = (int)((realValue - loopConfigBean.getStart()) / loopConfigBean.getStep());
            double nearest = (((double)index) * loopConfigBean.getStep()) + loopConfigBean.getStart();
            valueMap.put(id, nearest);
            indexMap.put(id, index);
        }		
	}

	public Map<String, Integer> getPercentageMap(){
		return percentageMap;
	}
	
	public Map<String, Double> getValueMap(){
		return valueMap;
	}
	
	public Map<String, Integer> getIndexMap(){
		return indexMap;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		System.out.println("action event:" + actionEvent);
		
		if (actionEvent.getSource() == setButton) {
			dialogCancelled = false;
		}
		else{
			dialogCancelled = true;			
		}
		setVisible(false);		
	}
	
	public boolean getWasCancelled(){
		return dialogCancelled;
	}
	
	public static void main(String[] args) {

		LoopConfigurationBean loopConfig = new LoopConfigurationBean("p1");
		loopConfig.set(LoopConfigurationBean.ParamType.START, 20);
		loopConfig.set(LoopConfigurationBean.ParamType.STOP, 200);
		loopConfig.set(LoopConfigurationBean.ParamType.STEP, 10);
		Map<String, LoopConfigurationBean> configMap = new Hashtable<String, LoopConfigurationBean>();
		configMap.put("P1", loopConfig);
		NestedGraphSelector nestedGraphSelector = new NestedGraphSelector(null, configMap);
		nestedGraphSelector.showDialog();	
		Map<String, Integer> resultsMap = nestedGraphSelector.getPercentageMap();
		System.out.println("Percentage:" + resultsMap);

		Map<String, Double> valueMap = nestedGraphSelector.getValueMap();
		System.out.println("Results:" + valueMap);

		Map<String, Integer> indexMap = nestedGraphSelector.getIndexMap();
		System.out.println("Indexes:" + indexMap);

	}

}
