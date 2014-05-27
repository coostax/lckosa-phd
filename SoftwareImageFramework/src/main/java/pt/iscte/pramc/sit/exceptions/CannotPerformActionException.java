/**
 * Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.exceptions;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * 
 *         Exception thrown when an agent cannot perform an action
 * 
 * @since 22 de Fev de 2012
 * @version 0.1
 */
public class CannotPerformActionException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private final String message;

    public CannotPerformActionException(String message) {
	this.message = message;
    }

    @Override
    public String getMessage() {
	return message;
    }
}
