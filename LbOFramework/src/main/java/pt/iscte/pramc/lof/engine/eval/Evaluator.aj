/**
 * Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.eval;

import java.util.List;

import org.apache.log4j.Logger;

import pt.iscte.pramc.lof.agent.Apprentice;
import pt.iscte.pramc.lof.domain.LbOInstance;
import pt.iscte.pramc.lof.domain.Step;
import pt.iscte.pramc.lof.engine.common.SolutionProvider;
import pt.iscte.pramc.lof.engine.common.holders.PossibleBehaviour;
import pt.iscte.pramc.lof.engine.executor.ActionExecutorEngine;
import pt.iscte.pramc.lof.exception.BlockedBehaviourException;
import pt.iscte.pramc.sit.ext.NoAction;
import pt.iscte.pramc.sit.ext.Pair;
import pt.iscte.pramc.sit.swi.di.ActionInstance;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 *
 * Listens for calls on important methods throughout the apprentice life.
 * 
 * 
 * In the evaluation stage the evaluation listens to calls on Apprentice.store() method to
 * run a sequence of tests and update the agent's confidence 
 *
 * @since 1 de Fev de 2012
 * @version 0.1 
 */
public aspect Evaluator {

	private final Logger logger = Logger.getLogger(Evaluator.class);
	
//	private final List<EvaluationEngine> engines = new ArrayList<EvaluationEngine>();
	
	/**
	 * Capture evaluation method initialization
	 */
	pointcut evaluationInit(EvaluationEngine eval): initialization(EvaluationEngine.new(..)) && this(eval);
	
	/**
	 * Capture step storage
	 * @param agent the agent where the step is stored
	 * @param stp the step to store
	 */
	pointcut stepStorage(Apprentice agent,Step stp): execution(* Apprentice.store(Step)) && args(stp) && target(agent);
	
	/**
	 * Captures the behaviour selection process in execution stage
	 * @param agent
	 * @param cond
	 */
	pointcut prepareExecution(Apprentice agent,List<LbOInstance<?>> cond, List<PossibleBehaviour> bhs): execution(* Apprentice.prepareExecution(..)) && args(cond,bhs) && target(agent);
	
	/**
	 * Capture the execution of a set of actions
	 */
	pointcut behaviourExecution(ActionExecutorEngine engine,Pair<List<LbOInstance<?>>, PossibleBehaviour> pbh): execution(* ActionExecutorEngine.executeBehaviour(*)) && args(pbh) && target(engine);
	
	/**
	 * Capture calls for individual action execution
	 */
	pointcut actionExecution(ActionExecutorEngine engine,ActionInstance ai): execution(* ActionExecutorEngine.call(*)) && args(ai) && target(engine);
	
	//Initialization
	
//	/**
//	 * Stores the evaluation method for future uses
//	 * @param eval
//	 */
//	after(EvaluationEngine eval): evaluationInit(eval){
//		this.engines.add(eval);
//	}
	
	//------ Learning stage evaluation -------
	/**
	 * Test agent behavior and update confidence accordingly 
	 * before storing the step 
	 * @param agent
	 * @param stp
	 */
	before(Apprentice agent,Step step): stepStorage(agent,step){
		agent.getEvaluationEngine().evaluateLearning(step);
	}
	
	//------ Execution stage evaluation -------
	
	/**
	 * Remove incorrect behaviours from the behaviour selection process
	 */
	before(Apprentice agent,List<LbOInstance<?>> cond, List<PossibleBehaviour> bhs): prepareExecution(agent,cond,bhs){
		while(!bhs.isEmpty()){
			//get first behaviour
			PossibleBehaviour behaviour = bhs.get(0);
			//test
			if(agent.getEvaluationEngine().isBehaviourIncorrectFor(cond,behaviour.behaviour)){
				//remove it from list
				bhs.remove(0);
			}else{
				break;
			}
		}
	}
	
	/**
	 * Prepare evaluation by indicating what solution provider is being evaluated
	 * Run pre-evaluators and proceed as necessary
	 * @throws BlockedBehaviourException 
	 */
	before(ActionExecutorEngine engine,Pair<List<LbOInstance<?>>, PossibleBehaviour> pbh): behaviourExecution(engine,pbh){
		//run pre execution evaluators
		if(engine.getAgent().getEvaluationEngine().runPreExecutionEval(pbh)){
			//reset execution evaluation
			engine.getAgent().getEvaluationEngine().setExecutionEval(true);
			//TODO: remove unwanted actions from possible behaviour
			//tell method behaviour was not blocked
			engine.getAgent().getEvaluationEngine().unblockBehaviour();
		}else{
			//tell method that behaviour was blocked
			engine.getAgent().getEvaluationEngine().blockBehaviour(pbh);
			//stop agent from making an action
			pbh.getSecond().behaviour.clear();
			pbh.getSecond().behaviour.add(new NoAction());
			pbh.getSecond().provider = SolutionProvider.EVALUATION;
//			final Pair<List<LbOInstance<?>>, PossibleBehaviour> aux = new Pair<List<LbOInstance<?>>, PossibleBehaviour>(pbh.getFirst(), new PossibleBehaviour(new ArrayList<ActionInstance>(), 1.0, SolutionProvider.EVALUATION));
//			pbh = aux;
		}
	}
	
	/**
	 * See if agent is performing all actions correctly
	 */
	after(ActionExecutorEngine engine,ActionInstance ai) returning(boolean acomplished): actionExecution(engine,ai){
		if(!acomplished){
			//set a negative evaluation
			engine.getAgent().getEvaluationEngine().setExecutionEval(false);
			//TODO: add this action to the unwanted list
		}
	}
	
	/**
	 * After behaviour execution evaluate it by decreasing or increasing confidence
	 * and by changing mechanism weights
	 */
	after(ActionExecutorEngine engine,Pair<List<LbOInstance<?>>, PossibleBehaviour> pbh): behaviourExecution(engine,pbh){
		engine.getAgent().getEvaluationEngine().evaluateExecutionOf(pbh);
	}
	
}
