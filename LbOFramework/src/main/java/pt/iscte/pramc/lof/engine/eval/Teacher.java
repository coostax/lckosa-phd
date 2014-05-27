/**
 * Copyright 2012 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.eval;

import java.util.List;

import pt.iscte.pramc.lof.domain.eval.InternalAttInfo;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * Represents a teacher pre-evaluator
 * 
 * Besides the normal methods, teachers also provide info on additional internal data 
 * @since Mar 29, 2012
 * @version 0.1
 */
public interface Teacher extends DSEvaluator {

	/**
	 * @return A list of information on important internal attributes used in teacher comparison
	 */
	public List<InternalAttInfo> provideAdditionalInfo();
	
}
