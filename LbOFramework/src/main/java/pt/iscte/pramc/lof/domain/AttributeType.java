/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.domain;


/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * Distinguishes the different types of attributes
 * 
 * 
 *  Two types of attributes are used by the learning algorithm
 * 
 *  
 *  - The Behaviour type stores lists of ActionInstances or Actions (depending on the preferences) that represent the agent's behaviour
 *  
 *  
 *  - The Condition type stores lists of objects that represent the conditions that force agent behaviours
 *
 * @version 0.1
 * @since Nov 16, 2011
 */
public enum AttributeType {	
	BEHAVIOUR,
	CONDITION
}
