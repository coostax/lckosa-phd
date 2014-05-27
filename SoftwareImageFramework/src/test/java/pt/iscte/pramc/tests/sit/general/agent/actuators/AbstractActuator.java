/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.tests.sit.general.agent.actuators;

import pt.iscte.pramc.sit.annotations.VisibleAction;
import pt.iscte.pramc.sit.annotations.VisibleActuator;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * Another trial implementation of an actuator.
 * This one provides two actions
 *
 * @version 0.1
 * @since Jun 16, 2011
 */
@VisibleActuator
public class AbstractActuator {

	public AbstractActuator() {
	}
	
	@VisibleAction(descriptor="do.with.2.paramenters")
	public void act1(boolean action, int val){
		System.out.println("doing? " + action + " with val " + val);
	}
	
	@VisibleAction(descriptor="do.with.1.paramenter")
	public void act1(String word){
		System.out.println("out is " + word);
	}
}