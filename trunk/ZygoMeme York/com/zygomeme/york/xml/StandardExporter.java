package com.zygomeme.york.xml;

import java.io.File;

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
 * Simple exporter is a super abstract class from which all other York exporter are derived.
 * 
 */

public abstract class StandardExporter{

	public void save(YorkEntityView view, String filename){

		Document doc;
		try {

			doc = getDoc(view);
			Source source = new DOMSource(doc);

			// Prepare the output file
			//String filename = viewModel.getFilename();
			Result result = new StreamResult(new File(filename));

			// Write the DOM document to the file
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT,"yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
			transformer.transform(source, result);

		} catch (TransformerConfigurationException e) {
		} catch (TransformerException e) {
		} 		
	}

	protected Document getDoc(YorkEntityView view){
		throw new RuntimeException("This method must be overriden");
	}
	
	protected Element appendNode(Document doc, Node node){
		throw new RuntimeException("This method (\"appendNode(Document doc, Node node)\") must be overriden");
	}

}
