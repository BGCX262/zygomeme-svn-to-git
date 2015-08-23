package com.zygomeme.york.dynamicmodels;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

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
 * Test for the Dynamic Model.
 *
 */
public class DynamicModelTest extends TestCase {
	
	public void testSimpleModel(){

		DynamicModelNode node1 = new DynamicModelNode("one");
		node1.setInitialValue(5);

		DynamicModelNode node2 = new DynamicModelNode("two");
		node2.addInput(node1);
		node2.setExpression("(one * 5) + 3");

		DynamicModelNode node3 = new DynamicModelNode("three");
		node3.addInput(node1);
		node3.setExpression("(one * -10) + 75");;

		DynamicModel model = new DynamicModel("Simple_Model");
		model.addNode(node1);
		model.addNode(node2);
		model.addNode(node3);

		// Parameters - start node(s) and end time
		model.run("one", 2);

		assertTrue("Node Two has incorrect value. Is " + model.getNode("two").getOutputValue() + " should be 28", 
				model.getNode("two").getOutputValue() == 28);

		assertTrue("Node Three has incorrect value. Is " + model.getNode("three").getOutputValue() + " should be 25", 
				model.getNode("three").getOutputValue() == 25);
	}

	public void testSimpleTwoInputModel(){

		DynamicModelNode node1 = new DynamicModelNode("one");
		node1.setInitialValue(5);

		DynamicModelNode node2 = new DynamicModelNode("two");
		node2.setInitialValue(10);

		DynamicModelNode node3 = new DynamicModelNode("three");
		node3.addInput(node1);
		node3.addInput(node2);
		node3.setExpression("((one * 5) + 3) + ((two * -10) + 175)");

		DynamicModel model = new DynamicModel("TwoInputModel");
		model.addNode(node1);
		model.addNode(node2);
		model.addNode(node3);

		List<String> startNodes = new ArrayList<String>();
		startNodes.add("one");
		startNodes.add("two");
		model.setStartNodes(startNodes);
		model.setEndTime(2);
		model.run();

		assertTrue("Node Three has incorrect value. Is " + model.getNode("three").getOutputValue() + " should be 103", 
				model.getNode("three").getOutputValue() == 103);
	}

	public void testFourNodeModelWithStaggeredArcs(){

		DynamicModelNode node1 = new DynamicModelNode("one");
		node1.setInitialValue(5);

		DynamicModelNode node2 = new DynamicModelNode("two");
		node2.addInput(node1);
		node2.setExpression("(one * 5) + 3");

		DynamicModelNode node3 = new DynamicModelNode("three");
		node3.addInput(node1);
		node3.setExpression("(one * -10) + 75");

		DynamicModelNode node4 = new DynamicModelNode("four");
		node4.addInput(node1);
		node4.addInput(node2);
		node4.addInput(node3);
		node4.setExpression("(((one * 21) + 3) + ((two * 2) - 10)) + ((three * 3) - 15)");
		
		DynamicModel model = new DynamicModel("Four nodes with staggered arcs");
		model.addNode(node1);
		model.addNode(node2);
		model.addNode(node3);
		model.addNode(node4);

		List<String> startNodes = new ArrayList<String>();
		startNodes.add("one");
		model.setStartNodes(startNodes);
		model.setEndTime(2);
		model.run();

		assertTrue("Node Three has incorrect value. Is " + model.getNode("four").getOutputValue() + " should be 103", 
				model.getNode("four").getOutputValue() == 214);
	}

	public void testModelHistory(){

		DynamicModelNode node1 = new DynamicModelNode("one");
		node1.setInitialValue(5);

		DynamicModelNode node2 = new DynamicModelNode("two");
		node2.addInput(node1);
		node2.setExpression("(one * 5) + 5");
		
		DynamicModelNode node3 = new DynamicModelNode("three");
		node3.addInput(node2);		
		node3.setExpression("(two * 2) + 10");
		node3.addModifier(node2, new LinearPropagationModifier());
		
		DynamicModel model = new DynamicModel("Test history model");
		model.addNode(node1);
		model.addNode(node2);
		model.addNode(node3);

		List<String> startNodes = new ArrayList<String>();
		startNodes.add("one");
		model.setStartNodes(startNodes);
		model.setEndTime(15);
		model.run();

		System.out.println("History:\n" + model.getDefaultHistory());
		
		assertTrue("Node Three has incorrect value at t+6. Is " + model.getDefaultHistory().getSnapshot(6).get("three").getOutputValue() + " should be 46", 
				model.getDefaultHistory().getSnapshot(6).get("three").getOutputValue() == 46);
				
		assertTrue("Node Three has incorrect value at t+12. Is " + model.getDefaultHistory().getSnapshot(12).get("three").getOutputValue() + " should be 70", 
				model.getDefaultHistory().getSnapshot(12).get("three").getOutputValue() == 70);
	}

