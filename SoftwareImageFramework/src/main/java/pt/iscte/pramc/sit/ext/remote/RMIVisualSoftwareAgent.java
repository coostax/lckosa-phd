/**
 * 
 */
package pt.iscte.pramc.sit.ext.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import pt.iscte.pramc.sit.swi.SoftwareImage;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         RMI remote interface for the visual software agent. Provides RMI
 *         access to the show() method
 * 
 * @version 0.1
 * @since Mar 8, 2011
 */
public interface RMIVisualSoftwareAgent extends Remote {

    /**
     * @return the agent's software image
     * @throws RemoteException
     */
    public SoftwareImage getSoftwareImage() throws RemoteException;

    /**
     * @return the agent's unique identifier
     * @throws RemoteException
     */
    public String getAgentUUID() throws RemoteException;

}
