/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.exceptions;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Exception thrown when it is not possible to attach an agent
 * 
 * @version 0.1
 * @since Oct 19, 2011
 */
public class ObserverException extends Exception {

    // enum Causes{
    // ALREADY_ATTACHED,
    // NOT_ATTACHED,
    // CANNOT_ATTACH,
    // CANNOT_DETACH
    // }

    private final String cause;

    public ObserverException(String cause) {
	this.cause = cause;
    }

    @Override
    public String getMessage() {
	return super.getMessage() + " Cause: " + cause;
    }

}
