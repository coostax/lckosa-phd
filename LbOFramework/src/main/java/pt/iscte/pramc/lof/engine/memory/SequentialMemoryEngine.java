/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.memory;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pt.iscte.pramc.lof.agent.Apprentice;
import pt.iscte.pramc.lof.domain.BehaviourAttribute;
import pt.iscte.pramc.lof.domain.BehaviourInstance;
import pt.iscte.pramc.lof.domain.ConditionAttribute;
import pt.iscte.pramc.lof.domain.LbOAttribute;
import pt.iscte.pramc.lof.domain.LbOInstance;
import pt.iscte.pramc.lof.domain.Step;
import pt.iscte.pramc.lof.engine.LTEngine;
import pt.iscte.pramc.lof.exception.AttributeNotFoundException;
import pt.iscte.pramc.lof.exception.CannotConvertToAttributeException;
import pt.iscte.pramc.sit.swi.di.Condition;
import pt.iscte.pramc.sit.swi.di.Snapshot;
import pt.iscte.pramc.sit.swi.si.DataSource;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * The main class of the memory method.
 * 
 * Stores all observed snapshots as a set of steps. Steps hold the condition-action 
 *
 * Manages condition attributes. Condition attributes define the different types of conditions and hold their possible values.
 * Condition attributes are essential for the mirror learning method.
 *
 * Holds the recall mechanism to callback a sequence of steps according to a given condition set 
 *
 * @version 0.1 - Initial version. 
 * @version 1.0 - working version minor bugs to correct. Renamed episode to step. Holds condition attributes
 * @version 1.1 - Solved some problems regarding step sequencing
 *  
 * @since Nov 2, 2011
 */
public class SequentialMemoryEngine extends LTEngine {

	private final Logger logger = Logger.getLogger(SequentialMemoryEngine.class);
	
	/**
	 * The storage facility
	 */
	private final SequentialStorage storage;
	
	/**
	 * The last observed step
	 */
	protected Step lastObservedStep;
	
	/**
	 * The recall mechanism
	 */
	private final RecallMechanism recall;
	
	/**
	 * The attributes stored in this method
	 */
	private final List<LbOAttribute<?>> attributes;
	
	//----- FLAGS ------
	/**
	 * Allow the recall mechanism to be used in execution stage
	 */
	private final boolean ALLOW_RECALL;
	
	private final boolean STORE_ACTIONINSTANCE_DATA;
	
	/**
	 * Default constructor
	 * @param apprentice the apprentice agent associated
	 * @param recallFlag enables or disables the recall mechanism
	 * @param actionInstanceFlag stores action instances with or without parameters
	 */
	public SequentialMemoryEngine(Apprentice apprentice,boolean recallFlag, boolean actionInstanceFlag,boolean useSimilarBhFlag) {
		super(apprentice);
		this.ALLOW_RECALL = recallFlag;
		this.STORE_ACTIONINSTANCE_DATA = actionInstanceFlag;
		this.storage = new SequentialStorage();
		this.recall = new RecallMechanism(this,apprentice,useSimilarBhFlag);
		this.lastObservedStep = null;
		this.attributes = new ArrayList<LbOAttribute<?>>();
	}
	
	/**
	 * Stores the provided step in the agent's memory
	 * associates it with the last observed step
	 * @param steps
	 * @return
	 */
	public synchronized boolean storeObservedStep(final Step step){
		boolean result = true;
		//add step to memory
		Step stored = storage.add(step);
		if(stored == null){
			logger.error("could not store step on memory");
			return false;
		}
		//associate to previous if a previous exists
		if(lastObservedStep != null){
			if(!lastObservedStep.addFollowingStep(stored)){
				logger.warn("Could not add follwing step");
				result = false;
			}
		}else if(stored == step){//there is no last observed and the current step was never observed before 
			//see what is the best candidate for the previous episode
			lastObservedStep = findCandidateForPreviousAt(stored); 
			if(lastObservedStep != null){
				if(!lastObservedStep.addFollowingStep(stored)){
					logger.warn("Could not add follwing step");
					result = false;
				}
			}
		}
		//make the this step the last observed
		lastObservedStep = stored;
		return result;
	}

