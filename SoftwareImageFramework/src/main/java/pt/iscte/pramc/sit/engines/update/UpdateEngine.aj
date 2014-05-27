/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.engines.update;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pt.iscte.pramc.sit.agent.VisualSoftwareAgent;
import pt.iscte.pramc.sit.annotations.AgentLoop;
import pt.iscte.pramc.sit.annotations.VisibleAction;
import pt.iscte.pramc.sit.annotations.VisibleActuator;
import pt.iscte.pramc.sit.annotations.VisibleAgentPart;
import pt.iscte.pramc.sit.exceptions.ObserverException;
import pt.iscte.pramc.sit.ext.ActuatorInstance;
import pt.iscte.pramc.sit.ext.NoAction;
import pt.iscte.pramc.sit.swi.di.ActionInstance;
import pt.iscte.pramc.sit.swi.di.Condition;
import pt.iscte.pramc.sit.swi.di.DynamicImage;
import pt.iscte.pramc.sit.swi.di.Snapshot;
import pt.iscte.pramc.sit.swi.si.APRoot;
import pt.iscte.pramc.sit.swi.si.Action;
import pt.iscte.pramc.sit.swi.si.Actuator;
import pt.iscte.pramc.sit.swi.si.AgentPart;
import pt.iscte.pramc.sit.swi.si.StaticImage;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * This aspect focuses on the second stage of the software image toolkit, updating the dynamic image
 * 
 * Dynamic image updates happen each time the agent main loop method is called. For this to happen, this method must be located at the main agent class (and annotated with VisibleAgent)
 * 
 * The dynamic image update reflects two stages:
 * - Provide the conditions - update perception and visual attributes (PerceptionEngine)
 * - Provide the actions - update the dynamic image with the set of actions performed by the agent (UpdateEngine)
 * 
 * The update engine focuses on the second stage.
 * It listens for calls to the agent loop method.
 * Agent behavior is captured between the beginning and the end of the agent loop
 * Before the loop is called the list of performed actions is reseted.
 * The loop is responsible for updating the agent's perception and calling the decision engines on each part. 
 * In their turn the decision engines call the action methods that represent the agent behaviour
 * On each action method call the list of performed actions is updated
 * After the loop is called the list of performed actions is combined with the current perception and the snapshot is created
 * 
 * 
 * @version 2.0
 * @since Jun 16, 2011
 */
