/**
 * Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.exception;

import pt.iscte.pramc.sit.swi.si.Action;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 *
 * Exception thrown when it is not possible to build an action instance
 *
 * @since 23 de Fev de 2012
 * @version 0.1 
 */
public class CannotBuildActionInstanceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String message;
	
	public CannotBuildActionInstanceException(Action action, String token) {
		this.message = "Cannot build action instance for " + action.toString() + ". Parameter not found: " + token;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}
