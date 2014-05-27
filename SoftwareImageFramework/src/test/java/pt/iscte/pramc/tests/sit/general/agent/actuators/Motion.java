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
 * A trial implementation of a simple actuator
 * Provides agents with four actions
 *
 * @version 0.1
 * @since Jun 16, 2011
 */
@VisibleActuator
public class Motion {
	
	private final Object toMove;
	
	public Motion(Object toMove){
		this.toMove = toMove;
	}
	
	@VisibleAction(descriptor="object.move.UP")
	public void moveUp(){
		System.out.println("UP");
	}
	
	@VisibleAction(descriptor="object.move.DOWN")
	public void moveDown(){
		System.out.println("DOWN");
	}
	
	@VisibleAction(descriptor="object.move.LEFT")
	public void moveLeft(){
		System.out.println("LEFT");
	}
	
	@VisibleAction(descriptor="object.move.RIGHT")
	public void moveRight(){
		System.out.println("RIGHT");
	}
	
}
