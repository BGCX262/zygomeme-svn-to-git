package com.zygomeme.york.xml;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.zygomeme.york.Node;
import com.zygomeme.york.gui.YorkEntityView;
import com.zygomeme.york.gui.PropertiesMemento;
import com.zygomeme.york.gui.model.ViewModel;
import com.zygomeme.york.gui.EntityViewConfig;

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
 * Saves the given viewModel to an XML file.
 * 
 */
public class ViewModelExporter {

	
	public void save(ViewModel viewModel, PropertiesMemento propertiesMemento){

		Document doc;
		try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.newDocument();

            // Prepare the DOM document for writing
			Source source = new DOMSource(doc);
			Element head = doc.createElement("model_view");
			head.setAttribute("name", viewModel.getName());
			viewModel.setVersion(viewModel.getVersion() + 1);
			head.setAttribute("version", "" + viewModel.getVersion());
			
			Element list = doc.createElement("model_list");
			
			for(YorkEntityView modelView: viewModel.getViews()){
				// Don't save DynamicModelNodes using this system. These are saved as part of the 
				// DynamicModel instead. 
				EntityViewConfig config = modelView.getConfig();
				if(config.getType() != EntityViewConfig.Type.DYNAMIC_NODE){
					if(config.getType() == EntityViewConfig.Type.UNKNOWN){
						throw new RuntimeException("Undefined type for " + modelView.getModelsIdString());
					}

					Element view = doc.createElement("view");
					view.setAttribute("name", modelView.getModelsIdString());
					view.setAttribute("filename", propertiesMemento.getDir(config.getType()) + modelView.getModelsIdString() + ".xml");					
					list.appendChild(view);
				}
			}
			head.appendChild(list);
			
			// Save the arcs
			list = doc.createElement("arcs");
			Map<String, List<Node>> arcMap = viewModel.getArcMap().getFromMap();
			for(String id: arcMap.keySet()){
				for(Node node: arcMap.get(id)){
					Element arcElement = doc.createElement("arc");
					arcElement.setAttribute("from", node.getIdString());
					arcElement.setAttribute("to", id);
					list.appendChild(arcElement);
				}
			}
			head.appendChild(list);

			// Save the view config - initially the just the colour
			Element guiConfig = doc.createElement("gui_config");
			Element colourElement = doc.createElement("background_color_top");
			colourElement.setAttribute("red", viewModel.getBackgroundGradientColorTop().getRed() + "");
			colourElement.setAttribute("green", viewModel.getBackgroundGradientColorTop().getGreen() + "");
			colourElement.setAttribute("blue", viewModel.getBackgroundGradientColorTop().getBlue() + "");
			guiConfig.appendChild(colourElement);			
			colourElement = doc.createElement("background_color_bottom");
			colourElement.setAttribute("red", viewModel.getBackgroundGradientColorBottom().getRed() + "");
			colourElement.setAttribute("green", viewModel.getBackgroundGradientColorBottom().getGreen() + "");
			colourElement.setAttribute("blue", viewModel.getBackgroundGradientColorBottom().getBlue() + "");
			guiConfig.appendChild(colourElement);			

			head.appendChild(guiConfig);

			doc.appendChild(head);

			// Prepare the output file
			String filename = propertiesMemento.getTabDir() + viewModel.getName() + ".xml";
			Result result = new StreamResult(new File(filename));

			// Write the DOM document to the file
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.setOutputProperty(OutputKeys.INDENT,"yes");
			xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
			xformer.transform(source, result);

			// Write the backup file
			System.out.println("!!!!! Backup not written !!!!!");
			/*String pathname = propertiesMemento.getBaseDir();
			String name = filename.substring(filename.lastIndexOf("\\") + 1, filename.lastIndexOf(".xml"));
			System.out.println("Save " + pathname + "backups\\" + name + "_" + viewModel.getVersion() + ".xml");
			result = new StreamResult(new File(pathname + "backups\\" + name + "_" + viewModel.getVersion() + ".xml"));
			
			// Write the DOM document to the file
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT,"yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
			transformer.transform(source, result);
			*/			
			
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException(e);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
        }
	}
}
