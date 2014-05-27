/**
 * Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.exception;

import pt.iscte.pramc.sit.swi.di.ActionInstance;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 *
 * Exception thrown when trying to execute an action with more that a limited number of parameters
 *
 * @since 23 de Fev de 2012
 * @version 0.1 
 */
public class ActionWithManyParamsException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String message; 
	
	public ActionWithManyParamsException(ActionInstance ai) {
		this.message = "Action " + ai.toString() + " exceeds parameter execution: " + ai.getParameters().size();
	}
	
	@Override
	public String getMessage() {
		return message;
	}

}
