package com.zygomeme.york.util;   	

import java.awt.FontMetrics;
import java.util.*;

import org.apache.log4j.Logger;

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
 * Utils for String manipulation. 
 * 
 */

public class StringUtil
{
	// Don't include '\'' (single quotes in the list of punctuations as the returned string has lost
	// information - it could be "...Harry's is the..." or "...Harry's is a letter'..."
    private static char[] puntuations = new char[]{'.',',',';','?','(',')','!',':','"','/', '{','}','[',']','-','_','£','$','%'};
    private static char[] nonAlphabetCharacters = new char[]{'"','~','/', '{','}','[',']','£','$', '%','&','*','@','#','<','>','-','+','=','^'};
    
    
    
    public static String[] getWordAsArray(String oneWord){
    
    	String[] x = new String[oneWord.length()];
    	
    	char[] cra = oneWord.toCharArray();
    	for(int i = 0; i < oneWord.length(); i++){
    		x[i] = "" + cra[i]; 
    	}
    	return x;
    }
    	
    /** 
	Used in the display of arrays.
    **/
    public static String arrayToString(String pattern[]){
        
    	if(pattern == null){
    		return "";  
    	}
    	
        StringBuffer buffer = new StringBuffer();
        
        for(int i = 0; i < pattern.length; i++){
            buffer.append(pattern[i] + " ");
        }
        return buffer.toString().trim();
    }

    public static String[] stringToArray(String newString) {
        
    	if(newString == null){
    		return null;
    	}
    	
        StringTokenizer st = new StringTokenizer(newString);
        String[] stringArray = new String[st.countTokens()];

        int i = 0;
        while (st.hasMoreTokens()) {
            stringArray[i] = st.nextToken();
            i++;
        }
        return stringArray;
    }

    public static String listToString(List<Object> pattern) {

        StringBuffer buffer = new StringBuffer();
        Iterator<Object> it = pattern.iterator();
        String item = null;
        Object obj = null;
        while (it.hasNext()) {
            obj = it.next();
            item = (String) obj.toString();
            buffer.append(item + " ");
        }

        return buffer.toString().trim();
    }
    
