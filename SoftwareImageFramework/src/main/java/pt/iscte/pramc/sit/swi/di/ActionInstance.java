/**
 * 
 */
package pt.iscte.pramc.sit.swi.di;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pt.iscte.pramc.sit.ext.cloning.CloneHelper;
import pt.iscte.pramc.sit.swi.SoftwareImage;
import pt.iscte.pramc.sit.swi.si.Action;

/**
 * Action instances represent agent calls for an action in the dynamic image
 * 
 * Action instances are made out by the action and the parameters used to call
 * the action
 * 
 * 
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 * @version 0.1
 * @since Jan 7, 2011
 */
public class ActionInstance implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Action associatedAction;

    private List<Object> parameters;

    /**
     * Default constructor, builds an single action instance with no parameters
     * 
     * @param associatedAction
     *            the action associated to this instance
     */
    // public ActionInstance(Action associatedAction) {
    // super();
    // this.associatedAction = associatedAction;
    // this.parameters = Collections.EMPTY_LIST;
    // }

    /**
     * Default constructor. Builds an action instance with a set of parameters
     * 
     * @param associatedAction
     *            the action associated to this instance
     * @param parameters
     *            an array of parameters used for this action instance
     */
    public ActionInstance(Action associatedAction, Object... parameters) {
	super();
	this.associatedAction = associatedAction;
	this.parameters = parameters != null ? Arrays.asList(parameters)
		: Collections.emptyList();
    }

    /**
     * Action instance constructor with a list of parameter objects
     * 
     * @param associatedAction
     * @param parameters
     */
    public ActionInstance(Action associatedAction, List<Object> parameters) {
	super();
	this.associatedAction = associatedAction;
	this.parameters = parameters;
    }

    /**
     * Copy constructor
     * 
     * @param actionInst
     */
    public ActionInstance(ActionInstance actionInst) {
	this.associatedAction = new Action(actionInst.getAssociatedAction());
	this.parameters = new ArrayList<Object>();
	if (actionInst.hasParameters()) {
	    // make a copy of the parameters if possible
	    for (Object elem : actionInst.getParameters()) {
		try {
		    this.parameters.add(CloneHelper.makeCopyOf(elem));
		} catch (CloneNotSupportedException e) {
		    SoftwareImage.swiLogger
			    .warn("Could not make a clone of parameter " + elem
				    + " in ActionInstance " + actionInst
				    + ". Making a direct copy");
		    this.parameters.add(elem);
		}
	    }
	}
	// this.parameters = new ArrayList<Object>(actionInst.getParameters());
    }

    /**
     * @return the associatedAction
     */
    public Action getAssociatedAction() {
	return associatedAction;
    }

    /**
     * @return the parameters
     */
    public List<Object> getParameters() {
	return parameters;
    }

    @Override
    public String toString() {
	StringBuffer sb = new StringBuffer(associatedAction.toString());
	sb.append(" params: ");
	sb.append(parameters.toString());
	return sb.toString();
    }

    /**
     * Validates if another action instance is the same as this one Two action
     * instances are the same if they make reference to actions with the same
     * descriptor and their parameters are the same
     * 
     * @param otherActionInstance
     * @return
     */
    public boolean isTheSameAs(ActionInstance otherActionInstance) {
	if (this.associatedAction.isSimilar(otherActionInstance
		.getAssociatedAction())) {
	    // both have a similar action
	    if (this.getParameters().size() == otherActionInstance
		    .getParameters().size()) {
		// both have the same number of parameters
		return this.getParameters().containsAll(
			otherActionInstance.getParameters());
	    }
	}
	return false;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof ActionInstance) {
	    return this.isTheSameAs((ActionInstance) obj);
	}
	return super.equals(obj);
    }

    /**
     * @return true if this action instance has parameters
     */
    public boolean hasParameters() {
	return parameters != null && !parameters.isEmpty();
    }

}
