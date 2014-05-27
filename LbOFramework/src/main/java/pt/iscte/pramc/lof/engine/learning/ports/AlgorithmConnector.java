/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.learning.ports;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import pt.iscte.pramc.lof.domain.BehaviourAttribute;
import pt.iscte.pramc.lof.domain.LbOAttribute;
import pt.iscte.pramc.lof.domain.LbOInstance;
import pt.iscte.pramc.lof.domain.Step;
import pt.iscte.pramc.lof.engine.common.holders.PossibleBehaviour;
import pt.iscte.pramc.lof.exception.CannotFindProperSolutionException;

/**
 * @author Paulo Costa (coostax@gmail.com)
 * 
 *         Connects machine learning algorithms to learning by observation.
 * 
 *         The algorithm connector bridges the mirror learning mechanism with
 *         solutions such as CBR or other type of classification algorithms.
 * 
 *         Learning engines must be supported by an evaluation method to improve
 *         agent performance
 * 
 *         Allows apprentices to build their case bases and explore solutions
 *         for a given problem
 * 
 * @version 2.1 updated AlgorithmConnector to support the new version of agent
 *          memory
 */
public interface AlgorithmConnector extends Serializable {

	/**
	 * Logger for algorithm connectors
	 */
	public static Logger logger = Logger.getLogger(AlgorithmConnector.class);
	
//	/**
//	 * Indication to use behaviours as lists of actions
//	 * Loaded from properties file, default is false
//	 */
//	public static final boolean BEHAVIOURS_AS_ACTIONS = new Boolean(
//			PropertiesLoader.loadPropertyFrom("learning.properties",
//					"learning.parser.storeBehaviourAsActions", "false"));
//	
//	/**
//	 * The parser for this connector
//	 */
//	final static LbOParser parser = new LbOParser(BEHAVIOURS_AS_ACTIONS);
	
	/**
	 * Provides a set of possible behaviours that are adequate for the provided
	 * conditions
	 * 
	 * @param conditions
	 *            a list of LbOInstances representing the conditions. Conditions
	 *            can come from observation or perception
	 *            @param att the agent's behaviour attribute
	 * @return a list of possible behaviours that can be applied to the supplied
	 *         conditions
	 */
	public List<PossibleBehaviour> provideSolutionsFor(
			List<LbOInstance<?>> conditions, final BehaviourAttribute att)
			throws CannotFindProperSolutionException;

	/**
	 * Trains the algorithm with the supplied attributes and list of steps.
	 * 
	 * @param attributes
	 *            the apprentice's attribute list
	 * @param steps
	 *            the list of examples that will train this algorithm
	 * @return true if training was successfull
	 */
	public boolean train(final List<LbOAttribute<?>> attributes,
			final List<Step> steps);

	/**
	 * @return information on the method
	 * @return
	 */
	public String getInfo();

	/**
	 * Sets properties for this connector from the indicated properties file
	 * @param propsFile the properties file from where properties are extracted
	 */
	public void setProperties(String propsFile);

}
