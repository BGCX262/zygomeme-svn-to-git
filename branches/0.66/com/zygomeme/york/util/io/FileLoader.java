package com.zygomeme.york.util.io;

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
 * FileLoader.java
 * 
 * File Loading and Saving Utils. 
 * 
 */

import java.io.*;

public class FileLoader
{
    public static String load(String filename) throws IOException
    {
        File file;
        FileReader reader = null;

        file = new File(filename);
        reader = new FileReader(file);
        int size = (int) file.length();
        char[] data = new char[size];
        int chars_read = 0;
        try{
            while(chars_read < size){
                chars_read += reader.read(data, chars_read, size - chars_read);
            }
        }
        catch(IOException e){
            throw e;
        }
        finally{
            try { 
            	if (reader != null){
            		reader.close();
            	}
            }
            catch(IOException e){
                throw e;
            }
        }
        return new String(data); // returns from here if all well
    }

    /**
     * Silent exceptions !! 
     * TODO Deprecate
     * 
     * @param filename
     * @return
     */
    public static String loadFile (String filename)
    {
        try
        {
            return load(filename);
        }
        catch(IOException e)
        {
        	System.out.println("File:\"" + filename + "\" not found");
            return null;
        }
    }

    public static void save(String fileName, String contents)
    {

        try{
        	FileWriter fw = new FileWriter(fileName);
        	BufferedWriter bWriter = new BufferedWriter(fw);

            try{
                bWriter.write(contents);
                bWriter.newLine();
                bWriter.flush();
            }
    	    catch(IOException e){
                System.err.println("Unable to write to: " + fileName);            
            }
    	    fw.close();
        }
        catch (IOException e){
            System.err.println("Unable to save: " + fileName);            
        }
    }    
}
