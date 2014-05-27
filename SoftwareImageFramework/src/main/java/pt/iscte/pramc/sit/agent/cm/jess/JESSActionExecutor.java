/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.agent.cm.jess;

//import jess.Context;
//import jess.Fact;
//import jess.JessException;
//import jess.Userfunction;
//import jess.Value;
//import jess.ValueVector;

/**
 * @author Paulo Costa (coostax@gmail.com)
 * 
 *         A default executor for the actions selected by the JESS rule engine.
 * 
 *         Makes use of the user-defined function facilities from JESS to call
 *         the methods associated to agent actions. JESS rules only allow
 *         calling static methods on the rule result. This makes impossible to
 *         call agent actions inside JESS rules. By using this helper class,
 *         developers can abstract from the connectivity issues between JESS
 *         rule engine and the agent associatedInstance's code.
 * 
 *         Each agent associatedInstance or actuator (or whatever class contains
 *         the action methods) must be associated to an instance of this class.
 *         Rule files must make calls to this class with the indication of the
 *         action method's name and parameters. The method with the same name
 *         and parameters on the associated class will then be called
 * 
 *         ATTENTION: The user-defined function must be added to the rule
 *         engine, using the java method ruleEngine.addUserfunction()
 * 
 * @version 2.0
 * @deprecated due to licensing restrictions. Use Jadex Engine
 */
@Deprecated
public class JESSActionExecutor { // implements Userfunction {

    // private final JessActuatorInstance associatedInstance;
    // private final String name;
    //
    // /**
    // * Default constructor, associates this class to an IAgentPart or
    // * IAgentActuator
    // *
    // * @param associatedInstance
    // * the associatedInstance to be associated
    // * @param name
    // * the name of this function on the JESS rule file
    // */
    // public JESSActionExecutor(final JessActuatorInstance part, final String
    // name) {
    // this.associatedInstance = part;
    // this.name = name;
    //
    // // add this user function to the rule engine
    // part.getRuleEngine().addUserFunction(this);
    // }
    //
    // /**
    // * Replies to a call on the rule engine by running the corresponding
    // method
    // * on the associated instance
    // */
    // @Override
    // public Value call(final ValueVector vector, final Context context)
    // throws JessException {
    // // get method name
    // final String methodName = vector.get(1).stringValue(context);
    // // get method parameters
    // Object[] params;
    // Class<?>[] paramTypes;
    // if (vector.size() > 2) {
    // params = new Object[vector.size() - 2];
    // paramTypes = new Class<?>[params.length];
    // for (int i = 2; i != vector.size(); ++i) {
    // final Object aux = vector.get(i).javaObjectValue(context);
    // if (aux instanceof Fact) {
    // System.err.println("ERROR: action called a fact");
    // return null;
    // } else {
    // params[i - 2] = aux;
    // paramTypes[i - 2] = aux.getClass();
    // }
    // }
    // } else {
    // params = new Object[0];
    // paramTypes = new Class<?>[0];
    // }
    // try {
    // // invoque the method from associatedInstance
    // associatedInstance.getClass().getMethod(methodName, paramTypes)
    // .invoke(associatedInstance, params);
    // } catch (final Exception e) {
    // System.err.print("ERROR on Method: " + methodName
    // + " with parameters: ");
    // for (final Object obj : params) {
    // System.err.print(obj.getClass().getSimpleName() + " ; ");
    // }
    // System.err.println();
    // e.printStackTrace();
    // }
    // return null;
    // }
    //
    // @Override
    // public String getName() {
    // return name;
    // }

}