	public void testRepeatedModifierCalls(){

		DynamicModelNode node1 = new DynamicModelNode("one");
		node1.setInitialValue(70);

		DynamicModelNode node2 = new DynamicModelNode("two");
		node2.addInput(node1);
		node2.setExpression("one");
		node2.addModifier(node1, new LinearPropagationModifier());
				
		DynamicModel model = new DynamicModel("Test Repeated Modifier Calls");
		model.addNode(node1);
		model.addNode(node2);

		List<String> startNodes = new ArrayList<String>();
		startNodes.add("one");
		model.setStartNodes(startNodes);
		model.setEndTime(15);
		model.run();

		System.out.println("History:\n" + model.getDefaultHistory());
		
		assertTrue("Node Two has incorrect value at t+6. Is " + model.getDefaultHistory().getSnapshot(6).get("two").getOutputValue() + " should be 42", 
				model.getDefaultHistory().getSnapshot(6).get("two").getOutputValue() == 42);
				
		assertTrue("Node Two has incorrect value at t+12. Is " + model.getDefaultHistory().getSnapshot(12).get("two").getOutputValue() + " should be 70", 
				model.getDefaultHistory().getSnapshot(12).get("two").getOutputValue() == 70);
		
		// Run the model again and it should have the same outputs
		model.run();

		System.out.println("History:\n" + model.getDefaultHistory());
		
		assertTrue("Node Two has incorrect value at t+6. Is " + model.getDefaultHistory().getSnapshot(6).get("two").getOutputValue() + " should be 42", 
				model.getDefaultHistory().getSnapshot(6).get("two").getOutputValue() == 42);
				
		assertTrue("Node Two has incorrect value at t+12. Is " + model.getDefaultHistory().getSnapshot(12).get("two").getOutputValue() + " should be 70", 
				model.getDefaultHistory().getSnapshot(12).get("two").getOutputValue() == 70);

		
	}

	public void testSimpleLoop(){

		DynamicModelNode node1 = new DynamicModelNode("one");
		node1.setInitialValue(5);

		DynamicModelNode node2 = new DynamicModelNode("two");
		node2.addInput(node1);
		node2.setExpression("(one * 5) + 5");
		node1.addInput(node2); // Loop back
		node1.setExpression("(two * 5) + 5");
		
		DynamicModel model = new DynamicModel("Simple loop model");
		model.addNode(node1);
		model.addNode(node2);

		List<String> startNodes = new ArrayList<String>();
		startNodes.add("one");
		model.setStartNodes(startNodes);
		model.setEndTime(10);
		model.run();

		System.out.println("History:\n" + model.getDefaultHistory());
		
		assertTrue("Node One has incorrect value at t+3. Is " + model.getDefaultHistory().getSnapshot(3).get("one").getOutputValue() + " should be 46", 
				model.getDefaultHistory().getSnapshot(3).get("one").getOutputValue() == 3905);
	}

	public void testMultiRuns(){

		DynamicModelNode node1 = new DynamicModelNode("n1");
		node1.setInitialValue(1);

		node1.addInput(node1); 
		node1.setExpression("n1 + 1.0");
		
		DynamicModel model = new DynamicModel("Test multi-runs");
		model.addNode(node1);

		List<String> startNodes = new ArrayList<String>();
		startNodes.add("n1");
		model.setStartNodes(startNodes);
		model.setEndTime(10);
		model.run();

		System.out.println("History:\n" + model.getDefaultHistory());
		
		assertTrue("Node One has incorrect value at t+3. Is " + model.getDefaultHistory().getSnapshot(3).get("n1").getOutputValue() + " should be 46", 
				model.getDefaultHistory().getSnapshot(3).get("n1").getOutputValue() == 4);

		model.run();

		// Should not be any different if the model is run again
		System.out.println("History:\n" + model.getDefaultHistory());
		
		assertTrue("Node One has incorrect value at t+3. Is " + model.getDefaultHistory().getSnapshot(3).get("n1").getOutputValue() + " should be 46", 
				model.getDefaultHistory().getSnapshot(3).get("n1").getOutputValue() == 4);

	}
	
	public void testSimpleBalancedLoop(){

		DynamicModelNode node1 = new DynamicModelNode("House_Price");
		node1.setInitialValue(5000);

		DynamicModelNode node2 = new DynamicModelNode("Demand");
		//node2.setInitialValue(300);
		node2.addInput(node1);
		node2.setExpression("(House_Price * -0.3) + 5000");
		node2.addModifier(node1, new LinearPropagationModifier());

		node1.addInput(node2); // Loop back
		node1.setExpression("Demand * 0.4");
		node1.addModifier(node2, new LinearPropagationModifier());
	
		DynamicModel model = new DynamicModel("Simple balanced loop model");
		model.addNode(node1);
		model.addNode(node2);

		List<String> startNodes = new ArrayList<String>();
		startNodes.add("House_Price");
		model.setStartNodes(startNodes);
		model.setEndTime(100);
		model.run();

		System.out.println("SimpleBalancedLoop History:\n" + model.getDefaultHistory());
		
		assertTrue("House Price Node has incorrect value at t+3. Is " + model.getDefaultHistory().getSnapshot(3).get("House_Price").getOutputValue(), 
				Math.abs(model.getDefaultHistory().getSnapshot(3).get("House_Price").getOutputValue() - 379.76) < 0.00005);
		/*		
		assertTrue("Node Three has incorrect value at t+12. Is " + model.getHistory().getSnapshot(12).get("three").getOutputValue() + " should be 70", 
				model.getHistory().getSnapshot(12).get("three").getOutputValue() == 70);
				*/
	}

}
