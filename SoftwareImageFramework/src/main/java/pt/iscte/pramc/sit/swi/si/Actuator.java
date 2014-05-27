/**
 *  Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.swi.si;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pt.iscte.pramc.sit.ext.ActuatorInstance;

/**
 * Represents an actuator in the software image The actuator is responsible for
 * applying changes in the environment or in the agent’s internal state. The
 * visual image contains information on the set of actions this actuator can
 * perform.
 * 
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * @version 1.0
 * @since 04/2010
 */
public class Actuator implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * A developer comment
     */
    private final String comment;

    /**
     * The agent instance associated with this actuator
     */
    private final ActuatorInstance instance;

    /**
     * A link to the agent part
     */
    private AgentPart root;

    /**
     * The set of actions that this actuator can perform
     */
    private final List<Action> actionSet;

    /**
     * Default constructor
     * 
     * @param comment
     *            the developer comment
     * @param actionSet
     *            the action set for this actuator
     * @param implementingClass
     *            the class implementing this actuator
     */
    public Actuator(String comment, ActuatorInstance inst) {
	super();
	this.comment = comment;
	this.actionSet = new ArrayList<Action>();
	this.instance = inst;
	// link the created actuator representation with the agent instance
	if (instance != null) {
	    instance.setActuatorRep(this);
	}
    }

    /**
     * Clone construction Makes a copy of the passed actuator
     * 
     * @param toCopy
     */
    public Actuator(Actuator toCopy) {
	super();
	this.comment = new String(toCopy.getComment());
	this.actionSet = new ArrayList<Action>(toCopy.actionSet);
	this.instance = null;
    }

    /**
     * @return the action set for this actuator
     */
    public List<Action> getActionSet() {
	return actionSet;
    }

    /**
     * @return the comment for this actuator
     */
    public String getComment() {
	return comment;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	StringBuffer text = new StringBuffer();
	for (Action act : getActionSet()) {
	    text.append("    [action] ");
	    text.append(act.getComment() + "\n");
	    text.append(act.toString());
	    text.append("\n");
	    text.append("    [/action] \n");
	}
	return text.toString();
    }

    /**
     * Gets a similarity measurement between actuators.
     * 
     * Two actuators are 100% similar if they have the same set of actions
     * 
     * @param otherActuator
     *            the other actuator to compare
     * @return 1.0 if actuators are 100% similar. A value between 0 and 0.99 if
     *         actuators share some similarities
     */
    public double getSimilarity(Actuator otherActuator) {
	double similarity = 0.0;
	// see if actuators perform the same number of actions
	if (this.getNumberOfActions()
		.equals(otherActuator.getNumberOfActions())) {
	    for (Action myAction : this.actionSet) {
		INNER: for (Action otherAction : otherActuator.getActionSet()) {
		    if (myAction.isSimilar(otherAction)) {
			similarity += 1.0;
			break INNER;
		    }
		}
	    }
	}
	return similarity == 0.0 ? 0.0 : (similarity / new Double(
		getNumberOfActions()));
    }

    /**
     * @return the number of actions this actuator can perform
     */
    public Integer getNumberOfActions() {
	return this.getActionSet() != null ? this.getActionSet().size() : 0;
    }

    /**
     * Adds a new action to this Actuator Sets the action's root
     * 
     * @return true if action correctly added, false if not
     */
    public boolean addAction(Action action) {
	if (actionSet.contains(action)) {
	    System.err.println("ERROR: action " + action.toString()
		    + " already exists in this actuator");
	    return false;
	}
	boolean ok = actionSet.add(action);
	if (ok) {
	    action.setRoot(this);
	}
	return ok;
    }

    /**
     * @param toCompare
     * @return true if this actuator is implemented by the provided type
     */
    public boolean isImplementedBy(Object toCompare) {
	return this.instance.equals(toCompare);
    }

    /**
     * @return the instance that implements this actuator
     */
    public ActuatorInstance getImplementedInstance() {
	return instance;
    }

    /**
     * @return the agent part that roots this actuator
     */
    public AgentPart getRoot() {
	return root;
    }

    /**
     * Sets the root
     * 
     * @param root
     */
    public void setRoot(AgentPart root) {
	this.root = root;
    }
}
