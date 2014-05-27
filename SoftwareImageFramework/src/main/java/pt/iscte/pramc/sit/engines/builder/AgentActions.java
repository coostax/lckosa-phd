/**
 * Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.engines.builder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import pt.iscte.pramc.sit.swi.si.Action;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * 
 *         Holds the relation between agent visible actions (the method itself)
 *         and the Action representation in the software image
 * 
 * @since 13 de Fev de 2012
 * @version 0.1
 */
public final class AgentActions {

    /**
     * @author Paulo Costa (paulo.costa@iscte.pt)
     * 
     *         Internal class that represents an association between the action
     *         representation, the action itself and the actuator that executes
     *         that action
     * 
     * @since 13 de Fev de 2012
     * @version 0.1
     */
    class Association {
	public final Action action;
	public final Method method;
	public final Object executor;

	/**
	 * @param action
	 * @param method
	 * @param executor
	 */
	public Association(Action action, Method method, Object executor) {
	    super();
	    this.action = action;
	    this.method = method;
	    this.executor = executor;
	}

	/**
	 * Two associations are equal if they have the same action
	 */
	@Override
	public boolean equals(Object obj) {
	    if (obj instanceof AgentActions.Association) {
		return this.action == ((AgentActions.Association) obj).action;
	    }
	    return super.equals(obj);
	}
    }

    /**
     * The list of associations for the agent
     */
    private final List<AgentActions.Association> associations;

    public AgentActions() {
	this.associations = new ArrayList<AgentActions.Association>();
    }

    /**
     * Creates a new association for this agent actions
     * 
     * @param vAction
     *            the action representation in the software image
     * @param method
     *            the action itself
     * @param executor
     *            the actuator that executes the action
     */
    public boolean associate(Action vAction, Method method, Object executor) {
	final AgentActions.Association aux = new AgentActions.Association(
		vAction, method, executor);
	if (!associations.contains(aux)) {
	    return associations.add(aux);
	}
	return false;
    }

    /**
     * @param associatedAction
     *            the action to find the executor
     * @return the associated executor, null if nothing found
     */
    public Object getExecutorFor(Action associatedAction) {
	for (AgentActions.Association assoc : associations) {
	    if (assoc.action == associatedAction) {
		return assoc.executor;
	    }
	}
	return null;
    }

    /**
     * @param associatedAction
     *            the action to find the method
     * @return the associated method, null if nothing found
     */
    public Method getMethodFor(Action associatedAction) {
	for (AgentActions.Association assoc : associations) {
	    if (assoc.action == associatedAction) {
		return assoc.method;
	    }
	}
	return null;
    }

}
