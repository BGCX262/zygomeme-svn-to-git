package com.zygomeme.york.util;

import junit.framework.TestCase;
import java.util.*;

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
 * Utils for String manipulation. 
 * 
 */
public class StringUtilTest extends TestCase
{
    private String s1 = "S1";
    private String s2 = "S2";
    private String s3 = "S3";

    public void testRetainingSplit(){
    	String input = "(a + b)";
    	char[] separators = new char[]{'(', '+', ')'};
    	String[] res = StringUtil.retainingSplit(input, separators);
    	int i = 0;
    	System.out.println("input:\"" + input + "\", result:" + Arrays.toString(res));
    	String[] expected = new String[]{"(", "a", "+", "b", ")"};
    	//check 
    	boolean correct = true;
    	i = 0;
    	for(String string: res){
    		if(!string.equals(expected[i++])){
    			correct = false;
    		}
    	}
    	assertTrue("Incorrect return values, found " + res.length + " should be 5", res.length == 5);
    	assertTrue("The strings don't match", correct);

    
    	i = 0;
    	input = "a + b";
    	res = StringUtil.retainingSplit(input, separators);
    	System.out.println("input:\"" + input + "\", result:" + Arrays.toString(res));
    	expected = new String[]{"a", "+", "b",};
    	//check 
    	correct = true;
    	i = 0;
    	for(String string: res){
    		if(!string.equals(expected[i++])){
    			correct = false;
    		}
    	}
    	assertTrue("Incorrect return values, found " + res.length + " should be 3", res.length == 3);
    	assertTrue("The strings don't match", correct);

    	i = 0;
    	input = "((a + b))";
    	res = StringUtil.retainingSplit(input, separators);
    	System.out.println("input:\"" + input + "\", result:" + Arrays.toString(res));
    	expected = new String[]{"(", "(", "a", "+", "b", ")", ")"};
    	//check 
    	correct = true;
    	i = 0;
    	for(String string: res){
    		if(!string.equals(expected[i++])){
    			correct = false;
    		}
    	}
    	assertTrue("Incorrect return values, found " + res.length + " should be 7", res.length == 7);
    	assertTrue("The strings don't match", correct);

    	i = 0;
    	input = " ((a + b))";
    	res = StringUtil.retainingSplit(input, separators);
    	System.out.println("input:\"" + input + "\", result:" + Arrays.toString(res));
    	expected = new String[]{"(", "(", "a", "+", "b", ")", ")"};
    	//check 
    	correct = true;
    	i = 0;
    	for(String string: res){
    		if(!string.equals(expected[i++])){
    			correct = false;
    		}
    	}
    	assertTrue("Incorrect return values, found " + res.length + " should be 7", res.length == 7);
    	assertTrue("The strings don't match", correct);

    	i = 0;
    	input = " ((a + b)) ";
    	res = StringUtil.retainingSplit(input, separators);
    	System.out.println("input:\"" + input + "\", result:" + Arrays.toString(res));
    	expected = new String[]{"(", "(", "a", "+", "b", ")", ")"};
    	//check 
    	correct = true;
    	i = 0;
    	for(String string: res){
    		if(!string.equals(expected[i++])){
    			correct = false;
    		}
    	}
    	assertTrue("Incorrect return values, found " + res.length + " should be 7", res.length == 7);
    	assertTrue("The strings don't match", correct);

    	i = 0;
    	input = " ((fish + b)) ";
    	res = StringUtil.retainingSplit(input, separators);
    	System.out.println("input:\"" + input + "\", result:" + Arrays.toString(res));
    	expected = new String[]{"(", "(", "fish", "+", "b", ")", ")"};
    	//check 
    	correct = true;
    	i = 0;
    	for(String string: res){
    		if(!string.equals(expected[i++])){
    			correct = false;
    		}
    	}
    	assertTrue("Incorrect return values, found " + res.length + " should be 7", res.length == 7);
    	assertTrue("The strings don't match", correct);

    	i = 0;
    	input = " ((fish +b))   dog ";
    	res = StringUtil.retainingSplit(input, separators);
    	System.out.println("input:\"" + input + "\", result:" + Arrays.toString(res));

    	expected = new String[]{"(", "(", "fish", "+", "b", ")", ")", "dog"};
    	//check 
    	correct = true;
    	i = 0;
    	for(String string: res){
    		if(!string.equals(expected[i++])){
    			correct = false;
    		}
    	}
    	assertTrue("Incorrect return values, found " + res.length + " should be 8", res.length == 8);
    	assertTrue("The strings don't match", correct);

    	input = "a";
    	res = StringUtil.retainingSplit(input, separators);
    	System.out.println("input:\"" + input + "\", result:" + Arrays.toString(res));
    	expected = new String[]{"a"};
    	//check 
    	correct = true;
    	i = 0;
    	for(String string: res){
    		if(!string.equals(expected[i++])){
    			correct = false;
    		}
    	}
    	assertTrue("The strings don't match", correct);
    	assertTrue("Incorrect return values, found " + res.length + " should be 1", res.length == 1);

    	
    	input = "a+b";
    	res = StringUtil.retainingSplit(input, separators);
    	System.out.println("input:\"" + input + "\", result:" + Arrays.toString(res));
    	expected = new String[]{"a", "+", "b"};
    	//check 
    	correct = true;
    	i = 0;
    	for(String string: res){
    		if(!string.equals(expected[i++])){
    			correct = false;
    		}
    	}
    	assertTrue("The strings don't match", correct);
    	assertTrue("Incorrect return values, found " + res.length + " should be 3", res.length == 3);

    }
    
