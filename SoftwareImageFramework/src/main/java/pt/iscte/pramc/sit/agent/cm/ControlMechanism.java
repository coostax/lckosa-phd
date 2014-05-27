/**
 * Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.agent.cm;

import org.apache.log4j.Logger;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * 
 *         Interface representation of a control mechanism.
 * 
 *         Control mechanisms are usually implemented by agent parts or
 *         sub-parts
 * 
 *         The control mechanism has a single method that makes a call for
 *         decision taking
 * 
 * @since 20 de Fev de 2012
 * @version 0.1
 */
public interface ControlMechanism {

    public final Logger logger = Logger.getLogger(ControlMechanism.class);

    /**
     * Initializes this control mechanism. Provides initial data to visual
     * attributes when needed
     */
    public void init();

    /**
     * Makes a call to the control mechanism for decision taking
     */
    public void callControlMechanism();

}
