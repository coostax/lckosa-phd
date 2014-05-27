/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.engines.builder;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import pt.iscte.pramc.sit.agent.VisualSoftwareAgent;
import pt.iscte.pramc.sit.annotations.AgentLoop;
import pt.iscte.pramc.sit.annotations.VisibleAgent;
import pt.iscte.pramc.sit.engines.perception.AgentPerception;
import pt.iscte.pramc.sit.swi.SoftwareImage;
import pt.iscte.pramc.tools.TimeMeasurement;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * This aspect focuses on the first stage of the software image toolkit, creating the software image and associating an agent to it
 * 
 * This aspect listens to invocations of objects annotated with VisibleAgent. 
 * 
 * Agent objects must identify their loop method with the AgentLoop tag, if not this aspect warns the developer.  
 *
 * Agent objects implicitly implement the SwiBuildMechanism interface. This interface has no methods, it is used to differentiate the agent class from other instances
 *
 * @version 2.5 added action association
 * @since Jun 16, 2011
 */
public aspect SwIBuildEngine {
	
	static Logger logger = Logger.getLogger(SwIBuildEngine.class);
	
	/**
	 * holds the time taken to build the agent's software image
	 */
	private long VisualSoftwareAgent.ttb;
	
	/**
	 * holds the agent's software image
	 */
	private final SoftwareImage VisualSoftwareAgent.softwareImage = new SoftwareImage(this);
	
	/**
	 * holds the agent's perception
	 */
	private final AgentPerception VisualSoftwareAgent.perception = new AgentPerception();
	
	/**
	 * holds the association between the agent actions and its representation in the software image 
	 */
	private final AgentActions VisualSoftwareAgent.actions = new AgentActions();
	
	/**
	 * All elements annotated with VisibleAgent must implement the
	 * SwiBuildMechanism interface
	 */
	declare parents: (@VisibleAgent *) implements VisualSoftwareAgent;

	//default implementations
	
	/**
	 * Default implementation for the getSoftwareImage method in VisualSoftwareAgent interface 
	 * @return this agent's software image
	 */
	public SoftwareImage VisualSoftwareAgent.getSoftwareImage(){
		return softwareImage;
	}
	
	/**
	 * Default implementation of the getTtb method in VisualSoftwareAgent interface
	 * @return the time it took to build the agent's software image in milliseconds
	 */
	public long VisualSoftwareAgent.getTtb(){
		return ttb;
	}
	
	/**
	 * Default implementation of the setTtb method in VisualSoftwareAgent interface
	 * @param time the time it takes to build the agent's software image in milliseconds
	 */
	public void VisualSoftwareAgent.setTtb(long time){
		this.ttb = time;
	}
	
	/**
	 * Default implementation of the getAgentPerception() method 
	 * @return this agent's perception
	 */
	public AgentPerception VisualSoftwareAgent.getAgentPerception(){
		return perception;
	}
	
	/**
	 * Default implementation of the getAgentActions() method
	 * @return the association between the agent's actions and its representation
	 */
	public AgentActions VisualSoftwareAgent.getAgentActions(){
		return actions;
	}
	
	/**
	 * @return this agent's unique identifier stamped in the the software image
	 */
	public String VisualSoftwareAgent.getAgentUUID(){
		return getSoftwareImage().getAgentUUID();
	}
	
	/**
	 * Removes this agent from the agent registry
	 */
	public void VisualSoftwareAgent.shutdown(){
		logger.info("finalizing agent " + this);
		AgentRegistry.getAgentRegistry().removeAgent(this);
	}
	
	/**
	 * Declaration of a new agent
	 * @param obj
	 */
	pointcut newAgent(Object obj) : @target(VisibleAgent) && target(obj) && execution((@VisibleAgent *).new(..));
	
	/**
	 * Before the agent is created verify if loop method is annotated, emmit warning if not
	 * @param agent
	 */
	before(Object agent):newAgent(agent){
		int counter = 0;
		for(Method method : agent.getClass().getDeclaredMethods()){
			if(method.isAnnotationPresent(AgentLoop.class)){
				counter++;
			}
		}
		if(counter == 0){
			logger.error("loop method not annotated in agent main class: " + agent.getClass().getCanonicalName());
		}else if(counter > 1){
			logger.warn("more than one loop method ("+counter+") is annotated in agent main class: " + agent.getClass().getCanonicalName());
		}
	}
	
	/**
	 * After the agent is initialized:
	 * - create a new software image
	 * - associate agent to the software image
	 * @param agent
	 */
	after(Object agent):newAgent(agent){
		//create new software image
		if(agent instanceof VisualSoftwareAgent){
			//use the tools to build the software image
			long time = TimeMeasurement.getCpuTime();
			BuilderTools tools = new BuilderTools((VisualSoftwareAgent)agent);
			tools.buildSoftwareImage();
			//register agent in the registry
			AgentRegistry.getAgentRegistry().registerAgent((VisualSoftwareAgent)agent);
			((VisualSoftwareAgent)agent).setTtb( TimeMeasurement.getCpuTime() - time);
		}else{
			logger.error("agent " + agent.toString() + " is not a visual software agent");
		}
		
	}
	
	
}
