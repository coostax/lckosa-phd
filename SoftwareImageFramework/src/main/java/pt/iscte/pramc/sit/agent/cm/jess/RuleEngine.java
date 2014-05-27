/**
 * Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.agent.cm.jess;

//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;

//import jess.Filter;
//import jess.JessException;
//import jess.Rete;
//import jess.Userfunction;
//import jess.WorkingMemoryMarker;
//import pt.iscte.pramc.sit.agent.cm.jess.exceptions.RuleNotLoadedException;

/**
 * This object stores the rules that regulate the agent's behaviour. These rules
 * are described in JESS language and make use of the JESS API to interact with
 * the agent's internal mechanisms. It is a helper for managing the JESS rule
 * mechanism
 * 
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * @version 1.0
 * @since 04/2010
 * 
 * @deprecated due to licensing restrictions. Use Jadex Engine
 */
@Deprecated
public class RuleEngine {

    // /**
    // *
    // */
    // private static final long serialVersionUID = 1L;
    //
    // /**
    // * JESS rule engine
    // */
    // private final Rete engine;
    //
    // /**
    // * boolean indicator of loaded rules
    // */
    // private boolean loaded;
    //
    // /**
    // * The working memory marker
    // */
    // private WorkingMemoryMarker marker;
    //
    // /**
    // * Default constructor, receives no arguments
    // */
    // public RuleEngine() {
    // // start rule engine
    // engine = new Rete();
    // loaded = false;
    // }
    //
    // /**
    // * Load rules from a rule file located at the indicated path
    // *
    // * @param path
    // * the location of the rule file
    // * @return
    // */
    // public boolean loadRulesFrom(String path) {
    // // load rules
    // try {
    // engine.reset();
    // engine.batch(path);
    // marker = engine.mark();
    // loaded = true;
    // } catch (JessException ex) {
    // System.err.println("Cannot run rule engine: " + ex.getMessage());
    // loaded = false;
    // }
    // return loaded;
    // }
    //
    // /**
    // * updates the rule engine with the appropriate perception, selects the
    // * proper rule to run and runs that rule
    // *
    // * @param engineData
    // * the data provided to the rule engine (AgentPerception +
    // InternalAttributes)
    // * @throws JessException
    // * @throws RuleNotLoadedException
    // * if rule file is not correctly loaded
    // */
    // public void runRules(List<Object> engineData) throws JessException,
    // RuleNotLoadedException {
    // if (loaded) {
    // // reset engine to initial mark
    // engine.resetToMark(marker);
    // // add data to the rule engine
    // engine.addAll(engineData);
    // // run the rules and find the best one to run
    // engine.run();
    // // TODO: do something with the results??
    // } else {
    // throw new RuleNotLoadedException();
    // }
    // }
    //
    // /**
    // * Retrieves the responses from rule execution
    // *
    // * @throws RuleNotLoadedException
    // * @throws JessException
    // */
    // public List<Object> runRulesWithResponse(Class<?> responseType,
    // List<Object> data) throws JessException, RuleNotLoadedException {
    // runRules(data);
    // List<Object> results = new ArrayList<Object>();
    // Iterator<?> it = engine.getObjects(new Filter.ByClass(responseType));
    // while (it.hasNext()) {
    // results.add(it.next());
    // }
    // return results;
    // }
    //
    // /**
    // * Adds a user-defined function to this engine
    // */
    // public void addUserFunction(Userfunction function) {
    // engine.addUserfunction(function);
    // }
    //
    // /**
    // * Adds data to the rule engine
    // *
    // * @throws RuleNotLoadedException
    // * if rule file is not correctly loaded
    // * @throws JessException
    // * if something is wrong with the added object
    // */
    // public void addDataToEngine(Object data) throws RuleNotLoadedException,
    // JessException {
    // if (loaded) {
    // engine.add(data);
    // } else {
    // throw new RuleNotLoadedException();
    // }
    // }
    //
    // /**
    // * @return the RETE rule engine
    // */
    // public Rete getRuleEngine() {
    // return engine;
    // }
    //
    // public void reset() throws JessException {
    // engine.resetToMark(marker);
    // }

}
