/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.domain;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.pramc.sit.swi.di.ActionInstance;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * A behaviour attribute represents the agent's behaviour and contains all the possible behaviours the agent can perform
 *
 * Behaviour data is represented as a list of ActionInstances
 * 
 * Action instance data storage is  
 *
 * @version 0.1 - initial version
 * @version 0.5 - Generics used in attribute constitution
 * @since Nov 16, 2011
 */
public class BehaviourAttribute extends LbOAttribute<List<ActionInstance>>{
	
	private final boolean storeInstanceData;
	
	/**
	 * Default constructor;
	 * Creates a behaviour attribute with instance data storage
	 * @param source
	 */
	public BehaviourAttribute(){
		this(true);
	}
	
	/**
	 * Creates a behaviour attribute with optional data storage
	 * @param storageFlag
	 */
	public BehaviourAttribute(boolean storageFlag){
		super(AttributeType.BEHAVIOUR);
		this.storeInstanceData = storageFlag;
	}
	
	/**
	 * when adding objects in behaviour attributes it is necessary to see if the behaviour has been observed before
	 * When storing action instance data option is turned off a new action instance needs to be created
	 * @see pt.iscte.pramc.lof.domain.LbOAttribute#addValue(java.lang.Object)
	 */
	@Override
	public int addValue(final List<ActionInstance> value) {
		final List<ActionInstance> toStore;
		if(storeInstanceData){
			toStore = value;
		}else{//create a new action instance list without instance data
			toStore = buildWithoutInstanceDataFrom(value);
		}
		// see if same behaviour has been observed before and get its position in the list
		int pos = getBehaviour(toStore);
		if (pos == -1) {//behaviour not observed: add a copy of it
			data.add(new ArrayList<ActionInstance>(toStore));
			return data.size()-1;
		}
		return pos;
	}
	
	/**
	 * @return a copy of the list of actions stored at the specified position
	 */
	@Override
	public List<ActionInstance> getValueAt(int valuePos) {
		return new ArrayList<ActionInstance>(data.get(valuePos));
	}
	
	/**
	 * Builds a new behaviour without instance data
	 * @param value
	 * @return
	 */
	private List<ActionInstance> buildWithoutInstanceDataFrom(
			final List<ActionInstance> value) {
		List<ActionInstance> result =  new ArrayList<ActionInstance>();
		for(ActionInstance instance : value){
			result.add(new ActionInstance(instance.getAssociatedAction()));
		}
		return result;
	}

	/**
	 * Sees if the provided behaviour has been observed before
	 * 
	 *  Two behaviours are the same if the the same actions / action instances are performed in the same order
	 * 
	 * @param behavior
	 * @return the position of this behaviour in the attribute's list of values, -1 if behaviour is not found
	 */
	private int getBehaviour(List<ActionInstance> behavior) {
		// search for all list
//		boolean res = false;
		for (int index = 0; index !=  data.size(); ++index) {
			List<ActionInstance> bhAtIndex = data.get(index);
			if(bhAtIndex.equals(behavior)){
				return index;
			}
//			if (bhAtIndex.size() == behavior.size()) {
//				INNER: for (int i = 0; i != behavior.size(); ++i) {
//					if (bhAtIndex.get(i).equals(behavior.get(i))) {
//						res = true;
//					} else {
//						res = false;
//						break INNER;
//					}
//				}
//			if (res) {
//				return index;
//			}
//			}
//			} else {//data is not storing lists
//				logger.error("Data does not correspond to behaviour data. Expecting List<>, obtained " + bhAtIndex.getClass().getName());
//				return -1;
//			}
		}
		return -1;
	}
	
	/**
	 * @see pt.iscte.pramc.lof.domain.LbOAttribute#getName()
	 */
	@Override
	public String getName() {
		return "Behaviour: ";
	}
	
	/**
	 * @see pt.iscte.pramc.lof.domain.LbOAttribute#getSimpleName()
	 */
	@Override
	public String getSimpleName() {
		return getName();
	}
	
}
