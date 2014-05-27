/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.domain.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.iscte.pramc.lof.domain.LbOInstance;
import pt.iscte.pramc.sit.swi.di.ActionInstance;
import pt.iscte.pramc.sit.swi.si.Action;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Holds a behaviour for parsing. Behaviours can be represented as lists of actions with no arguments. 
 *        	If arguments depend on the provided conditions they can be only added when conditions are supplied
 * 
 *         Storing behaviours like this improves the comparison between actions
 * 
 * 		A flag is used to identify what type of representation is used
 * 
 * @version 2.1 Holders have the capability to be stored as lists of action instances or lists of actions
 * @since Jul 12, 2011
 */
public class BehaviourHolder {

	private final List<Action> actions;
	
	private final List<ActionInstance> actionInstances;
	
	/**
	 * Creates a behaviour holder from a list of action instances Behaviour
	 * holders only store information on the performed action. The action
	 * instance is created posteriorly
	 * 
	 * @param behaviour the behaviour to hold to
	 * @param asActions a flag indicating if behaviours are stored as actions (true) or as action instances (false)
	 */
	public BehaviourHolder(List<ActionInstance> behaviour,boolean asActions) {
		if(asActions){//store as actions
			List<Action> aux = new ArrayList<Action>();
			// fill action description array
			for (ActionInstance ai : behaviour) {
				aux.add(ai.getAssociatedAction());
			}
			this.actions = Collections.unmodifiableList(aux);
			this.actionInstances = null;// Collections.emptyList();
		}else{//store as action instances
			this.actionInstances = Collections.unmodifiableList(behaviour);
			this.actions = null; //Collections.emptyList();
		}
	}

	/**
	 * Used when storing behaviours as lists of action instances
	 * @return the list of actions stored on this holder
	 */
	public List<ActionInstance> getBehaviour() {
		if(actionInstances != null && !actionInstances.isEmpty()){
			return actionInstances;
		}
		return null;
	}
	
	/**
	 * Used when storing behaviours as lists of actions
	 * @param conditons
	 * @return the list of actions stored on this holder given the proper conditions
	 */
	public List<ActionInstance> getBehaviour(List<LbOInstance> conditions){
		if(actions != null && !actions.isEmpty()){
			return BehaviourTools.buildBehaviourFrom(conditions,((List<Action>)actions));
		}//actions are empty return action instances
		return actionInstances;
	}

	/**
	 * When holding behaviours as actions:
	 * Two behaviours are equal if they share the same actions (arguments apart)
	 * in the same order
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BehaviourHolder) {
			if(!actions.isEmpty()){
				BehaviourHolder other = ((BehaviourHolder) obj);
				if (other.getActions().size() == this.getActions().size()) {
					for (int i = 0; i != getActions().size(); ++i) {
						if (((Action)getActions().get(i)).isSimilar(
								((Action)other.getActions().get(i)))) {
							return true;
						}
					}
				}
					return false;
				}else{//behaviour stored as a set of action instances
					BehaviourHolder other = ((BehaviourHolder) obj);
					if (other.getActions().size() == this.getActions().size()) {
						for (int i = 0; i != getActions().size(); ++i) {
							if (((ActionInstance)getActions().get(i)).isTheSameAs(
									((ActionInstance)other.getActions().get(i)))) {
								return true;
							}
						}
					}
					return false;
				}
			}
		//obj is not a behaviour holder
		return super.equals(obj);
	}

	protected List<?> getActions() {
		return actions;
	}
	
	/**
	 * 
	 * @return A string representation for this behaviour holder
	 */
	public String toStringRepresentation() {
		return "Behaviour: " + actions.toString();
	}

	/**
	 * Builds a BehaviourHolder from a string representation. To build the
	 * string representation use toStringRepresentation()
	 * 
	 * @param rep
	 * @return a new BehaviourHolder from a string representation
	 */
	@Deprecated
	public BehaviourHolder fromStringRepresentation(String rep) {
		return null; //LbOParser.getParser().getFromJSON(rep, BehaviourHolder.class);
	}
}
