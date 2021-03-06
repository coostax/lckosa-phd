/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.tests.sit.general.agent;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.pramc.sit.annotations.AgentLoop;
import pt.iscte.pramc.sit.annotations.VisibleAgent;
import pt.iscte.pramc.sit.swi.SoftwareImage;
import pt.iscte.pramc.sit.swi.si.Action;
import pt.iscte.pramc.sit.swi.si.Actuator;
import pt.iscte.pramc.sit.swi.si.AgentPart;
import pt.iscte.pramc.sit.swi.si.Sensor;
import pt.iscte.pramc.sit.swi.si.StaticImage;
import pt.iscte.pramc.sit.swi.si.VisualAttribute;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * A simple agent to test the version 2.0 of the software image
 * 
 * The simple agent has two parts, instantiated as a List
 *
 * The first part
 * This part has:
 * - one sensor, implemented in the part's main class 
 * 		obtainData.part1
 * - two actuators, defined as an array attribute
 *   	Act1: do.with.2.paramenters , do.with.1.paramenter
 *   	Act2: object.move.UP , object.move.DOWN , object.move.LEFT , object.move.RIGHT
 * 
 * The second part
 * This part has:
 * - Three sensors, one defined in a class attribute, the other two defined inside an array
 * 		time.as.string , random.by.decider , random.by.decider
 * - One actuator with a single action, defined as a method in the part
 * 		Act1: action.do
 * - One visual attribute 
 * 		emotion.string
 *
 * @version 0.1
 * @since Jun 16, 2011
 */
@VisibleAgent
public class SimpleExpert {

private List<Part> agentParts;
	
	public SimpleExpert() {
		//instantiate the agent parts
		agentParts = new ArrayList<Part>(2);
		agentParts.add(new PartOne());
		agentParts.add(new PartTwo());
	}
	
	/**
	 * The loop method
	 * calls all parts's decision mechanism
	 */
	@AgentLoop
	public void run(){
		for(Part part : agentParts){
			part.gatherSensorData();
			part.decideBehaviour();
		}
	}
	
	/**
	 * @return the agent parts associated with this agent
	 */
	public List<Part> getAgentParts() {
		return agentParts;
	}
	
	/** 
	 * @return a software image that is similar (in a static image point of view) to the one generated by the agent
	 */
	public static SoftwareImage getSimilarSwI(){
		SoftwareImage swi = new SoftwareImage("SimilarSoftwareImage");
		StaticImage si = swi.getStaticImage();
		//first agent part
		AgentPart ap1 = new AgentPart("");
		ap1.addSensor(new Sensor("obtainData.part1","",null));
		ap1.addSensor(new Sensor("heap.totalMemory","",null));
		Actuator act1 = new Actuator("", null);
		act1.addAction(new Action("do.with.2.paramenters", ""));
		act1.addAction(new Action("do.with.1.paramenter", ""));
		Actuator act2 = new Actuator("",null);
		act2.addAction(new Action("object.move.UP",""));
		act2.addAction(new Action("object.move.DOWN",""));
		act2.addAction(new Action("object.move.LEFT",""));
		act2.addAction(new Action("object.move.RIGHT",""));
		ap1.addActuator(act1);
		ap1.addActuator(act2);
		//second agent part
		AgentPart ap2 = new AgentPart("");
		ap2.addSensor(new Sensor("time.as.string","",null));
		ap2.addSensor(new Sensor("random.by.decider","",null));
		ap2.addSensor(new Sensor("random.by.decider","",null));
		Actuator act1p2 = new Actuator("",null);
		act1p2.addAction(new Action("action.do",""));
		ap2.addActuator(act1p2);
		ap2.addVisualAttribute(new VisualAttribute("emotion.string","",null,null));
		//add parts to static image
		si.addAgentPart(ap1);
		si.addAgentPart(ap2);
		return swi;
	}

	public Object getPartOne() {
		return agentParts.get(0);
	}
	
}
