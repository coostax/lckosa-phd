/**
 * Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.exception;

import pt.iscte.pramc.sit.swi.di.Condition;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 *
 * Exception thrown whenever it is not possible to convert a condition to a LbOAttribute
 *
 * @since 1 de Fev de 2012
 * @version 0.1 
 */
public class CannotConvertToAttributeException extends Exception {

	private final String message;
	
	
	public CannotConvertToAttributeException(Condition cond) {
		this.message = "No corresponding attribute for condition: " + cond + ". ";
	}
	
	@Override
	public String getMessage() {
		return message + super.getMessage();
	}

}
