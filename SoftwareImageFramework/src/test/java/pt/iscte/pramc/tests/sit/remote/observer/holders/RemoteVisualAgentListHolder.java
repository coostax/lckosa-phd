/**
 * 
 */
package pt.iscte.pramc.tests.sit.remote.observer.holders;

import pt.iscte.pramc.sit.ext.remote.RMIVisualSoftwareAgent;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         A holder for the visual software agent
 * 
 * @version 0.1
 * @since Mar 15, 2011
 */
public class RemoteVisualAgentListHolder {

	private final RMIVisualSoftwareAgent agent;
	private final String agentName;

	public RemoteVisualAgentListHolder(RMIVisualSoftwareAgent agent, String name) {
		super();
		this.agent = agent;
		this.agentName = name;
	}

	@Override
	public String toString() {
		return agentName;
	}

	public RMIVisualSoftwareAgent getAgent() {
		return agent;
	}

}
