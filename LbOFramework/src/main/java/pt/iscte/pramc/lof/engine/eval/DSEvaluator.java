/**
 * Copyright 2012 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.eval;

import java.util.List;

import org.apache.log4j.Logger;

import pt.iscte.pramc.lof.domain.LbOInstance;
import pt.iscte.pramc.sit.swi.di.ActionInstance;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 *
 * A domain specific evaluator evaluates specific circumstances in agent execution. These are directly related to the agent's domain
 * 
 *  DSEvaluators can be used before action execution (e.g. teacher appraisal) or after execution (e.g. error evaluator)
 *
 *	DSEvaluators are assigned with a weight factor, that is used to increase or decrease agent confidence. Weight can assume the values on the ]0.0,1.0] interval 
 *
 * @since Mar 6, 2012
 * @version 0.1
 */
public interface DSEvaluator {

	public static final Logger logger = Logger.getLogger(DSEvaluator.class);
	
	/**
	 * @return this evaluator's weight in the ]0.0;1.0] interval
	 */
	public double getWeight();
	
	/**
	 * Evaluates agent behaviour after of before execution
	 * @param conditions the conditions that led to the behaviour
	 * @param behaviour the behaviour as a set of action instances
	 * @return true if behaviour performed correctly, false otherwise
	 */
	public boolean provideFeedBackOn(List<LbOInstance<?>> conditions, List<ActionInstance> behaviour);
	
	/**
	 * Performs corrective actions in case of negative evaluation
	 * @param conditions 
	 * @return the representation of the performed actions, an empty list if no actions performed
	 */
	public List<ActionInstance> performCorrectiveActions(List<LbOInstance<?>> conditions);
	
}
