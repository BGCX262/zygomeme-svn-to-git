package com.zygomeme.york.propertiesdialog;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.zygomeme.york.dynamicmodels.LoopConfigurationBean;
import com.zygomeme.york.gui.YorkBrowser;

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
 *  Panel for the UI of the nested loop feature (as of version 0.5.3 this
 *  has yet to be fully implemented) 
 * 
 */
public class ConfigureNestedLoopUI {

	private IndividualPanelDialog propsDialog;

	@SuppressWarnings("unchecked")
	public Map<String, LoopConfigurationBean> getLoopConfiguration(YorkBrowser browser, List<String> candidateNodeIds){

		//List<String> names = Arrays.asList(new String[]{"Tom", "Dick", "Harry"});
		propsDialog = new IndividualPanelDialog(browser, new SpinnerListPanel(candidateNodeIds, 23, "Nested Loops"), "Configure Nested Loops", new Dimension(520, 520));

		propsDialog.showDialog();		

		if(!wasCancelled()){
			if(propsDialog.getResultsMap().get("config") != null){
				return (Map<String, LoopConfigurationBean>) propsDialog.getResultsMap().get("config");
			}
		}
		return null;
	}


	public boolean wasCancelled(){
		return propsDialog.getWasCancelled();
	}

	public static void main(String[] args) {

		ConfigureNestedLoopUI cnlui = new ConfigureNestedLoopUI();
		cnlui.getLoopConfiguration(null, Arrays.asList(new String[]{"Tom", "Dick", "Harry"}));
	}
}
