/**
 * 
 */
package pt.iscte.pramc.sit.ext.remote;

import java.rmi.RemoteException;

import pt.iscte.pramc.sit.agent.VisualSoftwareAgent;
import pt.iscte.pramc.sit.swi.SoftwareImage;

/**
 * Server implementation of the RMI bridge for the visual software agent.
 * 
 * Provides the remote access for the visual software agent's software image
 * through the show() method
 * 
 * This server is initialized by the SwiRemoteAccess aspect.
 * 
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 * @version 0.1
 * @since Mar 8, 2011
 */
public class VisualSoftwareAgentRMIServer implements RMIVisualSoftwareAgent {

    private final VisualSoftwareAgent agent;

    public VisualSoftwareAgentRMIServer(VisualSoftwareAgent agent) {
	this.agent = agent;
    }

    /**
     * @see pt.iscte.pramc.sit.remote.rmi.RMIVisualSoftwareAgent#getSoftwareImage()
     */
    @Override
    public SoftwareImage getSoftwareImage() throws RemoteException {
	return agent.getSoftwareImage();
    }

    @Override
    public String getAgentUUID() throws RemoteException {
	return agent.getSoftwareImage().getAgentUUID();
    }

}
