/**
 * Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.agent.cm.jess.exceptions;

/**
 * @author Paulo Costa (coostax@gmail.com)
 * 
 */
public class RuleNotLoadedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return "ERROR: Rules not correctly loaded. " + super.getMessage();
	}

}
