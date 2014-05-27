/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.exception;

import pt.iscte.pramc.lof.agent.Apprentice;
import pt.iscte.pramc.sit.agent.VisualSoftwareAgent;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Thrown when no similar expert agent is found
 * 
 * @version 0.1
 * @since Aug 17, 2011
 */
public class NoSimilarAgentFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String message;

	public NoSimilarAgentFoundException(Apprentice apprentice) {
		if(apprentice != null && apprentice instanceof VisualSoftwareAgent){
		this.message = apprentice.getClass().getSimpleName() + "::"
				+ ((VisualSoftwareAgent) apprentice).getAgentUUID();
		}else{
			this.message = apprentice.getClass().getSimpleName() + "::NO AGENT ID" ;
		}
	}

	@Override
	public String getMessage() {
		return "No similar expert for " + message + ".\n"
				+ super.getMessage();
	}

}
