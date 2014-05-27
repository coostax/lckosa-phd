/**
 * Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.eval;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.pramc.lof.agent.Apprentice;
import pt.iscte.pramc.lof.domain.BehaviourInstance;
import pt.iscte.pramc.lof.domain.LbOInstance;
import pt.iscte.pramc.lof.domain.Step;
import pt.iscte.pramc.lof.domain.eval.IncorrectBehaviour;
import pt.iscte.pramc.lof.domain.eval.InternalAttInfo;
import pt.iscte.pramc.lof.engine.LearningMethod;
import pt.iscte.pramc.lof.engine.LTEngine;
import pt.iscte.pramc.lof.engine.common.LearningTools;
import pt.iscte.pramc.lof.engine.common.SolutionProvider;
import pt.iscte.pramc.lof.engine.common.holders.PossibleBehaviour;
import pt.iscte.pramc.lof.engine.learning.MirrorLearningEngine;
import pt.iscte.pramc.lof.engine.memory.RecallMechanism;
import pt.iscte.pramc.lof.engine.memory.SequentialMemoryEngine;
import pt.iscte.pramc.lof.exception.CannotFindProperSolutionException;
import pt.iscte.pramc.sit.agent.VisualSoftwareAgent;
import pt.iscte.pramc.sit.ext.NoAction;
import pt.iscte.pramc.sit.ext.Pair;
import pt.iscte.pramc.sit.swi.di.ActionInstance;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 *
 * Main class for the evaluation method.
 * 
 *   Evaluation provides the agent with a confidence value that determines when the agent changes between the learning and the execution stage.
 *   
 *   The confidence represents the number of actions proposed correclty throughout the agent execution. 
 *   
 *   The confidence is calculated as Total correct actions / Total actions proposed
 *   
 *   
 *   
 *   The evaluation method also manages the individual confidence of each learning method, the method weights. 
 *   The weights are used when selecting the actions to decide which of the methods provides the best proposals. 
 *
 * @since 30 Oct 2011
 * @version 0.1 Confidence level functions activated at learning stage 
 * @version 2.0 Confidence and Tresholds are normalized values
 */
public class EvaluationEngine extends LTEngine{

	/**
	 * Holds the relation between the learning method and its normalized weight
	 * 
	 * @author Paulo Costa (paulo.costa@iscte.pt)
	 */
	class LearningMethodHolder<T extends LearningMethod>{
		public final T method;
		private double weight;
		
		/**
		 * Initializes this holder. Sets the normalized weights to 0.5
		 * @param method
		 */
		public LearningMethodHolder(T method) {
			super();
			this.method = method;
			this.weight = 0.5;
		}
		
		/**
		 * Decreases the weights until zero
		 * @param value
		 */
		public void decreaseWeightBy(double value){
			if(weight-value < 0.001){
				weight = 0.001;
			}else{
				weight -= value;
			}
		}
		
		/**
		 * Increases the weights
		 * @param value
		 */
		public void increaseWeightBy(double value){
			weight += value;
		}
		
		public double getWeight() {
			return weight;
		}
		
	}
	
	/**
	 * Holds the results from pre or post evaluation
	 *
	 * @since Mar 6, 2012
	 * @version 0.1
	 */
	class EvaluationResuls{
		private final List<Pair<DSEvaluator,Boolean>> results;
		
		public EvaluationResuls(){
			this.results =  new ArrayList<Pair<DSEvaluator,Boolean>>();
		}
		
		/**
		 * Adds a new evaluation result
		 * @param evaluator the evaluator
		 * @param evaluation the result of the evaluation
		 */
		public void addEvaluation(DSEvaluator evaluator,boolean evaluation){
			results.add(new Pair<DSEvaluator, Boolean>(evaluator, evaluation));
		}
		
		/**
		 * @return false if any of the evaluations is false, true otherwise
		 */
		public boolean getFinalEvaluation(){
			for(Pair<DSEvaluator, Boolean> pair : results){
				if(!pair.getSecond()){
					return false;
				}
			}
			return true;
		}
		
		/**
		 * @return true if it contains evaluation information, false otherwise
		 */
		public boolean hasInformation(){
			return !results.isEmpty();
		}
		
		/**
		 * @return the average weights of the evaluators that provided positive feedbacks
		 */
		public double getPositiveAvg(){
			double result = 0.0;
			int count = 0;
			for(Pair<DSEvaluator, Boolean> pair : results){
				if(pair.getSecond()){
					result += pair.getFirst().getWeight();
					count++;
				}
			}
			return count != 0 ? result/count : 0;
		}
		
