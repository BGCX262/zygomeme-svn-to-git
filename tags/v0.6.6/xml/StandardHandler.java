package com.zygomeme.york.xml;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.zygomeme.york.xml.SimpleParser;

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
 * Simple Handler, used to abstract away common handler functions. 
 * 
 */

public class StandardHandler extends DefaultHandler{
	
	protected String currentString = "";

	/** Takes a given string and starts parsing. **/
	public boolean parse(String string)
	{
		// TODO ensure client code is checking the return flag 
		// and maybe throw an exception instead.
		boolean parsedOK = true;
		try {
			SimpleParser.parse(new StringReader(string), this);
		}
		catch (ParserConfigurationException e) {
			System.out.println("ParserConfigurationException");
			parsedOK = false;
		}
		catch (SAXException e) {
			System.out.println("SAXException");
			parsedOK = false;
		}
		catch (IOException e) {
			System.out.println("IOException");
			parsedOK = false;
		}
		catch(Exception e){
			System.out.println("Exception: " + e);
			System.out.print("Stack trace: " );
			e.printStackTrace();
			parsedOK = false;
		}

		return parsedOK;
	}
	
	public void characters(char[] chars, int start, int length)
    {
        currentString += (new String(chars)).substring(start, start + length);        
    }

}