    public void testAreArrayEquals() 
    {
        List<String[]> list1 = new ArrayList<String[]>();
        List<String[]> list2 = new ArrayList<String[]>();
        
        list1.add(StringUtil.stringToArray(s1));
        list2.add(StringUtil.stringToArray(s1));
        
       assertTrue("These two should be the same", 
                StringUtil.areArraysEquals(list1, list2));
        
        list1.add(StringUtil.stringToArray(s2));
        list2.add(StringUtil.stringToArray(s2));
        
        assertTrue("These two should still be the same", 
                StringUtil.areArraysEquals(list1, list2));
        
        list1.add(StringUtil.stringToArray(s2));
        list2.add(StringUtil.stringToArray(s1));
        
        assertTrue("These two should NOT be the same", 
                !StringUtil.areArraysEquals(list1, list2));               
    }

    public void testContains() 
    {
        List<String[]> list = new ArrayList<String[]>();
        
        list.add(StringUtil.stringToArray(s1));
        list.add(StringUtil.stringToArray(s2));
        list.add(StringUtil.stringToArray(s2));

        assertTrue("The array should contain this string", 
                StringUtil.contains(list, StringUtil.stringToArray(s1)));
        assertTrue("The array should contain this string", 
                StringUtil.contains(list, StringUtil.stringToArray(s2)));
        assertTrue("The array should contain this string", 
                !StringUtil.contains(list, StringUtil.stringToArray(s3)));
    }

    public void testIsSamePattern() 
    {        
        assertTrue("These are the same pattern", 
                StringUtil.equals(StringUtil.stringToArray(s1), 
                                         StringUtil.stringToArray(s1)));
    }
    
    public void testCopy()
    {
        String[] sA = StringUtil.stringToArray("dfsafdsad adfsa asdf");
        assertTrue("These should be the same", 
                StringUtil.equals(sA, StringUtil.copy(sA)));
        
        String[] sB = StringUtil.stringToArray("");
        assertTrue("These should be the same", 
                StringUtil.equals(sB, StringUtil.copy(sB)));
        
        String[] sC = StringUtil.stringToArray("sddfsad ");
        assertTrue("These should be the same", 
                StringUtil.equals(sC, StringUtil.copy(sC)));

        String[] sD = StringUtil.stringToArray(" sddfsad");
        assertTrue("These should be the same", 
                StringUtil.equals(sD, StringUtil.copy(sD)));

    }

