/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.tests.sit.general.agent;

import pt.iscte.pramc.sit.annotations.VisibleAction;
import pt.iscte.pramc.sit.annotations.VisibleAgentPart;
import pt.iscte.pramc.sit.annotations.VisibleAttribute;
import pt.iscte.pramc.tests.sit.general.agent.sensors.AbstractSensor;
import pt.iscte.pramc.tests.sit.general.agent.sensors.SimpleTimeSensor;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * Another simple representation of an agent part
 * 
 * This part has
 * - Three sensors, one defined in a class attribute, the other two defined inside an array
 * - One actuator with a single action, defined as a method in the part
 * - One visual attribute 
 *
 * @version 0.1
 * @since Jun 16, 2011
 */
@VisibleAgentPart
public class PartTwo implements Part{

	//the visual attribute
	@VisibleAttribute(descriptor="emotion.string")
	private String emotion;
	
	//the sensor attributes
	private final SimpleTimeSensor timeSensor;
	private final AbstractSensor[] sensors;
	
	public PartTwo(){
		this.emotion = "normal";
		this.timeSensor = new SimpleTimeSensor();
		this.sensors = new AbstractSensor[] {
				new AbstractSensor(3),
				new AbstractSensor(10)
		};
	}
	
	/**
	 * action method
	 * @param todo
	 */
	@VisibleAction(descriptor="action.do")
	public void doSomething(String todo){
		System.out.println("called doSomething with "+todo);
	}
	
	/**
	 * @return this agent's time sensor instance
	 */
	public SimpleTimeSensor getTimeSensor() {
		return timeSensor;
	}

	@Override
	public boolean decideBehaviour() {
		doSomething(timeSensor.getData());
		return true;
	}

	@Override
	public boolean gatherSensorData() {
		updateEmotion();
		timeSensor.gatherTimeAsString();
		for(AbstractSensor as : sensors){
			as.update();
		}
		return true;
	}

	private void updateEmotion() {
		this.emotion = "new emotion";	
	}
	
}