/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import pt.iscte.pramc.lof.engine.memory.StepMatcher;
import pt.iscte.pramc.lof.exception.MatchException;
import pt.iscte.pramc.sit.engines.match.TranslationMatrix;
import pt.iscte.pramc.sit.ext.Pair;
import pt.iscte.pramc.sit.swi.di.Condition;
import pt.iscte.pramc.sit.swi.di.Snapshot;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         The apprentice's version of a snapshot. Makes use of the extended versions of conditions and behaviour to store not only the observed data but also all the possible values for the condition / behaviour
 *         
 *         Apprentices store the observed behaviour as its own behaviour
 * 
 * 		Since steps are built from the translation of observed snapshots there is no need to make copies of the supplied data
 * 
 * 		v2.1 - Steps no longer need to extend snapshots since they use different storage method
 * 
 * @version 1.1
 * @version 2.1 Steps are made out of a list of LbOInstance
 * @since Jul 22, 2011
 */
public class Step {

	static Logger logger = Logger.getLogger(Step.class);
	
	/**
	 * 
	 * @author Paulo Costa (paulo.costa@iscte.pt)
	 *
	 * Holds the following step and the strength (the number of times this step has been observed as the following one)
	 *
	 * @since Mar 2, 2012
	 * @version 0.1
	 */
	class Following{
		private final Step step;
		private int strength;
		
		public Following(Step step) {
			this.step = step;
			this.strength = 1;
		}
		
		/**
		 * Increases the strength of this following step
		 */
		public void increaseStrength(){
			this.strength++;
		}
		
		/**
		 * @return the strength 
		 */
		public int getStrength() {
			return strength;
		}
		
		/**
		 * @return the step associated to this following
		 */
		public Step getStep() {
			return step;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Step.Following){
				return this.step.equals(((Step.Following) obj).step);
			}else if(obj instanceof Step){
				return this.step.equals(obj);
			}
			return super.equals(obj);
		}
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final TranslationMatrix tMatrix;

	/**
	 * The following steps
	 */
	private final List<Step.Following> followingSteps;
	
	/**
	 * The conditions and behaviour stored as instances in an unmodifiable list
	 */
	private final List<LbOInstance<?>> instances;
	
	/**
	 * The maximum strength of the following steps;
	 */
	private int maxStrength;
	
//	/**
//	 * The conditions for this step
//	 */
//	private final List<Condition> conditions;
//	
//	/**
//	 * The sequence of actions applied when conditions are observed
//	 */
//	private final List<ActionInstance> behaviour;
//	
	/**
	 * Constructor with translation matrix
	 * @param conditions
	 * @param behaviour
	 * @throws CloneNotSupportedException 
	 */
	public Step(List<LbOInstance<?>> instances,
			TranslationMatrix matrix){
		this.instances = Collections.unmodifiableList(instances);
				//new ArrayList<LbOInstance>(instances);
		this.tMatrix = matrix;
		this.followingSteps = new ArrayList<Step.Following>();
		this.maxStrength = 1;
	}
	
	/**
	 * Manual constructor
	 * @param instances
	 */
	public Step(LbOInstance<?>... instances){
		this.tMatrix = null;
		this.followingSteps = new ArrayList<Step.Following>();
		this.instances = Arrays.asList(instances);
		this.maxStrength = 1;
	}
	
	/**
	 * Condition constructor. Builds a step from a list of conditions and the list of agent attributes
	 */
//	public Step(List<Condition> conditions, final List<LbOAttribute> attributes){
//		this.instances = new ArrayList<LbOInstance>();
//		//add instances from condition with provided attributes
//		for(Condition cond: conditions){
//			for(LbOAttribute att: attributes){
//				if(cond.getSource().equals(att.g))
//			}
//		}
//		this.tMatrix = null;
//		this.followingSteps = new HashSet<Step>();
//	}

	/**
	 * Used only for tests
	 * Builds a step from a snapshot without translation 
	 * Since no translation matrix is supplied it implies that the data is coming from a source that does not change
	 * For external tests use the buildStep() method instead
	 * 
	 * @param conditions
	 * @param behavior
	 * @Deprecated Use the constructor with translation matrix. For tests use the buildStep() method instead. 
	 */
	public static Step fromSnapshot(Snapshot snp){
		logger.warn("Building steps with a testing method");
		List<LbOInstance<?>> instances = new ArrayList<LbOInstance<?>>();
		//add conditions
		for(Condition cond: snp.getConditions()){
			instances.add(new LbOInstance(new ConditionAttribute(cond.getSource()), cond.getData()));
		}
		//add behaviour
		instances.add(new BehaviourInstance(new BehaviourAttribute(), snp.getBehaviour()));
		return new Step(instances,null);
	}

