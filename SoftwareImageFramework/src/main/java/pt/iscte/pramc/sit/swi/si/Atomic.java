/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.swi.si;

import java.io.Serializable;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Represents an atomic element in the software image Since version 2.0
 *         atomic elements are:
 * 
 *         - Actions
 * 
 *         - Sensors
 * 
 *         - Visual Attributes
 * 
 *         All atomic elements have a string descriptor of the ontolgy class
 *         associated to the atomic element
 * 
 * @version 2.0
 * @since Jul 22, 2011
 */
public interface Atomic extends Serializable {

    public String getDescriptor();

}