		/**
		 * @return the average weights of the evaluators that provided negative feedbacks
		 */
		public double getNegativeAvg(){
			double result = 0.0;
			int count = 0;
			for(Pair<DSEvaluator, Boolean> pair : results){
				if(!pair.getSecond()){
					result += pair.getFirst().getWeight();
					count++;
				}
			}
			return count != 0 ? result/count : 0;
		}
		
		/**
		 * Runs the correction methods on the evaluators if a negative evaluation was provided
		 * @param conditions the current conditions
		 * @return The actions performed by the agent, an empty set of actions if no correction is needed
		 */
		public List<ActionInstance> runCorrectiveActions(List<LbOInstance<?>> conditions){
			//go through all evaluators
			List<ActionInstance> actions = new ArrayList<ActionInstance>();
			for(Pair<DSEvaluator, Boolean> pair : results){
				if(!pair.getSecond()){
					actions.addAll(pair.getFirst().performCorrectiveActions(conditions));
				}
			}
			return actions;
		}
	}
	
	
	
	//----ATTRIBUTES--------
	
	/**
	 * The agent's overall confidence normalized according to the number of times the agent proposes actions
	 */
	//private double confidence;
	
	/**
	 * Holds the number of times the agent proposes actions
	 */
	private int numProposedActions;
	
	
	/**
	 * Holds the number of actions proposed correctly
	 */
	private double numCorrectActionsProposed;
	
	
	/**
	 * The provider being currently evaluated on execution
	 */
	//private SolutionProvider executionProvider;
	
	/**
	 * Evaluation to give to execution
	 */
	private boolean correctlyExecuted;
	
	/**
	 * The memory method and recall mechanism and their weights
	 */
	private final LearningMethodHolder<RecallMechanism> recall;
	
	/**
	 * The mirror method mechanism and its weights
	 */
	private final LearningMethodHolder<MirrorLearningEngine> mirror;
	
	/**
	 * The list of pre execution evaluators
	 */
	private final List<DSEvaluator> preEvaluators;
	
	/**
	 * The list of post execution evaluators
	 */
	private final List<DSEvaluator> postEvaluators;

	/**
	 * Tells if behaviour was blocked
	 */
	private boolean blockedBehaviour;
	
	/**
	 * The list of blocked condition-behaviour instances
	 */
	private List<IncorrectBehaviour> incorrectBehaviours;

	/**
	 * Tells if the evaluation method can block behaviours that 
	 *  listed in the negative training examples
	 */
	private final boolean isAbleToBlock;
	
	//----CONSTRUCTORS--------
	
	/**
	 * Default constructor
	 * @param apprentice the apprentice agent that has this method
	 * @param mirror 
	 * @param memory 
	 * @param initValue the initial value for the proposed actions
	 */
	public EvaluationEngine(Apprentice apprentice, SequentialMemoryEngine memory, MirrorLearningEngine mirror, int initValue, boolean isAbleToBlock) {
		super(apprentice);
		this.isAbleToBlock = isAbleToBlock;
		this.numProposedActions = initValue;
		this.numCorrectActionsProposed = 0;
		this.recall = new LearningMethodHolder<RecallMechanism>(memory.getRecall());
		this.mirror = new LearningMethodHolder<MirrorLearningEngine>(mirror);
		this.preEvaluators = new ArrayList<DSEvaluator>();
		this.postEvaluators = new ArrayList<DSEvaluator>();
		this.blockedBehaviour = false;
		this.incorrectBehaviours = new ArrayList<IncorrectBehaviour>();
	}

	//------------- Learning stage evaluation ------------
	
