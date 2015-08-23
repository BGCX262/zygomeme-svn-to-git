package com.zygomeme.york.util.io;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.filechooser.FileFilter;

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
 * Filters file names for the save and open dialogs.  
 * 
 */
public class GenericFileFilter extends FileFilter
{
    private List<String> extensions = new LinkedList<String>();
    private String description = "";
    
    public GenericFileFilter(){
        super();
    }
    
    public GenericFileFilter(String newExtensionList, String newDescription){
        super();
        LinkedList<String> newExtensions = new LinkedList<String>();
        for(String extension: newExtensionList.split(" ")){
        	newExtensions.add(extension.trim());	
        }        
        setFilter(newExtensions, newDescription);
    }

    protected void setFilter(List<String> newExtensions, String newDescription){
        this.extensions.addAll(newExtensions);
        this.description = newDescription;   
    }

    public boolean accept(File file){

        // Get the extensions
        String inputExtension = getExtension(file);
        if(extensions.contains(inputExtension) || file.isDirectory()){
            return true;
        }
        else{
            return false;
        }
    }
    
    public String getDescription(){
        
        return description;
    }
    
    private String getExtension(File file){

        // Get the extensions
        String filename = file.getName();
        int splitIndex = filename.lastIndexOf(".");
        String ext = "";
        if(splitIndex > -1){
            ext = filename.substring(splitIndex + 1, filename.length());
        }
        return ext;            
    }
}

