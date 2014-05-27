/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.agent.cm.jess;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Represents an executor for the JESS rule engine. Makes use of the
 *         user-defined function facilities from JESS to call the methods
 *         associated to agent actions. JESS rules only allow calling static
 *         methods on the rule result. This makes impossible to call agent
 *         actions inside JESS rules.
 * 
 *         Each agent associatedInstance or actuator (or whatever class contains
 *         the action methods) must implement this interface to be associated to
 *         an instance of the JESSActionExecutor class.
 * 
 * 
 *         Instances that implement this interface must provide the
 *         getRuleEngine method
 * @see JESSActionExecutor
 * @version 2.0
 * @since Jun 30, 2011
 * 
 * @deprecated due to licensing restrictions. Use Jadex Engine
 */
@Deprecated
public interface JessActuatorInstance {

    /**
     * @return the rule engine associated with this actuator
     */
    public RuleEngine getRuleEngine();

}