	/**
	 * Evaluates the agent on the learning stage
	 * Compares the solution provided by the mirror method and the recall method with the actions observed in the snapshot
	 * The weight of the method that proposes the correct actions is increased 
	 * 
	 * old: Confidence is increased/decreased depending on the probability provided by the response
	 * 
	 * since v2.0: the number of correct actions proposed increases depending on the probability of the proposed actions
	 * since v2.0: each time this evaluation is called the number of actions proposed increases
	 * 
	 * @param step observed snapshot
	 * @return true if the agent is capable of providing a correct response to this situation, false otherwise
	 */
	public synchronized boolean evaluateLearning(Step step){
		if(apprentice.isExecutionStage()){
			logger.warn("Calling learning stage evaluation on execution stage");
		}
		if(step != null){
			//increase number of proposed actions
			numProposedActions++;
			PossibleBehaviour mirrorBh;
			PossibleBehaviour recallBh;
			boolean mirrorCorrect;
			boolean recallCorrect;
			//update mirror method
			mirror.method.update();
			//get solutions from learning methods
			try {
				//(mirror)
				mirrorBh = mirror.method.estimateBehaviourFor(step.getConditions()).get(0);
				//check match
				mirrorCorrect = LearningTools.doBehavioursMatch(mirrorBh.behaviour, step.getBehaviour().getValue());
			} catch (CannotFindProperSolutionException e) {
				mirrorBh = null;
				mirrorCorrect = false;
			}try {
				//(recall)
				recallBh = recall.method.estimateBehaviourFor(step.getConditions()).get(0);
				//check match
				recallCorrect = LearningTools.doBehavioursMatch(recallBh.behaviour, step.getBehaviour().getValue());
			} catch (CannotFindProperSolutionException e) {
				recallBh = null;
				recallCorrect = false;
			}
			if(mirrorCorrect && recallCorrect){
				//if both correct increase confidence with highest value
				increaseCorrectActionsProposed(Math.max(mirrorBh.probability,recallBh.probability));
				//increase weights on both
				mirror.increaseWeightBy(mirrorBh.probability);
				recall.increaseWeightBy(recallBh.probability);
				return true;
			}else if(mirrorCorrect){//mirror has the correct answer
				increaseCorrectActionsProposed(mirrorBh.probability);
				//increase mirror weights
				mirror.increaseWeightBy(mirrorBh.probability);
				return true;
			}else if(recallCorrect){//recall has the correct answer
				increaseCorrectActionsProposed(recallBh.probability);
				//increase recall weights
				recall.increaseWeightBy(recallBh.probability);
				return true;
			}else{//no one is correct pay attention to null values!!
				final double mirrorProb = mirrorBh != null ? mirrorBh.probability : 0.1;
				final double recallProb = recallBh != null ? recallBh.probability : 0.1;
				//decrease confidence with highest probability - no longer needed in v2.0
				reduceConfidence(Math.max(mirrorProb,recallProb));
				//reduce weights on both methods
				mirror.decreaseWeightBy(mirrorProb);
				recall.decreaseWeightBy(recallProb);
				return false;
			}
		}
		return false;
	}
	
	//------------- Execution stage evaluation ------------

	/**
	 * Evaluates the execution of  the actions proposed for the current conditions
	 * which depends on the value of the executionEval field
	 * 
	 * Sets the last performed step if evaluation was positive
	 * 
	 * since v2.0:  the number of actions proposed correctly increases even when not possible to determine if actions were correct 
	 * since v2.0: each time this evaluation is called the number of actions proposed increases
	 * @param pair The condition behaviour set
	 */
	public void evaluateExecutionOf(Pair<List<LbOInstance<?>>,PossibleBehaviour> pair) {
		//if the behaviour was blocked there is nothing to evaluate
		if(blockedBehaviour){
			setExecutionEval(false);
		}else{//if not blocked then evaluate
			//increase number of actions proposed
			numProposedActions++;
			//run post-execution evaluators
			EvaluationResuls eval = runPostExecutionEval(pair); 
			if(!eval.getFinalEvaluation()){
				correctlyExecuted = false;
			}
			if(correctlyExecuted){
				//v2.0 - increase correct actions by one
				increaseCorrectActionsProposed(1);
				//set last performed step
				//add behaviour to instances to create a step
				List<LbOInstance<?>> instances = new ArrayList<LbOInstance<?>>(pair.getFirst());
				instances.add(new BehaviourInstance(apprentice.getBehaviourAttribute(), pair.getSecond().behaviour));
				//set it as the last performed step	
				apprentice.setLastPerformedStep(instances);
			}else{//not correctly executed
				//add to list of incorrect behaviours
				addToIncorrectBehaviours(pair.getFirst(), pair.getSecond().behaviour, new ArrayList<InternalAttInfo>());
				//reduce confidence and weights
				reduceConfidenceAndEngineWeights(pair.getSecond(), eval.getNegativeAvg());
				//gather conditions
				List<LbOInstance<?>> instances = new ArrayList<LbOInstance<?>>(pair.getFirst());
				//do the necessary corrective actions
				List<ActionInstance> correctiveActions = eval.runCorrectiveActions(instances);
				if(correctiveActions.isEmpty()){
					//TODO: see if current snapshot is available at this moment
					//get behaviour from current performed actions
					if(((VisualSoftwareAgent)apprentice).getPerformedActions() != null && 
							!((VisualSoftwareAgent)apprentice).getPerformedActions().isEmpty()){
						instances.add(new BehaviourInstance(apprentice.getBehaviourAttribute(), 
								((VisualSoftwareAgent)apprentice).getPerformedActions()));
					}else{
						List<ActionInstance> actions = new ArrayList<ActionInstance>();
						actions.add(new NoAction());
						instances.add(new BehaviourInstance(apprentice.getBehaviourAttribute(), 
								actions));	
					}
				}else{ 
					//make behaviour the corrective actions
					instances.add(new BehaviourInstance(apprentice.getBehaviourAttribute(), correctiveActions));
				}
				apprentice.setLastPerformedStep(instances);
			}
		}
	}
	
