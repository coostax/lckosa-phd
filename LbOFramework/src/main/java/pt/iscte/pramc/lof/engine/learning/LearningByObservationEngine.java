/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.learning;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.pramc.lof.domain.Step;
import pt.iscte.pramc.lof.engine.common.holders.PossibleBehaviour;
import pt.iscte.pramc.lof.engine.learning.ports.AlgorithmConnector;
import pt.iscte.pramc.sit.ext.PropertiesLoader;
import pt.iscte.pramc.sit.swi.di.ActionInstance;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Implements the learning by observation method.
 * 
 *         The learning by observation method makes part of the apprentice's
 *         decision method. It contains the metrics for deciding if the agent is
 *         prepared to perform actions or if it needs further observations. It
 *         makes the necessary calls to observe another agent and controls the
 *         observation time and statistics.
 * 
 *         The method uses algorithm ports to store and manage the observed
 *         data.
 * 
 *         The method uses the observation method to find and manage appropriate
 *         experts to observe
 * 
 * @version 0.1 Engine follows the solutions provided by the selected port
 * @version 1.1 Replaced by MirrorLearningEngine
 * @since Jul 01, 2011
 * @deprecated use MirrorLearningEngine instead
 */
@SuppressWarnings("unused")
@Deprecated
public class LearningByObservationEngine {

	public static final double DIFFERENT_PARAMS_FACTOR = new Double(
			PropertiesLoader.loadPropertyFrom("learning.properties",
					"different.params.value", "0.5"));

	private static final double BEHAVIOUR_NOT_LISTED_FACTOR = new Double(
			PropertiesLoader.loadPropertyFrom("learning.properties",
					"behaviour.not.listed", "1"));
	/**
	 * The method's confidence on the performed actions
	 */
	private double confidence;

	/**
	 * The list of observed cases
	 */
	private final List<Step> story;

	/**
	 * The base algorithm used by this method
	 */
	private final AlgorithmConnector algorithm;

	/**
	 * The latest list of possible behaviours
	 */
	private List<PossibleBehaviour> possibleBehaviours;

	/**
	 * Constructor, initializes confidence value and associates the base
	 * algorithm
	 * 
	 * @param algorithmPort
	 */
	public LearningByObservationEngine(AlgorithmConnector algorithmPort) {
		this.algorithm = algorithmPort;
		this.confidence = 0.0;
		this.story = new ArrayList<Step>();
		this.possibleBehaviours = null;
	}

	/**
	 * @return the current confidence in the agent's response
	 */
	public double getConfidence() {
		return confidence;
	}

	/**
	 * Updates the confidence value based on the correctiveness of the
	 * prediction made from the supplied episode
	 * 
	 * @param episode
	 */
	public void updateConfidenceOn(Step episode) {
//		try {
//			possibleBehaviours = algorithm.provideSolutionsFor(episode
//					.getConditions());
//			if (isSameBehaviour(possibleBehaviours.get(0).behaviour,
//					episode.getBehaviour())) {
//				// the first possible behaviour is equal to the behaviour from
//				// the snapshot
//				// increase the confidence by the factor provided in the
//				// algorithm
//				confidence += possibleBehaviours.get(0).probability;
//			} else if (LbOParser
//					.isSimilarBehaviour(possibleBehaviours.get(0).behaviour,
//							episode.getBehaviour())) {
//				// the first possible behavior has the same action sequence but
//				// with different parameters
//				// increase confidence by a factor of 0.5 times the factor
//				// provided in the algorithm
//				confidence += possibleBehaviours.get(0).probability
//						* DIFFERENT_PARAMS_FACTOR;
//			} else {// behaviour different from the first, find the
//					// correspondent
//				boolean found = false;
//				for (int i = 1; i != possibleBehaviours.size(); ++i) {
//					if (isSameBehaviour(possibleBehaviours.get(i).behaviour,
//							episode.getBehaviour())) {
//						// is equal to the behaviour from the snapshot
//						// decrease the confidence by the inverse of the factor
//						// provided in the algorithm
//						confidence -= 1 - possibleBehaviours.get(i).probability;
//						found = true;
//						break;
//					}
//				}
//				if (!found) {// behaviour was not listed in the list of possible
//								// behaviours
//					confidence -= BEHAVIOUR_NOT_LISTED_FACTOR;
//				}
//			}
//		} catch (CannotFindProperSolutionException e) {
//			// set confidence to zero;
//			confidence = 0;
//			possibleBehaviours = null;
//		}
	}

	/**
	 * Validates if two behaviours are the same. Two behaviours are the same if
	 * they have the same action instances in the same order
	 * 
	 * @param bh1
	 * @param bh2
	 * @return
	 */
	public boolean isSameBehaviour(List<ActionInstance> bh1,
			List<ActionInstance> bh2) {
		if (bh1.size() == bh2.size()) {
			for (int i = 0; i != bh1.size(); ++i) {
				// actions must be in the exact same order
				if (!bh1.get(i).isTheSameAs(bh2.get(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Loads an episode to the learning method Transforms the observed snapshot
	 * to the apprentice's perspective using the matching matrix
	 * 
	 * @param episode
	 *            the observed snapshot
	 */
	public void load(Step episode) {
		// TODO: verify if episode already exists and take action when that
		// happens
		// TODO: make special attention to stalled experts (that do nothing)
		if (!story.contains(episode)) {
			story.add(episode);
			//algorithm.load(episode);
		}
	}

	/**
	 * Loads an array of episodes consisting on the historical data from an
	 * observed expert Verifies if the loaded data already exists in the agent's
	 * memory
	 * 
	 * @param hData
	 */
	public void loadHistory(List<Step> hData) {
		for (Step ep : hData) {
			if (!story.contains(ep)) {
				story.add(ep);
			}
		}
	}

	/**
	 * @return the last observed episode, null if agent has not observed
	 *         anything
	 */
	public Step getLatestEpisode() {
		if (!story.isEmpty()) {
			return story.get(story.size() - 1);
		}
		return null;
	}

	/**
	 * @return the latest set of possible behaviours calculated by the expert
	 */
	public List<PossibleBehaviour> getLatestPossibleBehaviours() {
		return possibleBehaviours;
	}

	/**
	 * Gets the best behaviour for the supplied conditions
	 * 
	 * @param conditions
	 * @return the best behaviour for the supplied conditions
	 */
//	public List<ActionInstance> getBestBehaviourFor(List<Condition> conditions) {
//		// obtain list of possible behaviours from algorithm
//		try {
//			List<PossibleBehaviour> behaviours = algorithm
//					.provideSolutionsFor(conditions);
//			return behaviours.get(0).behaviour;
//		} catch (CannotFindProperSolutionException e) {
//			// whenever a proper solution is not found return confidence to zero
//			confidence = 0;
//			return null;
//		}
//	}
}
