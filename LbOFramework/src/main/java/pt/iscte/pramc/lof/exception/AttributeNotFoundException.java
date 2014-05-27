/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.exception;

import pt.iscte.pramc.sit.swi.si.DataSource;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 * Exception thrown when an attribute is not found
 *  
 * @version 0.1
 * @since Jul 20, 2011
 */
public class AttributeNotFoundException extends Exception {

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
	public AttributeNotFoundException(DataSource source) {
		this.message = "Attribute not found for datasource: "+source.getDescriptor();
	}

	public AttributeNotFoundException(String string) {
		this.message = string;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
