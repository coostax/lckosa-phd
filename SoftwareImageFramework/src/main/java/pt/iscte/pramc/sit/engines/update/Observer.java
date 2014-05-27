/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.engines.update;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Observer interface to implement the observer design pattern in the
 *         relation between experts and apprentices.
 * 
 *         Each time the expert updates its dynamic image the expert is notified
 * 
 * @version 0.1
 * @since Oct 19, 2011
 */
public interface Observer {

    public void notifyDIUpdate();

}
