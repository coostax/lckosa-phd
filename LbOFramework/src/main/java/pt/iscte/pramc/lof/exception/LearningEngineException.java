/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.exception;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Generic dataset exception
 * 
 * @version 0.1
 * @since Jul 20, 2011
 */
public class LearningEngineException extends Exception {

	private final String message;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LearningEngineException(String string) {
		this.message = string;
	}

	@Override
	public String getMessage() {
		return "ERROR: " + message;
	}
}
