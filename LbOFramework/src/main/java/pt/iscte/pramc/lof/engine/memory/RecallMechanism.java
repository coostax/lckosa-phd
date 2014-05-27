/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import pt.iscte.pramc.lof.agent.Apprentice;
import pt.iscte.pramc.lof.domain.BehaviourInstance;
import pt.iscte.pramc.lof.domain.LbOInstance;
import pt.iscte.pramc.lof.domain.Step;
import pt.iscte.pramc.lof.engine.LearningMethod;
import pt.iscte.pramc.lof.engine.common.SolutionProvider;
import pt.iscte.pramc.lof.engine.common.holders.PossibleBehaviour;
import pt.iscte.pramc.lof.exception.CannotFindProperSolutionException;
import pt.iscte.pramc.lof.exception.MatchException;
import pt.iscte.pramc.sit.ext.Pair;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * The recall mechanism is used by the memory method to follow a line of behaviours
 *
 *The recall mechanism uses two methods for estimating behaviours given a set of conditions:
 *
 *The first method is behaviour centric. It looks in the agent's memory for steps with behaviours that are the same as those in the last performed step 
 *
 *The second method is behaviour and condition centric. It looks in the agent's memory for steps with the same conditions and behaviours as the ones from the last perfomed step
 *
 *After finding the appropriate steps, the mechanism gathers all the following steps and compares those step's conditions with the supplied conditions. 
 *
 *
 * @version 0.1
 * @since Nov 25, 2011
 */
public class RecallMechanism implements LearningMethod{

	Logger logger = Logger.getLogger(RecallMechanism.class);
	
	private static final SolutionProvider provider = SolutionProvider.RECALL;
	
	/**
	 * flag that indicates the use of similar behaviours when searching for steps in memory
	 */
	private final boolean useSimilarBehaviours;
//	private static final boolean USE_SIMILAR_BEHAVIOURS = new Boolean(
//			PropertiesLoader.loadPropertyFrom("learning.properties",
//					"memory.recall.use.similar.behaviours", "false"));
	
	/**
	 * The percentage factor to use in similar behaviours when calculating 
	 */
//	private static final double SIMILAR_BEHAVIOUR_FACTOR = new Double(
//			PropertiesLoader.loadPropertyFrom("learning.properties",
//					"memory.recall.similar.behaviour.factor", "0.5"));
	
	private final SequentialMemoryEngine memEngine;
	
	private final Apprentice apprentice;
	
	/**
	 * Default constructor. Recieves the agent's memory
	 * @param memory the sequential storage where all observed steps are stored
	 */
	public RecallMechanism(final SequentialMemoryEngine engine, final Apprentice apprentice, final boolean useSimilarBhFlag){
		this.memEngine = engine;
		this.apprentice = apprentice;
		this.useSimilarBehaviours = useSimilarBhFlag;
	}
	
	/**
	 * Produces a set of possible behaviours extracted from memory
	 * Only provides results if a last observed step was stored
	 * @param conditions
	 * @return the list of possible behaviours sorted from the highest probability to the lowest one
	 * @throws CannotFindProperSolutionException
	 */
	public List<PossibleBehaviour> estimateBehaviourFor(List<LbOInstance<?>> conditions) throws CannotFindProperSolutionException{
		//the last step depends on weather the agent is in learning or execution stage
		final Step last = apprentice.isLearningStage() ? memEngine.getLastObserved() : apprentice.getLastPerformedStep();
//		Step last = apprentice.getLastPerformedStep();
//		if(last == null){
//			if(apprentice.isExecutionStage() || memEngine.getLastObserved() == null){
//				//find the steps with closest set of conditions
//				return buildFromConditions(conditions);
//			}
//			last = memEngine.getLastObserved();
//		}
		if(last != null){
			//provide behaviours from last
			List<PossibleBehaviour> result = new ArrayList<PossibleBehaviour>();
			//find in memory for similar steps
			result.addAll(buildPossibleBehaviours(conditions, getStepsSimilarTo(last)));
			//only if there are no results or if it is allowed to use similar behaviours
			if(result.isEmpty() || useSimilarBehaviours){
				//find in memory for steps with the same behaviour
				result.addAll(buildPossibleBehaviours(conditions,getStepsWithBehaviour(last.getBehaviour())));
			}
			if(result.isEmpty()){
				throw new CannotFindProperSolutionException("no match to previous behaviour");
			}
			Collections.sort(result,
					PossibleBehaviour.COMPARE_BY_GREATEST_PROBABILITY);
			return result;
		}
		throw new CannotFindProperSolutionException("no last performed behaviour");
	}

