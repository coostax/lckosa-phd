/**
 * Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.domain.eval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import pt.iscte.pramc.lof.domain.LbOInstance;
import pt.iscte.pramc.sit.swi.di.ActionInstance;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * Holder for incorrect behaviour listing
 * 
 * Incorrect behaviours show the conditions under which the listed behaviours cannot be executed
 * 
 *  Incorrect behaviours can also include internal attributes in the specified conditions
 *  
 *  Holds the list of visible conditions and the internal attribute accessors that represent all 
 *  conditions where the listed behaviours cannot be applied
 *  
 *  The holded internal attributes must extend the Copiable interface to ensure that a copy of the object is stored and not the object itself
 * 
 *   
 * @since Mar 2012
 * @version 0.1
 */
public class IncorrectBehaviour{
	
	final Logger logger = Logger.getLogger(IncorrectBehaviour.class);
	
	/**
	 * Holds a behaviour and a counter
	 */
	class BehaviourHolder{
		final List<ActionInstance> behaviour;
		
		double counter;
		
		/**
		 * Constructor
		 * @param behaviour
		 */
		public BehaviourHolder(List<ActionInstance> behaviour) {
			//make a copy of the behaviour to store
			this.behaviour = new ArrayList<ActionInstance>(behaviour);
			counter = 1.0;
		}
		
		public void increaseCounter(){
			counter++;
		}
		
		public void decreaseCounter(){
			counter -=0.1;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof List){
				return this.behaviour.equals(obj);
			}
			if(obj instanceof BehaviourHolder){
				return this.behaviour.equals(((BehaviourHolder) obj).behaviour);
			}
			return super.equals(obj);
		}
		
		@Override
		public String toString() {
			return "{"+counter + ", " + behaviour.toString()+"}" ;
		}
	}
	
	/**
	 * THe visible conditions where the behaviours cannot be applied
	 */
	final List<LbOInstance<?>> conditions;

	/**
	 * The internal attributes
	 */
	final List<InternalAttInfo> otherAtts;
	
	/**
	 * Holds the forbidden behaviours
	 */
	final List<BehaviourHolder> behaviours;
	
	
	
	/**
	 * Default constructor with visible conditions and internal attributes
	 * @param conditions the conditions
	 * @param internalAtts the internal attributes
	 */
	public IncorrectBehaviour(List<LbOInstance<?>> conditions,List<InternalAttInfo> internalAtts) {
		//make a copy of the conditions
		this.conditions = new ArrayList<LbOInstance<?>>(conditions);
		this.otherAtts = new ArrayList<InternalAttInfo>(internalAtts);
		this.behaviours = new ArrayList<IncorrectBehaviour.BehaviourHolder>();
	}
	
	/**
	 * Constructor with conditions only
	 * @param conditions
	 */
	public IncorrectBehaviour(List<LbOInstance<?>> conditions){
		this(conditions,new ArrayList<InternalAttInfo>());
	}
	
	/**
	 * Sees if provided conditions are the same as holded conditions
	 * @param conditions
	 * @return
	 */
	public boolean hasConditions(List<LbOInstance<?>> conditions){
		if(otherAtts.isEmpty()){
			//check conditions only 
			return this.conditions.equals(conditions);
		}
		boolean isEqual = true;
		for(InternalAttInfo ia : otherAtts){
			if(ia.getStoredData().getClass().isArray())
				isEqual &= Arrays.equals((Object[])ia.getStoredData(),(Object[])ia.getCurrentData());
			else
				isEqual &= ia.getStoredData().equals(ia.getCurrentData());
		}
		return this.conditions.equals(conditions) && isEqual;
	}
	
	/**
	 * Sees if provided conditions are the same as holded conditions
	 * @param conditions a list of visible conditions
	 * @param internal a list of internal attributes
	 * @return
	 */
	public boolean hasConditions(List<LbOInstance<?>> conditions,List<InternalAttInfo> internal){
		if(internal.isEmpty())
			return hasConditions(conditions);
		return this.conditions.equals(conditions) && this.otherAtts.equals(internal);
	}
	
	/**
	 * Adds an incorrect behaviour to this set of conditions
	 * if the behaviour has been seen before then increment the counter
	 * @param bh the behaviour to add
	 * @return true if correctly added
	 */
	public boolean addBehaviour(List<ActionInstance> bh){
		if(behaviours.contains(bh)){
			//increase counter
			behaviours.get(behaviours.indexOf(bh)).increaseCounter();
		}else{
			behaviours.add(new BehaviourHolder(bh));
		}
		return true;
	}
	
	/**
	 * Determines if this behaviour is listed 
	 * @param bh
	 * @return
	 */
	public boolean hasBehaviour(List<ActionInstance> bh) {
		for(BehaviourHolder holder : behaviours){
			if(holder.equals(bh)){
				return true;
			}
		}
//		int index = behaviours.indexOf(bh);
//		if(index != -1 && behaviours.get(index).counter > 0){
//			return true;
//		}
		return false;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IncorrectBehaviour){
			return conditions.equals(((IncorrectBehaviour) obj).conditions) && 
					otherAtts.equals(((IncorrectBehaviour) obj).otherAtts) &&
					behaviours.equals(((IncorrectBehaviour) obj).behaviours);
		}
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		return conditions + ": " + behaviours.toString();
	}
}