/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.exception;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 * Exception thrown when an attribute is not found
 *  
 * @version 0.1
 * @since Jul 20, 2011
 */
public class InstanceValueNotFoundException extends Exception {

	private final String message;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 * 
	 * @param attribute
	 */
	public InstanceValueNotFoundException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return "Not found: " + message;
	}

}
