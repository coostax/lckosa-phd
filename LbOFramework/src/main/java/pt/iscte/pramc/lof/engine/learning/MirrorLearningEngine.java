/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.learning;

import java.util.List;

import pt.iscte.pramc.lof.agent.Apprentice;
import pt.iscte.pramc.lof.domain.LbOInstance;
import pt.iscte.pramc.lof.engine.LearningMethod;
import pt.iscte.pramc.lof.engine.LTEngine;
import pt.iscte.pramc.lof.engine.common.holders.PossibleBehaviour;
import pt.iscte.pramc.lof.engine.learning.ports.AlgorithmConnector;
import pt.iscte.pramc.lof.exception.CannotFindProperSolutionException;
import pt.iscte.pramc.sit.ext.PropertiesLoader;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 *  Implements the learning by observation method.
 * 
 *         The learning by observation method makes part of the apprentice's
 *         internal engines. It makes use of a set of algorithms to produce outputs for the conditions sent by the apprentice.
 *         Those conditions can come from the apprentice's sensors or from expert observations.
 *          
 *
 * @version 2.1 Integrated learning method with new memory method
 * @since Nov 14, 2011
 */
public class MirrorLearningEngine extends LTEngine implements LearningMethod{
	
	//private static final LearningAlgorithmType USED_ALGORITHM = LearningAlgorithmType.valueOf(PropertiesLoader.loadPropertyFrom("learning.properties","learning.base.algorithm", "W_IBK"));
	
	/**
	 * The algorithm to use as default
	 */
	private static final String DEFAULT_ALGORITHM = "W_KSTAR";
	
//	private static final double DIFFERENT_PARAMS_FACTOR = new Double(
//			PropertiesLoader.loadPropertyFrom("learning.properties",
//					"different.params.value", "0.5"));
//	
//	private static final double BEHAVIOUR_NOT_LISTED_FACTOR = new Double(
//			PropertiesLoader.loadPropertyFrom("learning.properties",
//					"behaviour.not.listed", "1"));
	
	/**
	 * The method's confidence level
	 */
	private double confidence;
	
	/**
	 * The machine learning algorithm used by this method
	 */
	private final AlgorithmConnector algorithm;
	
	
	//----------CONSTRUCTORS------
	
	/**
	 * Default constructor, loads the default learning algorithm provided by the static argument or by the properties file 
	 * @param apprentice the apprentice agent
	 */
	public MirrorLearningEngine(Apprentice apprentice,String propsFile) {
		super(apprentice);
		this.confidence = 0;
		algorithm = propsFile != null ? LearningAlgorithmType.valueOf(PropertiesLoader.loadPropertyFrom(propsFile, 
				"learning.base.algorithm", DEFAULT_ALGORITHM)).getAlgorithmConnector(propsFile) : 
					LearningAlgorithmType.valueOf(DEFAULT_ALGORITHM).getAlgorithmConnector(null);
	}

	
//	/**
//	 * Default constructor, loads the default learning algorithm provided by the static argument or by the properties file 
//	 * @param apprentice the apprentice agent
//	 */
//	public MirrorLearningEngine(Apprentice apprentice, LearningAlgorithmType type) {
//		super(apprentice);
//		this.confidence = 0;
//		algorithm = type != null ? type.getAlgorithmConnector() : USED_ALGORITHM.getAlgorithmConnector();
//	}
	
	/**
	 * Updates the mirror learning method and trains the learning algorithm with the steps stored in memory 
	 */
	public synchronized void update(){
		//train the learning algorithm
		algorithm.train(apprentice.getAttributes(), apprentice.getAllFromMemory());
	}
	
	/**
	 * Provides a set of possible behaviours that are adequate for the provided conditions
	 * 
	 * Mirror learning needs to be trained before using this method (by calling update() method) to provide the best results
	 * 
	 * @param conditions a list of LbOInstances representing the conditions. Conditions can come from observation or perception
	 * @return a list of possible behaviours that can be applied to the supplied conditions
	 * @throws CannotFindProperSolutionException 
	 */
	public synchronized List<PossibleBehaviour> estimateBehaviourFor(List<LbOInstance<?>> conditions) throws CannotFindProperSolutionException{
		//calculate a set of possible solutions
		return algorithm.provideSolutionsFor(conditions,apprentice.getBehaviourAttribute());
	}
	
	
	/**
	 * @see pt.iscte.pramc.lof.engine.LTEngine#mechanismCycle()
	 */
	@Override
	protected void mechanismCycle() {
		update();
	}

	/**
	 * @return this method's confidence
	 */
	public double getConfidence() {
		return confidence;
	}
	
}
