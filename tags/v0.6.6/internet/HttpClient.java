package com.zygomeme.york.internet;

import java.io.*;
import java.net.*;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.zygomeme.york.gui.PropertiesMemento;

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
 * This class connects to a Web server and down loads the specified URL. 
 * It uses the HTTP protocol directly.
 * This class is used to connect to the Internet to check for updates. 
 * 
 **/
public class HttpClient {

	private Logger logger = Logger.getLogger(HttpClient.class);
	
	public String getPage(String urlString) throws SocketTimeoutException, UnknownHostException {

		StringBuilder content = new StringBuilder();
		try{
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			conn.setRequestProperty("User-Agent", "ZygoMeme York Version " + PropertiesMemento.APP_VERSION_STRING);
			conn.setDoOutput(true);

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				content.append(line + "\n");
			}
			rd.close();

			logger.info("Finished the download from the internet, URL: " + urlString);            
		}
		catch(SocketTimeoutException ste){
			throw ste;
		}
		catch(UnknownHostException uhe){
			throw uhe;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return content.toString();
	}

	/**
	 * Gets a set of Properties from the given URL. 
	 * Used to down load information about version updates.
	 * 
	 * @param urlString
	 * @return Properties as made available at the given URL
	 */
	public Properties getProperties(String urlString) throws SocketTimeoutException, UnknownHostException {

		Properties props = new Properties();
		
		try{
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			conn.setRequestProperty("User-Agent", "ZygoMeme York Version " + PropertiesMemento.APP_VERSION_STRING);
			conn.setDoOutput(true);

			// Get the response
			props.load(conn.getInputStream());
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((rd.readLine()) != null) {
			}
			rd.close();
			
			logger.info("Finished the properties download, URL: " + urlString);
			return props;
		}
		catch(SocketTimeoutException ste){
			throw ste;
		}
		catch(UnknownHostException uhe){
			throw uhe;
		}
		catch (Exception e) {
			e.printStackTrace();
			return props;
		}
	}
}
