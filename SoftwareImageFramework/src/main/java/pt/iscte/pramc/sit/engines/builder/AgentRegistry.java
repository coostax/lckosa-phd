/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.engines.builder;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.pramc.sit.agent.VisualSoftwareAgent;
import pt.iscte.pramc.sit.engines.perception.AgentPerception;
import pt.iscte.pramc.sit.swi.SoftwareImage;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Holds all agents that participate in a program.
 * 
 *         The AgentRegistry is a singleton class, meaning that while the
 *         program is running all registred agents are acessible through this
 *         instance
 * 
 *         This allows a total independence between the agent and its software
 *         image. Access to the agent's software image is made through this
 *         instance
 * 
 *         //TODO: allow remote access to other agent registry instances
 * 
 * @version 2.0
 * @since Jun 16, 2011
 */
public final class AgentRegistry {

    private final List<VisualSoftwareAgent> agents;

    private final static AgentRegistry singleton = new AgentRegistry();

    /**
     * Default private constructor
     */
    private AgentRegistry() {
	this.agents = new ArrayList<VisualSoftwareAgent>();
    }

    /**
     * Provides access to this singleton instance
     */
    public static AgentRegistry getAgentRegistry() {
	return singleton;
    }

    /**
     * Adds a new Agent image to this registry
     * 
     * @param agentPair
     *            the new agent to register
     * @return false if pair already exists, true if register is successful
     */
    public boolean registerAgent(VisualSoftwareAgent agent) {
	if (agents.contains(agent)) {
	    System.err.println("ERROR: the agent with ID "
		    + agent.getSoftwareImage().getAgentUUID()
		    + " is already registered");
	    return false;
	}
	agents.add(agent);
	return true;
    }

    /**
     * 
     * @param agent
     *            the agent to find the software image
     * @return the software image associated to this agent, null if agent is not
     *         registred
     */
    public SoftwareImage getAgentSoftwareImage(VisualSoftwareAgent agent) {
	int index = agents.indexOf(agent);
	if (index != -1) {
	    return agents.get(index).getSoftwareImage();
	}
	System.err.println("Agent " + agent.toString() + " is not registered");
	return null;
    }

    /**
     * 
     * @param agent
     * @return the perception of the agent
     */
    public AgentPerception getAgentPerception(VisualSoftwareAgent agent) {
	int index = agents.indexOf(agent);
	if (index != -1) {
	    return agents.get(index).getAgentPerception();
	}
	System.err.println("Agent " + agent.toString() + " is not registered");
	return null;
    }

    public boolean removeAgent(VisualSoftwareAgent agent) {
	int index = agents.indexOf(agent);
	if (index != -1) {
	    try {
		agents.remove(index);
		return true;
	    } catch (Exception e) {
		return false;
	    }
	}
	return false;
    }

    /**
     * Stores the provided sensor data in the agent's perception that implements
     * the provided sensor instance
     * 
     * @param data
     *            the data provided by the sensor
     * @param sensorInst
     *            the sensor implementation instance
     * @return true if correctly stored, false when error
     */
    // public boolean storeSensorDataFromSensorInstance(Object data,
    // Object sensorInst) {
    // Condition cond = null;
    // // locate the correct agent from the registry
    // for (VisualSoftwareAgent vsa : agents) {
    // cond = vsa.getAgentPerception().getConditionFromSensorInstance(
    // sensorInst);
    // if (cond != null) { // found the correct condition
    // break;
    // }
    // }
    // if (cond != null) {
    // // store data in the condition object
    // cond.setData(data);
    // return true;
    // } else {
    // System.err.println("ERROR: sensor instance "
    // + sensorInst.getClass().getCanonicalName()
    // + " is not registered in any perception");
    // return false;
    // }
    // }

    public synchronized List<VisualSoftwareAgent> retrieveVisualSoftwareAgents() {
	return this.agents;
    }
}
