/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.exception;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * @version 0.1
 * @since Nov 7, 2011
 */
public class MatchException extends Exception {

	private final String message;
	
	public MatchException(String string) {
		this.message = string;
	}

	@Override
	public String getMessage() {
		return message + " ; " +super.getMessage();
	}
	
}