    public void testGetSpacedPuntuation()
    {
        String sA = "this is a test.";
        System.out.println(StringUtil.getSpacedPuntuation(sA));
        assertTrue("Got the first one wrong", 
                StringUtil.getSpacedPuntuation(sA).equals("this is a test ."));
        
        String sB = ".this is a test.";
        System.out.println(StringUtil.getSpacedPuntuation(sB));
        assertTrue("Got the second one wrong", 
                StringUtil.getSpacedPuntuation(sB).equals(". this is a test ."));
        
        String sC = ".. is a test.";
        System.out.println(StringUtil.getSpacedPuntuation(sC));
        assertTrue("Got the third one wrong", 
                StringUtil.getSpacedPuntuation(sC).equals(". . is a test ."));
        
        String sD = ".this i.s a test.";
        System.out.println(StringUtil.getSpacedPuntuation(sD));
        assertTrue("Got the fourth one wrong", 
                StringUtil.getSpacedPuntuation(sD).equals(". this i . s a test ."));

        String sE = ".this .is a test.";
        System.out.println(StringUtil.getSpacedPuntuation(sE));
        assertTrue("Got the fifth one wrong", 
                StringUtil.getSpacedPuntuation(sE).equals(". this . is a test ."));

        String sF = ".this .is. a test.";
        System.out.println(StringUtil.getSpacedPuntuation(sF));
        assertTrue("Got the sixth one wrong", 
                StringUtil.getSpacedPuntuation(sF).equals(". this . is . a test ."));
        
        String sG = ".this ..is a test.";
        System.out.println(StringUtil.getSpacedPuntuation(sG));
        assertTrue("Got the seventh one wrong", 
                StringUtil.getSpacedPuntuation(sG).equals(". this . . is a test ."));

        String sH = ".this .is.. a test.";
        System.out.println(StringUtil.getSpacedPuntuation(sH));
        assertTrue("Got the eigth one wrong", 
                StringUtil.getSpacedPuntuation(sH).equals(". this . is . . a test ."));

        String sI = ".this      .is.. a test.";
        System.out.println(StringUtil.getSpacedPuntuation(sI));
        assertTrue("Got the nineth one wrong", 
                StringUtil.getSpacedPuntuation(sI).equals(". this . is . . a test ."));
        
        String sJ = ",this      ,is,, a test,";
        System.out.println(StringUtil.getSpacedPuntuation(sJ));
        assertTrue("Got the tenth one wrong", 
                StringUtil.getSpacedPuntuation(sJ).equals(", this , is , , a test ,"));

        String sK = "This is some test text.";
        System.out.println(StringUtil.getSpacedPuntuation(sK));
        assertTrue("Got the tenth one wrong", 
                StringUtil.getSpacedPuntuation(sK).equals("This is some test text ."));


    }
    
    public void testIsPuntuation(){
                
        char c = '.';
        assertTrue("Got the '.' one wrong", 
                StringUtil.isPuntuation(c) == true);

        c = 'A';
        assertFalse("Got the 'A' one wrong", 
                StringUtil.isPuntuation(c) == true);

        c = ',';
        assertTrue("Got the ',' one wrong", 
                StringUtil.isPuntuation(c) == true);

        c = 'c';
        assertTrue("Got the 'c' one wrong", 
                StringUtil.isPuntuation(c) == false);

        c = '?';
        assertTrue("Got the '?' one wrong", 
                StringUtil.isPuntuation(c) == true);
    }
    
    public void testMySplit(){
        
        assertTrue("Couldn't split properly", 
                StringUtil.mySplit("hello \nbye", "\n").length > 1);
        System.out.println("Answer is " + Arrays.toString(StringUtil.mySplit("hello \nbye", "\n")));
    }
        
    public void testRemoveTrailingSpaces(){
        
        String input = "hello there          ";
        String result = StringUtil.removeTrailingSpaces(input);
        assertTrue("Couldn't remove the spaces properly, output is >" + result + "<", 
                    result.equals("hello there")); 
        input = "  hello there  ";
        result = StringUtil.removeTrailingSpaces(input);
        assertTrue("Couldn't remove the spaces properly, output is >" + result + "<", 
                result.equals("  hello there")); 
    
    }
    
    public void testGetEscaped(){
        
        String input = "Hello & there";
        String result = StringUtil.getEscapedText(input);
        
        assertTrue("Didn't escape the ampersand properly", "Hello &amp; there".equals(result));
        
        input = "Hello \"there\"";
        result = StringUtil.getEscapedText(input);
        
        assertTrue("Didn't escape the double quote properly", "Hello &quot;there&quot;".equals(result));
        
    }
    
    public void testGetEscapedTextAgain(){
        
        String input = "Need to copy the node positions";
        String result = StringUtil.getEscapedText(input);
        
        assertTrue("Didn't run on the given sentence", input.equals(result));
                
    }

}