	/**
	 * Looks in the observed episodes for the best candidate for being the previous episode
	 * @param ep
	 * @return
	 */
	public synchronized Step findCandidateForPreviousAt(Step ep) {
		//start from the last observed episodes
		for(int i = storage.size()-1; i != -1; --i){
			Step episode = storage.get(i);
			if(episode.hasFollowing()){
				for(Step following : episode.getFollowingSteps()){
					if(following.equals(ep)){
						return episode;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Cuts the connection to the last observed step when the agent stops observing
	 * @see pt.iscte.pramc.lof.engine.LTEngine#mechanismCycle()
	 */
	@Override
	protected void mechanismCycle() {
		//put the last observed episode to null each time the agent is not observing
		if(!apprentice.isObserving() && lastObservedStep != null){
			lastObservedStep = null;
		}
	}
	
	public synchronized double getAvgObservations(){
		return storage.getAvgObservations();
	}
	
	public synchronized double getAvgFollowing(){
		return storage.getAvgFollowing();
	}
	
	public synchronized int getMaxObservations(){
		return storage.getMaxObservations();
	}
	
	public RecallMechanism getRecall(){
		return recall;
	}
	
	/**
	 * Prints the apprentice's storage
	 * @return a string representation of the apprentice's storage
	 */
	public synchronized String printStorage() {
		return storage.toString();
	}

	/**
	 * Sees if the supplied conditions exist in memory
	 * @param currentConditions the conditions to look for
	 * @return true if conditions are found in memory, false otherwise
	 */
	public boolean findConditions(List<LbOInstance<?>> currentConditions) {
		return !storage.getForConditions(currentConditions).isEmpty();
	}
	
	/**
	 * @return the occupied memory size
	 */
	public int getMemorySize() {
		return storage.size();
	}
	
	public Step getLastObserved() {
		return lastObservedStep;
	}
	
	/**
	 * @return the attributes for the agent's memory
	 */
	public List<LbOAttribute<?>> getAttributes() {
		return attributes;
	}

	/**
	 * @param step the step to obtain information from
	 * @return the number of times the provided step was observed
	 */
	public int getNumObservedFor(Step step){
		return storage.getNumObservedFor(step);
	}
	
	/**
	 * Retrieves a condition attribute from the agent's memory. If the attribute does not exist then it is created
	 * @param first the datasource that describes the attribute
	 * @return the required attribute
	 * @throws AttributeNotFoundException 
	 */
	public synchronized ConditionAttribute getAttributeFrom(DataSource ds, boolean allowCreation) throws AttributeNotFoundException {
		for(LbOAttribute<?> att: attributes){
			if(att instanceof ConditionAttribute){
				if(((ConditionAttribute)att).isSource(ds)){
					return (ConditionAttribute)att;
				}
			}
		}
		//if not found then create one when allowed
		if(allowCreation){
			ConditionAttribute catt = new ConditionAttribute(ds);
			//add it to attributes
			attributes.add(catt);
			return catt;
		}
		//if attribute was not found throw exception
		throw new AttributeNotFoundException(ds);
	}
	
	/**
	 * Retrieves the behaviour attribute stored in this agent's memory. 
	 * If the attribute does not exist a new one is created
	 * @return the required attribute
	 */
	public synchronized BehaviourAttribute getBehaviourAttribute(){
		for(LbOAttribute<?> att: attributes){
			if(att instanceof BehaviourAttribute){
				return ((BehaviourAttribute)att);
			}
		}
		//if not found then create one
		BehaviourAttribute att = new BehaviourAttribute(STORE_ACTIONINSTANCE_DATA);
		//add it to attributes
		attributes.add(att);
		return att;
	}
	
	/**
	 * Converts a step to a snapshot
	 * WARNING: Only use steps produced by the own agent
	 * @param snp
	 * @return the corresponding step, null if it was not possible to convert
	 * @throws CannotConvertToAttributeException whenever it is not possible to convert this snapshot
	 */
	public synchronized Step convert(Snapshot snp) throws CannotConvertToAttributeException{
		List<LbOInstance<?>> instances = new ArrayList<LbOInstance<?>>();
		//conditions
		for(Condition cond : snp.getConditions()){
			//retrieve condition attribute
			
			try {
				ConditionAttribute ca = getAttributeFrom(cond.getSource(),false);
				//build instance from condition
				instances.add(new LbOInstance<Object>(ca, cond.getData()));
			} catch (AttributeNotFoundException e) {
				throw new CannotConvertToAttributeException(cond);
			}
		}
		//behaviour
		instances.add(new BehaviourInstance(getBehaviourAttribute(), snp.getBehaviour()));
		return new Step(instances, null);
	}

	/**
	 * @param instances the instances that make out this step
	 * @return the step that matches the provided instances, null if nothing is found
	 */
	public synchronized Step getStepFor(List<LbOInstance<?>> instances) {
		return storage.getFromInstances(instances);
	}
	
	/**
	 * @param position
	 * @return the step at the memory's position
	 */
	protected synchronized Step getStepAt(int position){
		return storage.get(position);
	}

	/**
	 * @return all steps stored in memory
	 */
	public synchronized List<Step> getAll() {
		return storage.getAll();
	}
	
	/**
	 * @return true if the use recall flag is active
	 */
	public synchronized boolean canUseRecall(){
		return ALLOW_RECALL;
	}
}
