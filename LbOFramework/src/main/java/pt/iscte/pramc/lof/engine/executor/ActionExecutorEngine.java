/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.executor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import pt.iscte.pramc.lof.agent.Apprentice;
import pt.iscte.pramc.lof.domain.LbOInstance;
import pt.iscte.pramc.lof.engine.LTEngine;
import pt.iscte.pramc.lof.engine.common.LearningTools;
import pt.iscte.pramc.lof.engine.common.holders.PossibleBehaviour;
import pt.iscte.pramc.lof.exception.ActionWithManyParamsException;
import pt.iscte.pramc.lof.exception.CannotBuildActionInstanceException;
import pt.iscte.pramc.sit.agent.VisualSoftwareAgent;
import pt.iscte.pramc.sit.ext.NoAction;
import pt.iscte.pramc.sit.ext.Pair;
import pt.iscte.pramc.sit.swi.di.ActionInstance;
import pt.iscte.pramc.sit.swi.si.Action;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * 
 * The execution method is responsible for calling the supplied actions
 * 
 * @since Aug 24, 2011
 * @version 0.1
 */
public class ActionExecutorEngine extends LTEngine{

	/**
	 * The actions to be called associated to the current conditions
	 */
	private final List<Pair<List<LbOInstance<?>>,PossibleBehaviour>> toCall;
	
	/**
	 * 
	 * @param apprentice
	 */
	public ActionExecutorEngine(Apprentice apprentice) {
		super(apprentice);
		toCall = new ArrayList<Pair<List<LbOInstance<?>>,PossibleBehaviour>>();
	}

	/**
	 * Calls the action represented by the supplied action instance
	 * 
	 * @param ai
	 * @return
	 */
	private boolean call(ActionInstance ai) {
		if(ai.equals(new NoAction())){
			return true;
		}
		//get the actuator instance for this action
		Object actuatorInst = ((VisualSoftwareAgent)apprentice).getAgentActions().getExecutorFor(ai.getAssociatedAction());
		if(actuatorInst == null){
			logger.error("Actuator instance not found for " + ai.getAssociatedAction());
			return false;
		}
		// retrieve the method
		Method method = ((VisualSoftwareAgent)apprentice).getAgentActions().getMethodFor(ai.getAssociatedAction());
		if(method == null){
			logger.error("Method not found for " + ai.getAssociatedAction());
			return false;
		}
		// call the method on the executor
		try {
			//actions with parameters
			if (ai.hasParameters()) {
				//instance was stored with paramenters, proceed as usual
				invokeWithParams(ai,method,actuatorInst);
			} else if(ai.getAssociatedAction().hasParams()){
				//action with parameters but they have not been stored
				invokeWithParams(buildAiWithParamsFor(ai.getAssociatedAction(),actuatorInst), method, actuatorInst);
			}else{
				//action with no parameters
				method.invoke(actuatorInst);
			}
			//if reached here then action was successfully executed
			return true;
		} catch (Exception e) {
			logger.warn("Could not execute action "+ai.toString());
			//				System.err.println(e.getMessage() + ", for: " + ai.toString());
			//				e.printStackTrace();
			return false;
		}
	}

	/**
	 * Builds an action instance with parameters from the action supplied
	 * @param action the action to build the action instance from
	 * @param executor 
	 * @return
	 * @throws CannotBuildActionInstanceException 
	 */
	private ActionInstance buildAiWithParamsFor(Action action, Object executor) throws CannotBuildActionInstanceException{
		List<Object> parameterVals = new ArrayList<Object>();
		for(String params : action.getParams()){
			if(params != null && params.startsWith(LearningTools.INTERNAL_ARGS_REF)){
				//is an internal attribute, find it on the executor
				final int length = LearningTools.INTERNAL_ARGS_REF.length();
				StringTokenizer st = new StringTokenizer(params.substring(length),".");
				if(st.hasMoreTokens()){
					final String token = st.nextToken();
					//see if it is a field
					try {
						Field field = executor.getClass().getDeclaredField(token);
						if(!field.isAccessible()){
							field.setAccessible(true);
							//TODO: see if there are more tokens
							parameterVals.add(field.get(executor));
							field.setAccessible(false);
						}else{
							parameterVals.add(field.get(executor));
						}
					} catch (Exception e) {
						//it is not a field try a method
						try {
							Method method = executor.getClass().getDeclaredMethod(token);
							parameterVals.add(method.invoke(executor));
						} catch (Exception e1) {
							throw new CannotBuildActionInstanceException(action, token);
						}
					}
				}
			}else{
				logger.warn("missing....");
				//TODO: when it comes from perception
			}
		}
		if(!parameterVals.isEmpty()){
			return new ActionInstance(action,parameterVals);
		}
		throw new CannotBuildActionInstanceException(action, Arrays.toString(action.getParams()));
	}

	/**
	 * Takes care of actions with one to seven parameters
	 * @param ai
	 * @param actuatorInst 
	 * @param method 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws ActionWithManyParamsException 
	 */
	private void invokeWithParams(ActionInstance ai, Method method, Object actuatorInst) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, ActionWithManyParamsException {
		final List<Object> params = ai.getParameters();
		switch (params.size()) {
		case 1:
			method.invoke(actuatorInst,
					params.get(0));
			break;
		case 2:
			method.invoke(actuatorInst,
					params.get(0),params.get(1));
			break;
		case 3:
			method.invoke(actuatorInst,
					params.get(0),params.get(1),params.get(2));
			break;
		case 4:
			method.invoke(actuatorInst,
					params.get(0),params.get(1),params.get(2),params.get(3));
			break;
		case 5:
			method.invoke(actuatorInst,
					params.get(0),params.get(1),params.get(2),params.get(3),params.get(4));
			break;
		default:
			throw new ActionWithManyParamsException(ai);
		}
	}

	/**
	 * The execution method only works at the execution stage
	 */
	@Override
	protected void mechanismCycle() {
		if(!toCall.isEmpty()){
			if(apprentice.isLearningStage()){
				logger.warn("Executing actions in learning stage");
			}
			//see if there are actions to call
			for(Pair<List<LbOInstance<?>>, PossibleBehaviour> pbh : toCall){
				executeBehaviour(pbh);
			}
		}
		//reset the behaviour list
		toCall.clear();
	}

	/**
	 * Executes the provided behaviour
	 * @param pbh the behaviour to execute
	 */
	protected void executeBehaviour(Pair<List<LbOInstance<?>>, PossibleBehaviour> pbh){
		for(ActionInstance ai : pbh.getSecond().behaviour){
			call(ai);
		}
	}

	/**
	 * Selects the first behaviour on the list and puts it on hold to be executed
	 * @param currentConditions the conditions registered for this behaviour
	 * @param possibleBehaviour
	 */
	public synchronized boolean prepareExecution(List<LbOInstance<?>> currentConditions, PossibleBehaviour possibleBehaviour) {
		if(!toCall.isEmpty()){
			logger.warn("Execution with behaviours on holding list");
		}
		return toCall.add(new Pair<List<LbOInstance<?>>, PossibleBehaviour>(currentConditions, possibleBehaviour));
	}
	
}
