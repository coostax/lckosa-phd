/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.common.holders;

import java.util.Comparator;
import java.util.List;

import pt.iscte.pramc.lof.engine.common.SolutionProvider;
import pt.iscte.pramc.sit.swi.di.ActionInstance;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         A possible behaviour holds the behaviour and the probability of
 *         applying such behaviour
 * 
 * @version 0.1
 * @since Jul 13, 2011
 */
public class PossibleBehaviour {

	public final List<ActionInstance> behaviour;

	public final double probability;
	
	public SolutionProvider provider;

	public final static Comparator<PossibleBehaviour> COMPARE_BY_GREATEST_PROBABILITY = new Comparator<PossibleBehaviour>() {

		@Override
		public int compare(PossibleBehaviour o1, PossibleBehaviour o2) {
			if (o1.probability > o2.probability) {
				return -1;
			} else if (o1.probability < o2.probability) {
				return 1;
			} else {
				return 0;
			}
		}

	};

	/**
	 * Default constructor
	 * 
	 * @param behaviour
	 * @param probability
	 * @param provider where this possible behaviour came from
	 */
	public PossibleBehaviour(List<ActionInstance> behaviour, double probability, SolutionProvider provider) {
		super();
		this.behaviour = behaviour;
		this.probability = probability;
		this.provider = provider;
	}

	@Override
	public String toString() {
		return behaviour + " >>> " + probability;
	}

}
