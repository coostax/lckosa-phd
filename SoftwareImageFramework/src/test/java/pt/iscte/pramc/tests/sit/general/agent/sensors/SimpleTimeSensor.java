/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.tests.sit.general.agent.sensors;

import java.util.Date;

import pt.iscte.pramc.sit.annotations.VisibleSensor;
import pt.iscte.pramc.sit.annotations.VisibleSensorImpl;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * A simple implementation of a sensor
 *
 * @version 0.1
 * @since Jun 16, 2011
 */
@VisibleSensorImpl
public class SimpleTimeSensor {

	//the sensor data holder
	private String data;
	
	public SimpleTimeSensor(){
	}
	
	@VisibleSensor(descriptor="time.as.string",fieldName="data")
	public void gatherTimeAsString(){
		data = new Date(System.currentTimeMillis()).toString();
	}
	
	public String getData() {
		return data;
	}
}
