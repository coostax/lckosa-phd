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
 *         Exception thrown whenever the apprentice is not focused on observing
 *         an expert
 * 
 * @version 0.1
 * @since Jul 23, 2011
 */
public class NotObservingExpertException extends Exception {

	private final String message;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotObservingExpertException(Apprentice apprentice) {
		this.message = "Apprentice "
				+ ((VisualSoftwareAgent) apprentice).getAgentUUID()
				+ " is not focused in observing an expert";
	}

	@Override
	public String getMessage() {
		return message + "/n" + super.getMessage();
	}

}