	/**
	 * Gets the step with the closest conditions
	 * @param conditions
	 * @return
	 * @throws CannotFindProperSolutionException 
	 */
	private List<PossibleBehaviour> buildFromConditions(List<LbOInstance<?>> conditions) throws CannotFindProperSolutionException {
		if(this.memEngine.getMemorySize() > 0){
			double probability = 0.0;
			Step correct = null;
			for(int i = 0; i!= memEngine.getMemorySize();++i){
				final Step stp = memEngine.getStepAt(i);
				try {
					final double prob= StepMatcher.match(stp.getConditions(), conditions);
					if(prob > probability){//update
						probability = prob;
						correct = stp;
					}
				} catch (MatchException e) {
				}
			}
			if(correct != null){
				return Collections.singletonList(new PossibleBehaviour(correct.getBehaviour().getValue(), probability, provider));
			}
		}
		throw new CannotFindProperSolutionException("No last performed step");
	}

	/**
	 * Builds a list of possible behaviours for a given list of steps and provided conditions
	 * @param conditions the current conditions to build the behaviour
	 * @param steps the list of steps
	 */
	private List<PossibleBehaviour> buildPossibleBehaviours(List<LbOInstance<?>> conditions, List<Step> steps) {
		List<PossibleBehaviour> result = new ArrayList<PossibleBehaviour>();
		for(Step stp : steps){
//			Iterator<Step> stit = stp.getFollowingSteps();
//			while(stit.hasNext()){
			for(Pair<Double,Step> following : stp.getFollowingStepsWithStrength()){
				try {
					result.add(buildPossibleBehaviour(following.getSecond(), conditions));
				} catch (MatchException e) {
					logger.warn("Could not build possible behaviour.",e);
				}
			}
		}
		return result;
	}

	/** 
	 * Compares the conditions provided by the step in step info with the current conditions 
	 * @param step the step used for the analysis along with information on its strength
	 * @param currentConditions the current conditions for behaviour application
	 * @return the possibility of applying the behaviour contained in the step with the provided conditions
	 * @throws MatchException  when impossible to match conditions 
	 */
	protected PossibleBehaviour buildPossibleBehaviour(Step step, List<LbOInstance<?>> currentConditions) throws MatchException{
		//calculate how close are the supplied conditions from the conditions in the step
		//multiply them with the strength of the following step 
		double probability = StepMatcher.match(step.getConditions(), currentConditions); //* stepInfo.getFirst();
		return new PossibleBehaviour(step.getBehaviour().getValue(), probability,SolutionProvider.RECALL);
	}
	
	/**
	 * @param lastPerformed
	 * @return all steps that are similar to the supplied step
	 */
	protected List<Step> getStepsSimilarTo(final Step toCompare) {
		List<Step> steps = new ArrayList<Step>();
		for(int i = 0; i != memEngine.getMemorySize(); ++i){
			try {
				double val = StepMatcher.match(memEngine.getStepAt(i),toCompare);
				if(val == 1.0){//behaviours match
					steps.add(memEngine.getStepAt(i));
				}
			} catch (MatchException e) {
				logger.warn(e);
			}
		}
		return steps;
	}

	/**
	 * @param behaviour 
	 * @return all steps that have the same behaviour as the supplied behaviour
	 */
	protected List<Step> getStepsWithBehaviour(final BehaviourInstance behaviour) {
		List<Step> steps = new ArrayList<Step>();
		for(int i = 0; i != memEngine.getMemorySize(); ++i){
			try {
				double val = StepMatcher.match(memEngine.getStepAt(i).getBehaviour(),behaviour);
				if(val == 1.0){//behaviours match
					steps.add(memEngine.getStepAt(i));
				}
			} catch (MatchException e) {
				logger.warn(e);
			}
		}
		return steps;
	}
}
