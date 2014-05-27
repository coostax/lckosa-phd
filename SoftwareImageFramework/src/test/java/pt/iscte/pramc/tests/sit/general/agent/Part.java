/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.tests.sit.general.agent;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * Simple interface representation of an agent part
 * 
 *
 * @version 0.1
 * @since Jun 16, 2011
 */
public interface Part {

	public boolean gatherSensorData();
	
	public boolean decideBehaviour();
}
