/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.exceptions;

import pt.iscte.pramc.sit.swi.si.Atomic;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 * @version 0.1
 * @since Aug 22, 2011
 */
public class NoMacthForAtomicElementException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private final String message;

    public NoMacthForAtomicElementException(Atomic element) {
	this.message = "No macthing between " + element.toString();
    }

    @Override
    public String getMessage() {
	return message + "/n" + super.getMessage();
    }

}