    /**
     * Checks to see if the two given arrays are equal.
     * 
     * @param list1
     * @param list2
     * @return boolean - true if the two arrays are equal, false 
     * otherwise
     */
    public static boolean areArraysEquals(List<String[]> list1, 
                                         List<String[]> list2)
    {
        if(list1.size() != list2.size()){
            return false;
        }
        
        String[] element1, element2;
        int matchCount = 0; int  i = 0;
        // Have a array to mark which ones have been matched against
        boolean[] alreadyMatched = new boolean[list1.size()];
        for (int j = 0; j < alreadyMatched.length; j++) {
            alreadyMatched[j] = false;
        }

        // Matched the elements in the array to elements in the other
        // array that have not yet been matched against
        for (Iterator<String[]> it1 = list1.iterator(); it1.hasNext();) {
            element1 = it1.next();
            i = 0;
            for (Iterator<String[]> it2 = list2.iterator(); it2.hasNext();i++) {
                element2 = it2.next();
                if(!alreadyMatched[i] && 
                   equals(element1, element2)){
                    matchCount++;
                    alreadyMatched[i] = true;
                    break; // Jump out of the inner loop as we've matched
                }
            }
        }
        // If we have matched everything then return true
        if(matchCount == list1.size()){
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * Looks to see if the given String array is in the given List 
     * of String arrays. Returns true if this is the case, false
     * otherwise.
     */
    public static boolean contains(List<String[]> list, String[] stringArray)
    {
        for(String[] element: list){
            if(equals(element, stringArray)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check to see if the two string arrays have the same pattern
     */
    public static boolean equals(String[] one, String[] two) 
    {
        if (one.length != two.length) {
            return false;
        }

        int i;
        int count = one.length;
        for (i = 0; i < count; i++) {
            if (!one[i].equals(two[i])) {
                return false;
            }
        }
        return true;
    }
    
    public static String[] copy(String[] source)
    {
        if(source == null){
            return null;
        }
        
        String[] newCopy = new String[source.length];
        for(int i = 0; i < source.length; i++){
            newCopy[i] = new String(source[i]);
        }
        return newCopy;
    }
     
    /** Method to spread out all the puntuation so that it can 
     * be more easily analysed by the program.
     * @param input
     * @return String - the input string with the punctuation spread out. So 
     * "won't" becomes "won ' t"
     */
    public static String getSpacedPuntuation(String input)
    {
        StringBuffer buffer = new StringBuffer();
        int len = input.length();
  
        boolean justAddedASpace = false;
        char letter;
        for (int i = 0; i < len; i++) {
            letter = input.charAt(i);            
            // If its puntuation then put it in spaced out
            if(isPuntuation(letter)){
                if(justAddedASpace){
                    buffer.append(letter + " "); // preceeding space already
                }
                else{
                    buffer.append(" " + letter + " "); // no preceeding space
                }
                justAddedASpace = true;
            }
            else{ // its a space or a letter - not a punt
                if(letter == ' ' && !justAddedASpace){ // add space if no space already
                    buffer.append(' ');
                    justAddedASpace = true;
                }
                else{
                    if(letter != ' '){ // otherwise add the letter
                        buffer.append(letter);
                        justAddedASpace = false; // reset the space added flag
                    }
                }
            }
        }
        return buffer.toString().trim();
    }
   
    public static String wrapLinesToWidth(String string, int maxWidth, FontMetrics fm){
       return wrapLines(string, maxWidth, fm);
    }
    
    public static String wrapLines(String input, int len, FontMetrics fontMetrics){

    	String output = "";
        int pos = 0;
        String line = "";
        String nextBit = "";
        int spacePos = 0;
        while(pos < input.length()){
            // Get the next white space
            spacePos = input.indexOf(" ", pos);
            if(spacePos == -1){
                spacePos = input.length(); // or end of string
            }
            // Get the bit up to the space
            nextBit = input.substring(pos, spacePos);

            // If we add this next bit and then combined length is still less than "len" then..
            if(!(fontMetrics.stringWidth((line + nextBit)) > len)){
                output += nextBit + " "; // .. add it
                line += nextBit; // .. and record this line
            }
            else{
                output += "\n" + nextBit + " "; // ..or add a return character
                line = "";         // .. and zero the length of the line. 
            }
            pos += nextBit.length() + 1; // move the pos on so we don't find the same space again
        }
        
        // Only remove trailing spaces
        return removeTrailingSpaces(output);
    }
    
    public static String removeTrailingSpaces(String input){
        
        if(input.endsWith(" ")){
            return removeTrailingSpaces(input.substring(0, input.length() - 1));
        }
        else{
            return input;
        }
    }
    
    public static boolean isPuntuation(char letter){
        
        for(char punt: puntuations){
            if(letter == punt){
                return true;
            }
        }
        return false;
    }

    public static boolean isNonAlphabetCharacter(char letter){
        
        for(char punt: nonAlphabetCharacters){
            if(letter == punt){
                return true;
            }
        }
        return false;
    }

    public static boolean isSeparator(char letter, char[] separators){
        
        for(char sep: separators){
            if(letter == sep){
                return true;
            }
        }
        return false;
    }
    
    public static boolean isNumber(String string){
		
		try {
			Double.parseDouble(string);
			return true;
		} catch (Exception e) {
			return false;
		}				
	}

    /**
     * Create an indentation of a given number of spaces.
     * @param count
     * @return a string made up of 'count' number of spaces
     */
    public static String getIdent(int count){
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < count; i++){
            buffer.append(" ");
        }
        return buffer.toString();
    }
    
    public static String getUnescapedText(String escaped){
     
        String remapped = escaped.replaceAll("&amp;", "&");
        String finalString = remapped.replaceAll("&quot;", "\"");
        return finalString;
    }

    public static String getEscapedText(String unescaped){
        return getEscapedText(unescaped, null);
    }
    
    public static String getEscapedText(String unescaped, Logger logger){
    
        if(logger != null){
            logger.info("About to escape some text >" + unescaped + "<");
        }
        String deAmpped = replaceAmps(unescaped);
        return replace(deAmpped, "\"", "&quot;");
    }
        
    private static String replaceAmps(String string){
        
        if(string == null){
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < string.length(); i++){
            if(string.charAt(i) == '&'){
                if((i + 5) <= string.length() && string.substring(i, i + 5).equals("&amp;")){
                    buffer.append(string.charAt(i));
                }
                else{
                    buffer.append("&amp;");
                }
            }
            else{
                buffer.append(string.charAt(i));
            }
        }
        
        return buffer.toString();
    }

    private static String replace(String string, String seq, String replacement){
        if(string == null){
            return null;
        }
        else{
            return string.replace(seq, replacement);
        }
    }
    
    public static String[] mySplit(String string, String pattern){
        return string.split(pattern);
    }
    
    /**
     * Keep replacing the given string until they are all gone - a multi-pass rather than 
     * the normal single pass of replaceAll
     * 
     * @param string
     * @param doomed
     * @param newbie
     * @return The string with all the doomed strings replaced with the newbie strings
     */
	public static String repeatedlyReplace(String string, String doomed, String newbie){
		
		String cleaned = string.replaceAll(doomed, newbie);
		while(cleaned.length() != string.length()){
			string = cleaned;
			cleaned = string.replaceAll(doomed, newbie);
		}
		return cleaned;
	}
    /**
     * Splits the string into component parts. The splits are made where ever a separator is found. 
     * The array returned also includes the separators. For example the string " a + boo foo * c" with the
     * separators "+" and "*" would return {"a","+","boo foo","*","c"}
     * @param input
     * @param separators
     * @return
     */
    public static String[] retainingSplit(String input, char[] separators){
    	
    	String string = new String(input);
    	int lastSplit = 0;
		char letter;
    	List<String> segments = new ArrayList<String>();
		for(int i = 0; i < string.length(); i++){
    		letter = string.charAt(i);            
    		// If its one of the separators then split it out.
    		if(isSeparator(letter, separators)){
    			if(lastSplit == 0 && i == 0){
        			segments.add(string.substring(i, i + 1).trim());	
    			}
    			else{
    				String prev = string.substring(lastSplit, i).trim();
    				if(prev.length() > 0){
    					segments.add(prev);
    				}
    				segments.add(string.substring(i, i + 1).trim());
    			}
				lastSplit = i + 1;
    		}
    	}    	
		// Include the last one if not already done so
		if(lastSplit <= (string.length() -1) && string.substring(lastSplit).trim().length() > 0){
			segments.add(string.substring(lastSplit).trim());
		}
		// If not separator found then the return should be the entire input
		if(segments.size() == 0){
			segments.add(string.trim());
		}
		
		return segments.toArray(new String[]{});
    }
    
    public static List<String> getFilteredArray(String[] array, char[] filters){
    
    	List<String> filteredList = new ArrayList<String>();
    	for(String element: array){
    		boolean match = false;
    		for(char c:filters){
    			if(element.equals("" + c)){
    				match = true;
    			}
    		}
    		if(!match){
    			filteredList.add(element);
    		}
    	}
    	
    	
    	return filteredList;
    }
    
    public static String getFilenameWithoutExtension(String name){
        
        if(name.indexOf(".") != -1){
            return name.substring(0, name.indexOf(".")).trim();
        }
        else{
            return name.trim();
        }
    }
    
    /** 
     * Pads out the given string to the given width with spaces added to the right of the string
     * @param string
     * @param width
     * @return padded out string with the padding to the right
     */
    public static String padRight(String string, int width){
    	
    	if(string.length() >= width){
    		return string;
    	}
    	
    	StringBuilder builder = new StringBuilder(string);
    	for(int i = string.length(); i < width; i++){
    		builder.append(" ");
    	}
    	return builder.toString();
    }
    
    /** Truncate the given text to the given width **/
    public static String trunc(FontMetrics fontMetrics, String message, int width, int paddingWidth){

		// Truncate until it fits inside the box
		String truncMessage = message;
		int messageWidth = fontMetrics.stringWidth(truncMessage);
		if(messageWidth < (width - (paddingWidth * 2))){
			return message;
		}
		while(messageWidth >= (width - (paddingWidth * 2)) && truncMessage.length() > 0){
			truncMessage = truncMessage.substring(0, truncMessage.length() - 1);
			messageWidth = fontMetrics.stringWidth(truncMessage + "... ");
		}		
		return truncMessage + "... ";
	}
    
    public static String minimiseSpaces(String string){
    	
    	String newString = "";
    	char lastC = 'x';
    	char currentC;
    	for(int i = 0; i < string.length(); i++){
    		currentC = string.charAt(i);
    		if(lastC != ' ' || currentC != ' '){
    			newString += currentC;
    		}
    		lastC = currentC;
    	}
    	return newString;
    }
}