	/**
	 * Sets the execution evaluation to negative (false) or positive (true)
	 * @param b
	 */
	public void setExecutionEval(boolean b) {
		this.correctlyExecuted = b;
	}

	//-------------------- PRE and POST EVALUATIONS ----------------------------
	
		/**
		 * Runs the pre-execution behaviour evaluators
		 * fails (returns false) whenever a pre-evaluator provides a negative feedback
		 * @param pair
		 * @return true if all pre evaluators provided positive feedback
		 */
		public boolean runPreExecutionEval(
				Pair<List<LbOInstance<?>>, PossibleBehaviour> pair) {
			boolean evaluation = true;
			for(DSEvaluator eval : preEvaluators){
				if(!eval.provideFeedBackOn(pair.getFirst(), pair.getSecond().behaviour)){
					//on negative feedback add to list of incorrect behaviours
					if(eval instanceof Teacher){
						addToIncorrectBehaviours(pair.getFirst(),pair.getSecond().behaviour,((Teacher)eval).provideAdditionalInfo());
					}
					addToIncorrectBehaviours(pair.getFirst(),pair.getSecond().behaviour,new ArrayList<InternalAttInfo>());
					evaluation = false;
				}
			}
			return evaluation;
		}
		
		/**
		 * Runs the post-execution behaviour evaluators
		 * @param pair
		 * @return
		 */
		protected EvaluationResuls runPostExecutionEval(
				Pair<List<LbOInstance<?>>, PossibleBehaviour> pair) {
			EvaluationResuls results = new EvaluationResuls();
			for(DSEvaluator evaluator : postEvaluators){
				boolean eval = evaluator.provideFeedBackOn(pair.getFirst(), pair.getSecond().behaviour);
				//perform corrective actions if necessary
//				if(!eval){
//					evaluator.performCorrectiveActions(apprentice, pair.getFirst(), pair.getSecond().behaviour);
//				}
				//add to evaluation results
				results.addEvaluation(evaluator,eval);
			}
			return results;
		}
		
		/**
		 * Adds a new pre execution evaluator
		 * @param evaluator
		 * @return true if successfull
		 */
		public boolean addPreEvaluator(DSEvaluator evaluator) {	
			return preEvaluators.add(evaluator);
		}
		
		/**
		 * Adds a new post execution evaluator
		 * @param evaluator
		 * @return true if successfull
		 */
		public boolean addPostEvaluator(DSEvaluator evaluator) {	
			return postEvaluators.add(evaluator);
		}
		
//--------------------------------- EXECUTION BLOCKING --------
	
	/**
	 * Blocks a behaviour from being executed. 
	 * 
	 * adds it to to list of wrong behaviours 
	 * 
	 * Also reduces the method's weigth and agent confidence accordingly
	 */
	public void blockBehaviour(Pair<List<LbOInstance<?>>,PossibleBehaviour> pair) {
		if(isAbleToBlock){
			//assign blocked behaviour
			this.blockedBehaviour = true;
			//reduce confidence and weights
			final double w;
			if(SolutionProvider.MIRROR.equals(pair.getSecond().provider)){
				w = mirror.weight / (recall.weight + mirror.weight);
			}else if(SolutionProvider.RECALL.equals(pair.getSecond().provider)){
				w = recall.weight / (recall.weight + mirror.weight);
			}else if(SolutionProvider.BOTH.equals(pair.getSecond().provider)){
				w = Math.max(mirror.weight, recall.weight) / (recall.weight + mirror.weight);
			}else{
				w = 0.5;
			}
			//reduce confidence
			reduceConfidenceAndEngineWeights(pair.getSecond(), w);
		}
	}

	/**
	 * Unblocks a behaviour. Allows behaviour execution
	 */
	public void unblockBehaviour() {
		this.blockedBehaviour = false;
	}
	
	/**
	 * @return true if behaviour has been blocked
	 */
	public synchronized boolean isBlockedBehaviour() {
		return blockedBehaviour;
	}
	
