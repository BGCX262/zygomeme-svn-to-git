package com.zygomeme.york;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

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
 * Miscellaneous utilities.
 *
 */
public class YorkUtils {

	public static void setupDirectoryStructure(String dataDir){
		
		// Create these directories if not already present
		mkDirIfNotPresent(dataDir);
		mkDirIfNotPresent(dataDir + File.separator + "tabs");
		mkDirIfNotPresent(dataDir + File.separator + "dynamic models");
		mkDirIfNotPresent(dataDir + File.separator + "reports");
		
	}
	
	public static void mkDirIfNotPresent(String newDirName){
		
		File modelPatternsDir = new File(newDirName);
		if(!modelPatternsDir.exists()){
			modelPatternsDir.mkdir();
		}
	}
	
	public static List<String> getModelListFromDisk(String tabDirName){
		
	    FilenameFilter filter = new FilenameFilter() {
	        public boolean accept(File dir, String filename) {
	            return filename.endsWith(".xml");
	        }
	    };
	    
		File tabDir = new File(tabDirName);
		List<String> modelList = Arrays.asList(tabDir.list(filter));
		return modelList;
		
	}
}
