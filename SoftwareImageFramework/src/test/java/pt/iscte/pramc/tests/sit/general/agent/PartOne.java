/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.tests.sit.general.agent;

import pt.iscte.pramc.sit.annotations.VisibleAgentPart;
import pt.iscte.pramc.sit.annotations.VisibleSensor;
import pt.iscte.pramc.tests.sit.general.agent.actuators.AbstractActuator;
import pt.iscte.pramc.tests.sit.general.agent.actuators.Motion;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * Simple implementation of an agent part
 * 
 * This part has:
 * - two sensors, implemented in the part's main class
 * - two actuators, defined as an array attribute
 *
 * @version 0.1
 * @since Jun 16, 2011
 */
@VisibleAgentPart
public class PartOne implements Part{

	//the actuator array
	private final Object[] actuators;
	
	//the sensor one data holder
	private String data;
	
	//the sensor two data holder
	private Long heapsize;
	
	public PartOne(){
		//initialize actuators
		actuators = new Object[]{
				new AbstractActuator(),
				new Motion(this)
		};
	}
	
	@VisibleSensor(descriptor="obtainData.part1",fieldName="data")
	public void updateSensorOne(){
		data = this.toString();
	}

	@VisibleSensor(descriptor="heap.totalMemory",fieldName="heapsize")
	public void updateSensorTwo(){
		heapsize = Runtime.getRuntime().totalMemory();
	}
	
	@Override
	public boolean decideBehaviour() {
		// TODO Auto-generated method stub
		((Motion)actuators[1]).moveDown();
		((AbstractActuator)actuators[0]).act1(true, 10);
		return true;
	}

	/**
	 * Update sensor data
	 * @see pt.iscte.pramc.tests.sit.simpleAgent.expert.parts.Part#gatherSensorData()
	 */
	@Override
	public boolean gatherSensorData() {
		updateSensorTwo();
		updateSensorOne();
		return true;
	}
	
	/**
	 * @return the actual sensor data
	 */
	public String getData() {
		return data;
	}
}
