/** Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.swi.di;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.iscte.pramc.sit.ext.cloning.CloneHelper;

/**
 * 
 * Represents a snapshot of the agent's behaviour:
 * 
 * Snapshots contain information on:
 * 
 * - The agent's perception when the behaviour took place: the conditions.
 * 
 * - The behaviour(s) performed by the agent with the associated parameters: the
 * behaviour instance
 * 
 * - To prevent changes in condition data after the snapshot has been created,
 * the snapshot holds to locally created clones of the condition data
 * 
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * @version 2.0 Switched Case to Snapshot
 * @version 2.1 When creating snapshots, local copies (clones) of the condition
 *          data is made to prevent unwanted changes in the agent's dynamic
 *          image
 * @since 04/2010
 */
public class Snapshot implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    protected final List<Condition> conditions;

    protected final List<ActionInstance> behaviour;

    /**
     * Default constructor
     */
    protected Snapshot() {
	super();
	this.conditions = new ArrayList<Condition>();
	this.behaviour = new ArrayList<ActionInstance>();
    }

    /**
     * Default constructor, receives the list of conditions
     * 
     * @param conditions
     *            the agent's perception when the behaviour was taken
     * @throws CloneNotSupportedException
     */
    public Snapshot(List<Condition> conditions)
	    throws CloneNotSupportedException {
	this();
	// create new conditions with a copy of the data
	for (Condition cond : conditions) {
	    this.conditions.add(new Condition(cond.getSource(), CloneHelper
		    .makeCopyOf(cond.getData())));
	}
    }

    /**
     * Another constructor, receives the list of conditions and a single action
     * 
     * @param conditions
     *            the agent's perception when the behaviour was taken
     * @param action
     *            the action the agent took
     * @throws CloneNotSupportedException
     */
    public Snapshot(List<Condition> conditions, ActionInstance action)
	    throws CloneNotSupportedException {
	this(conditions);
	this.behaviour.addAll(Collections.singletonList(action));
    }

    /**
     * Another constructor, receives the list of conditions and a list of action
     * instances
     * 
     * @param conditions
     *            the agent's perception when the behaviour was taken
     * @param behavior
     *            the actions the agent took
     * @throws CloneNotSupportedException
     */
    public Snapshot(List<Condition> conditions, List<ActionInstance> behavior)
	    throws CloneNotSupportedException {
	this(conditions);
	this.behaviour.addAll(behavior);
    }

    /**
     * @return the agent's perception when the behaviour was triggered, in the
     *         form of an AgentPerception object
     */
    public List<Condition> getConditions() {
	return conditions;
    }

    /**
     * @return the behaviour performed by the agent when these conditions
     *         existed
     */
    public List<ActionInstance> getBehaviour() {
	return behaviour;
    }

    @Override
    public String toString() {
	StringBuffer sb = new StringBuffer();
	sb.append(conditions.toString());
	sb.append(" -> [behaviour] ");
	sb.append(behaviour.toString());
	sb.append("[/behaviour]");
	return sb.toString();
    }

    /**
     * Copy constructor
     * 
     * @param toCopy
     * @throws CloneNotSupportedException
     */
    public Snapshot(Snapshot toCopy) throws CloneNotSupportedException {
	this.behaviour = new ArrayList<ActionInstance>(toCopy.getBehaviour());
	this.conditions = new ArrayList<Condition>(toCopy.getConditions());
    }

    /**
     * Adds a new action instance to this case
     * 
     * @param ai
     */
    public void addActionInstance(ActionInstance ai) {
	this.behaviour.add(ai);
    }

    public boolean hasBehaviour() {
	return behaviour != null && !behaviour.isEmpty();
    }

}