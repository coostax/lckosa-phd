/**
 *  Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.swi.si;

import java.io.Serializable;
import java.util.List;

/**
 * Represents an agent's action in the software image. Actions are the result of
 * the agent's environment manipulation by its actuators. They are triggered by
 * actuators
 * 
 * Action descriptions must be universal for all agents that use the software
 * image to learn by observation.
 * 
 * TODO: find a way to make Method serializable in JSON
 * 
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * @version 2.0
 * @since 04/2010
 */
public class Action implements Serializable, Atomic {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private final String descriptor;

    private final String comment;

    private String[] params;

    private Actuator root;

    /**
     * Default constructor
     * 
     * @param descriptor
     * @param comment
     * @param instance
     */
    public Action(String descriptor, String comment, String... params) {
	super();
	this.descriptor = descriptor;
	this.comment = comment;
	this.params = params != null ? params : new String[0];
    }

    /**
     * Parameter list constructor
     * 
     * @param descriptor
     * @param comment
     * @param instance
     */
    public Action(String descriptor, String comment, List<String> params) {
	super();
	this.descriptor = descriptor;
	this.comment = comment;
	this.params = params != null ? params.toArray(new String[0])
		: new String[0];
    }

    /**
     * Copy constructor Makes a copy of the given Action
     * 
     * @param toCopy
     *            the Action object to copy
     */
    public Action(Action toCopy) {
	super();
	this.descriptor = new String(toCopy.getDescriptor());
	this.comment = new String(toCopy.getComment());
	// Method aux = null;
	// try {
	// aux = CloneHelper.makeCopyOf(toCopy.executor);
	// } catch (CloneNotSupportedException e) {
	// SoftwareImage.swiLogger.warn("Could not clone executor for action " +
	// this.toString() + ". Making a direct link");
	// aux = toCopy.executor;
	// }
	// this.executor = toCopy.executor;
	// action parameters
	if (toCopy.params != null && toCopy.params.length != 0) {
	    this.params = new String[toCopy.params.length];
	    // fill parameters with a copy
	    for (int i = 0; i != this.params.length; ++i) {
		this.params[i] = new String(toCopy.params[i]);
	    }
	} else {
	    this.params = new String[0];
	}
    }

    public String getDescriptor() {
	return descriptor;
    }

    public String getComment() {
	return comment;
    }

    /**
     * @return action description or ast description
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	String aux = "(";
	for (String param : params) {
	    aux += param + ",";
	}
	aux += ")";
	if (descriptor.indexOf("#") != -1) {// strip ontology description
	    return descriptor.substring(descriptor.indexOf("#") + 1) + aux;
	} else {
	    return descriptor + aux;
	}
    }

    /**
     * Verifies if two actions are similar. Two actions are similar if they
     * share the same descriptor
     * 
     * @param otherAction
     *            the action to compare
     * @return 1.0 if agent parts are 100% similar. A value between 0 and 0.99
     *         if agent parts share some similarities
     */
    public boolean isSimilar(Action otherAction) {
	return this.getDescriptor().equals(otherAction.getDescriptor());
    }

    /**
     * @param instance
     * @return true if this action makes reference to the supplied instance
     * @deprecated
     */
    public boolean refersTo(Object instance) {
	return false;// this.instance.equals(instance);
    }

    /**
     * @return the Actuator that roots this action
     */
    public Actuator getRoot() {
	return root;
    }

    /**
     * Sets this action's root
     * 
     * @param root
     */
    public void setRoot(Actuator root) {
	this.root = root;
    }

    /**
     * @return the description of the action parameters
     */
    public String[] getParams() {
	return params;
    }

    /**
     * @return true if this action accepts parameters
     */
    public boolean hasParams() {
	return params.length != 0;
    }

    /**
     * @return the method that executes this action
     */
    // private Method getExecutor() {
    // return executor;
    // }

}