	/**
	 * Points the supplied episode as following this one. Ensures that it is not already following
	 * @param ep the following episode
	 * @return true if everything is good
	 */
	public boolean addFollowingStep(Step ep){
		final Step.Following following = new Following(ep); 
		final int index = followingSteps.indexOf(following);
		if(index != -1){//already in following
			//increase the streght of the following
			followingSteps.get(index).increaseStrength();
			//increase maxStrenght if necessary
			final int aux = followingSteps.get(index).getStrength();
			maxStrength = aux > maxStrength ? aux : maxStrength;
			return true;
		}
		//if not then add as following
		return followingSteps.add(following);
	}
	
	public boolean hasFollowing(){
		return !followingSteps.isEmpty();
	}
	
	public List<Step> getFollowingSteps(){
		List<Step> result = new ArrayList<Step>();
		for(Step.Following following : followingSteps){
			result.add(following.getStep());
		}
		return result;
	}
	
	public List<Pair<Double,Step>> getFollowingStepsWithStrength(){
		List<Pair<Double,Step>> result = new ArrayList<Pair<Double,Step>>();
		for(Step.Following following : followingSteps){
			result.add(new Pair<Double,Step>(new Double(following.getStrength())/new Double(maxStrength),
					following.getStep()));
		}
		return result;
	}
	
	public int getNumOfFollowingSteps(){
		return followingSteps.size();
	}
	
	/**
	 * Makes use of the StepMatching algorithm to verify if two steps reflect the same thing
	 * @param st
	 * @return true if this step mathes the provided one.
	 * @see Object.equals()
	 */
	@Override
	public boolean equals(Object obj){
		if(obj instanceof Step){
			try {
				return StepMatcher.match(this, (Step)obj) == 1.0;
			} catch (MatchException e) {
				logger.error(e);
			}
		}
		return false;
	}
	
	/**
	 * @return the conditions from the list of instances
	 */
	public List<LbOInstance<?>> getConditions() {
		List<LbOInstance<?>> conditions = new ArrayList<LbOInstance<?>>();
		for(LbOInstance<?> instance : instances){
			if(instance.isCondition()){
				conditions.add(instance);
			}
		}
		return conditions;
	}

	/**
	 * @return the behaviour instance, null if no behaviour is found
	 */
	public BehaviourInstance getBehaviour() {
		for(int i = instances.size()-1; i != -1; --i){
			if(instances.get(i).isBehaviour()){
				return (BehaviourInstance)instances.get(i);
			}
		}
		return null;
	}

	/**
	 * @return an unmodifiable list with all the instances (conditions and behaviour) that make up this step
	 */
	public List<LbOInstance<?>> getAll(){
		return instances;
	}
	
	/**
	 * @param position the position of the instance in the instance array
	 * @return the instance (condition or behaviour) at the specified position
	 * @throws IndexOutOfBounds exception if the position is outside of the instance array
	 */
	public LbOInstance<?> getInstanceAt(int position) {
		return instances.get(position);
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Step: ");
		for(LbOInstance<?> instance : instances){
			sb.append(instance.toString());
			sb.append("; ");
		}
		return sb.toString();
	}
	
	/**
	 * Provides a metric value that results in matching two steps
	 * @param ep1
	 * @param ep2
	 * @return
	 * @deprecated use StepMatcher.match() instead
	 */
	@Deprecated
	public static double matchSteps(Step ep1, Step ep2){
		return 0.0;
	}
//		int total = 0;
//		int count = 0;
//		//conditions
//		if(ep1.getConditions().size() > ep2.getConditions().size()){
//			//start from cond1
//			for(Condition cond1 : ep1.getConditions()){
//				for(Condition cond2 : ep2.getConditions()){
//					if(cond1.equals(cond2)){
//						count++;
//					}
//				}
//				total++;
//			}
//		}else{
//			//start from cond2
//			for(Condition cond2 : ep2.getConditions()){
//				for(Condition cond1 : ep1.getConditions()){
//					if(cond1.equals(cond2)){
//						count++;
//					}
//				}
//				total++;
//			}
//		}
//		//behaviours
//		if(ep1.getBehaviour().size() > ep2.getBehaviour().size()){
//			//start from behaviour1
//			for(ActionInstance ai1 : ep1.getBehaviour()){
//				for(ActionInstance ai2 : ep2.getBehaviour()){
//					if(ai1.equals(ai2)){
//						count++;
//					}
//				}
//				total++;
//			}
//		}else{
//			//start from behaviour2
//			for(ActionInstance ai2 : ep2.getBehaviour()){
//				for(ActionInstance ai1 : ep1.getBehaviour()){
//					if(ai1.equals(ai2)){
//						count++;
//					}
//				}
//				total++;
//			}
//		}
//		return (double)count/(double)total;
//	}
}
