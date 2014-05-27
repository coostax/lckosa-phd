/**
 * Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine;

import java.util.List;

import pt.iscte.pramc.lof.domain.LbOInstance;
import pt.iscte.pramc.lof.engine.common.holders.PossibleBehaviour;
import pt.iscte.pramc.lof.exception.CannotFindProperSolutionException;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 *
 * Interface representation of an instance that implements methods for proposing actions
 *
 * @since 15 de Fev de 2012
 * @version 0.1 
 */
public interface LearningMethod {

	/**
	 * Estimates a behaviour based on the provided conditions
	 * @param conditions the conditions for the behaviour estimate
	 * @return a list of behaviours for the provided conditions
	 */
	public List<PossibleBehaviour> estimateBehaviourFor(
			List<LbOInstance<?>> conditions) throws CannotFindProperSolutionException;
	
}