public aspect UpdateEngine {

	//logger
	static Logger logger = Logger.getLogger(UpdateEngine.class);
	
	//default implementations
	
	declare parents: (@VisibleAgentPart *) implements ActuatorInstance;
	
	declare parents: (@VisibleActuator *) implements ActuatorInstance;

	//ActuatorInstance defaults

	private Actuator ActuatorInstance.actuatorRep;
	
	/**
	 * sets an actuator representation to this actuator instance
	 */
	public void ActuatorInstance.setActuatorRep(Actuator act){
		this.actuatorRep = act;
	}
	
	/**
	 * @return the software image actuator representation for this actuator instance
	 */
	public Actuator ActuatorInstance.getActuatorRep(){
		return this.actuatorRep;
	}

	//VisualSoftwareAgent extra methods
	/**
	 * Stores the action instances of an agent
	 */
	private List<ActionInstance> VisualSoftwareAgent.performedActions;
	
	/**
	 * Resets the performed actions list
	 * @return true if succeeded
	 */
	public boolean VisualSoftwareAgent.resetPerformedActions(){
		this.performedActions = new ArrayList<ActionInstance>();
		return true;
	}
	
	/**
	 * Adds an action to the performed actions list
	 * @param act the action to add
	 * @return true if success
	 */
	public boolean VisualSoftwareAgent.registerAction(ActionInstance act){
		return this.performedActions.add(act);
	}
	
	/**
	 * @return the list of actions performed by the agent
	 */
	public List<ActionInstance> VisualSoftwareAgent.getPerformedActions(){
		return this.performedActions;
	}
	
	/** 
	 * @return the current snapshot from the agent's dynamic image
	 */
	public Snapshot VisualSoftwareAgent.observeCurrent(){
		return this.getSoftwareImage().getDynamicImage().getCurrentSnapshot();
	}
	
	/**
	 * @return the historical record of snapshots from this agent's dynamic image
	 */
	public Snapshot[] VisualSoftwareAgent.observeHistory(){
		return this.getSoftwareImage().getDynamicImage().getHistoricalRecord();
	}
	
	//implementation of the observer pattern
	
	/**
	 * Stores the action instances of an agent
	 */
	private List<Observer> VisualSoftwareAgent.observers;
	
	public void VisualSoftwareAgent.attach(Observer observer) throws ObserverException{
		if(observers == null){
			observers = new ArrayList<Observer>();
		}
		if(observers.contains(observer)){
			throw new ObserverException("Observer already registered");
		}
		observers.add(observer);
	}
	
	public void VisualSoftwareAgent.detach(Observer observer) throws ObserverException{
		if(observers == null){
			throw new ObserverException("No observers registered");
		}
		if(!observers.remove(observer)){ 
			throw new ObserverException("The observer is not registered");
		}
	}
	
	public void VisualSoftwareAgent.recallObservers(){
		if(observers != null){
			for(Observer obs : observers){
				obs.notifyDIUpdate();
			}
		}
	}
	
	
	//end of observer pattern
	
	/**
	 * Execution of the AgentLoop method
	 */
	pointcut agentLoop(Object executor, AgentLoop tag) : execution ( @AgentLoop * * (..) ) && @annotation(tag) && this(executor);

	/**
	 * Execution of an action method
	 */
	pointcut agentAction(Object executor, VisibleAction tag) : execution ( @VisibleAction * * (..) ) && @annotation(tag) && this(executor);
	
	/**
	 * prepares the agent's list of executed actions
	 * @param executor
	 * @param tag 
	 */
	before(Object executor, AgentLoop tag):agentLoop(executor,tag){
		if(executor instanceof VisualSoftwareAgent){
			((VisualSoftwareAgent)executor).resetPerformedActions();
		}else{//the agent was not correcly identifyed
			logger.warn("Agent " + executor.getClass().getCanonicalName() + " is not annotated with @VisibleAgent");
		}
	}
	
	/**
	 * After the execution of an action store it in the list of executed actions, along with its parameters
	 * @param executor
	 * @param tag
	 */
	after(Object executor, VisibleAction tag):agentAction(executor, tag){
		if(executor instanceof ActuatorInstance){
			//get correspondent action from the executor's representation
			for(Action act : ((ActuatorInstance)executor).getActuatorRep().getActionSet()){
				if(act.getDescriptor().equals(tag.descriptor())){ 
					//create action instance with arguments
					ActionInstance ai = new ActionInstance(act, thisJoinPoint.getArgs());
					//store action instance in current behavior array
					APRoot root = ((ActuatorInstance)executor).getActuatorRep().getRoot().getRoot();
					//get up on the agent part tree
					while(root instanceof AgentPart){
						root = root.getRoot();
					}
					if(root instanceof StaticImage){
						((StaticImage)root).getRoot().getAssociatedAgent().registerAction(ai);
					}else{
						logger.error("Could not register action, static image not found");
					}	
				}
			}
		}else{
			logger.error("Executor " + executor.toString() +" is not connected to a software image");
		}
	}
	
	/**
	 * Combines the performed actions and agent perception in a snapshot and stores it in the agent's dynamic image
	 * Since conditions change along time, clones must be used to fill the dynamic image information
	 * @param executor
	 * @param tag
	 */
	after(Object executor, AgentLoop tag):agentLoop(executor,tag){
		if(executor instanceof VisualSoftwareAgent){
			//get the agent's dynamic image 
			DynamicImage di = ((VisualSoftwareAgent)executor).getSoftwareImage().getDynamicImage();
			//if no behaviour was registered add a NoAction to it
			if(((VisualSoftwareAgent)executor).getPerformedActions().isEmpty()){
				((VisualSoftwareAgent)executor).getPerformedActions().add(new NoAction());
			}
			//read conditions
			List<Condition> conditions = ((VisualSoftwareAgent)executor).getAgentPerception().getCurrentConditions();
			//read actions
			List<ActionInstance> behaviour = ((VisualSoftwareAgent)executor).getPerformedActions();
			//update dynamic image with a new snapshot
			try {
				di.update(new Snapshot(conditions, behaviour));
				//inform all registered observers
				((VisualSoftwareAgent)executor).recallObservers();
			} catch (CloneNotSupportedException e) {
				logger.error("Could not update dynamic image with " +  conditions.toString() + " -> " + behaviour.toString());
				logger.error(e);
			}
		}else{//the agent was not correcly identifyed
			logger.warn("Agent " + executor.getClass().getCanonicalName() + " is not annotated with @VisibleAgent");
		}
	}
	
}
