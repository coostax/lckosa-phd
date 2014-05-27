/**
 * 
 */
package pt.iscte.pramc.sit.exceptions;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * 
 *         Exception thrown when trying to instantiate agent parts on an agent
 *         that already has agent parts
 * 
 */
public class AgentAlreadyHasSoftwareImageException extends Exception {

    private final String agentID;

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public AgentAlreadyHasSoftwareImageException(String agentUUID) {
	this.agentID = agentUUID;
    }

    @Override
    public String toString() {
	return "Error: agent(" + agentID + ") already has a software image";
    }

}
