/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.ext;

import pt.iscte.pramc.sit.swi.di.ActionInstance;
import pt.iscte.pramc.sit.swi.si.Action;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         NoAction is used each time the agent does no action
 * 
 * @version 0.1
 * @since Jul 7, 2011
 */
public final class NoAction extends ActionInstance {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static final Action noActionAtom = new Action("no_action",
	    "no action");

    /**
     * Default constructor
     */
    public NoAction() {
	super(noActionAtom);
    }

    /**
     * Copy constructor
     */
    public NoAction(NoAction toCopy) {
	super(noActionAtom);
    }

}
