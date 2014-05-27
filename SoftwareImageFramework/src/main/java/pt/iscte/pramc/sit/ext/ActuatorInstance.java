/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.ext;

import java.io.Serializable;

import pt.iscte.pramc.sit.swi.si.Actuator;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Interface representation of the actuator instance in the agent
 * 
 *         Agent actuators (or agent parts if actions are described directly in
 *         the agent part) must implement this interface
 * 
 *         Connects the dynamic image with the agent actuator
 * 
 * @version 2.0
 * @since Jul 5, 2011
 */
public interface ActuatorInstance extends Serializable {

    /**
     * Associates a software image actuator representation to this actuator
     * instance
     * 
     * @param act
     */
    public void setActuatorRep(Actuator act);

    /**
     * @return the software image actuator representation for this actuator
     *         instance
     */
    public Actuator getActuatorRep();
}