	//--------------------- INCORRECT BEHAVIOUR MANAGEMENT ----------------------
	
	/**
	 * Adds the elements to the list of incorrect behaviours
	 * @param conditions the conditions
	 * @param bh the behaviour
	 * @param internalAtts the list of internal attribute, may be empty or null if not used
	 */
	private void addToIncorrectBehaviours(List<LbOInstance<?>> conditions,
			List<ActionInstance> bh, List<InternalAttInfo> internalAtts) {
		IncorrectBehaviour ib = null;
		for(IncorrectBehaviour ibh : incorrectBehaviours){
			if(ibh.hasConditions(conditions,internalAtts)){
				ib = ibh;
				break;
			}
		}
		if(ib == null){//create new
			ib = new IncorrectBehaviour(conditions,internalAtts);
			incorrectBehaviours.add(ib);
		}
		//add behaviour
		ib.addBehaviour(bh);
	}
	
	/**
	 * Determines if the behaviour is incorrect for the provided conditions
	 * The behaviour is incorrect if it is in the list of incorrect behaviours
	 * @param cond the conditions
	 * @param bh the behaviour as a list of action instances
	 * @return
	 */
	public synchronized boolean isBehaviourIncorrectFor(List<LbOInstance<?>> cond,
			List<ActionInstance> bh) {
		//see if conditions are listed
		for(IncorrectBehaviour ibh : incorrectBehaviours){
			if(ibh.hasConditions(cond) && ibh.hasBehaviour(bh)){
				return true;
			}
		}		
		return false;
	}
	
	//-------------Helper Methods---------------
	
	/**
	 * Applies the rules to reduce the overall agent confidence and the method weights according to the supplied arguments.
	 *   
	 * @param pair
	 * @param eval
	 */
	private void reduceConfidenceAndEngineWeights(PossibleBehaviour pbh,
			double weight) {
		//decrease confidence by the probability of the behaviour plus the average probabilities of the evaluators
		reduceConfidence(pbh.probability + weight);
		final double multiplier = weight != 0 ? weight : 0.5;
		if(SolutionProvider.MIRROR.equals(pbh.provider)){
			//decrease mirror weights
			mirror.decreaseWeightBy(pbh.probability * multiplier);
		}else if(SolutionProvider.RECALL.equals(pbh.provider)){
			//decrease recall weights
			recall.decreaseWeightBy(pbh.probability * multiplier);
		}else if(SolutionProvider.BOTH.equals(pbh.provider)){
			//decrease both 
		}
		else{
			logger.error("behavior came from unknown provider");
		}
	}
	
	/**
	 * Reduces the confidence by the supplied number
	 * @param num the number to subtract from confidence
	 * @deprecated since V2.0 there is no need to reduce the confidence
	 */
	public void reduceConfidence(double num) {
		//confidence -= num;
	}

	/**
	 * since V2.0 increases the number of actions proposed correctly
	 * @param num
	 */
	protected void increaseCorrectActionsProposed(double num){
		numCorrectActionsProposed += num;
		//confidence += num;
	}

	/**
	 * What the method does each time it is called
	 * In learning stage, evaluate the passed 
	 */
	@Override
	protected void mechanismCycle() {
		//TODO: see if something is necessary here
	}
	
	/**
	 * since v2.0: the confidence is calculated by CorrectActionsProposed / TotalActionsProposed
	 * @return the normalized value for the agent's confidence
	 */
	public double getConfidence() {
		return numCorrectActionsProposed/numProposedActions;
	}
	
	/**
	 * @return the normalized weights for the classification method
	 */
	public double getClassificationMethodWeight(){
		return mirror.getWeight() / (mirror.getWeight() + recall.getWeight());
	}
	
	/**
	 * @return the normalized weights for the recall method
	 */
	public double getRecallMethodWeight(){
		return recall.getWeight() / (mirror.getWeight() + recall.getWeight());
	}
	
	/**
	 * @param estimator the identifier for the recall or mirror method 
	 * @return the weights for the recall or mirror method
	 */
	public double getWeightFor(LearningMethod estimator){
		if(estimator.equals(recall.method)){
			return getRecallMethodWeight();
		}else if(estimator.equals(mirror.method)){
			return getClassificationMethodWeight();
		}else{
			logger.equals("Estimator of type "+estimator.getClass().getSimpleName() + " is unknown!!!");
			return 0;
		}
	}
	
	/**
	 * @return info on the execution of a behaviour
	 */
	public boolean isCorrectlyExecuted() {
		return correctlyExecuted;
	}
	
}
