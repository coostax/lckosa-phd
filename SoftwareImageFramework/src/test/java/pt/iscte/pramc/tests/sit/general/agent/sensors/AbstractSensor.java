/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.tests.sit.general.agent.sensors;

import pt.iscte.pramc.sit.annotations.VisibleSensor;
import pt.iscte.pramc.sit.annotations.VisibleSensorImpl;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * Another implementation of a sensor
 *
 * @version 0.1
 * @since Jun 16, 2011
 */
@VisibleSensorImpl
public class AbstractSensor {

	private final int decider;
	
	//sensor data
	double value;
	
	public AbstractSensor(int decider) {
		this.decider = decider;
	}
	
	@VisibleSensor(descriptor="random.by.decider",fieldName="value")
	public void update(){
		value = Math.random() * decider;
	}
}

