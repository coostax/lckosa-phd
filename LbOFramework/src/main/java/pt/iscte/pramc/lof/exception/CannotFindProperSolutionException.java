/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.exception;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Exception thrown when the method is not able to provide a proper
 *         solution
 * 
 * @version 0.1
 * @since Jul 20, 2011
 */
public class CannotFindProperSolutionException extends Exception {

	private final String message;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CannotFindProperSolutionException(String message) {
		this.message = message;
	}

	public CannotFindProperSolutionException(Exception e) {
		super(e);
		this.message = null;
	}

	@Override
	public String getMessage() {
		return "ERROR: " + message != null ? message : super.getMessage() ;
	}

}
