/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pt.iscte.pramc.sit.ext.cloning.CloneHelper;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         LbOAttributes hold the representations of the condition or behaviour and
 *         the set of possible data for those
 * 
 * @version 2.1 LbOAttribute is an abstract class with two implementations ConditionAttribute and BehaviourAttribute
 * @version 2.5 Generics used to differentiate datatypes in attributes
 * @since Jul 20, 2011
 */
public abstract class LbOAttribute<T>{

	final Logger logger = Logger.getLogger(this.getClass());

	//public static final String BEHAVIOUR_ATT = "Behaviour";

	private final AttributeType type;
	protected final List<T> data;

	/**
	 * Default constructor, recieves the type of attribute;
	 */
	public LbOAttribute(AttributeType type) {
		this.type = type;
		this.data = new ArrayList<T>();
	}
	
	/**
	* 
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		//TODO: see if this is necessary
		// the attribute equals the condition or behaviour
//		if (obj instanceof DataSource || obj instanceof String) {
//			return type.equals(obj);
//		} else if (obj instanceof Condition) {
//			return ((Condition) obj).equals(type);
//		}
		return super.equals(obj);
	}

	/**
	 * Adds new data to this attribute.
	 * It must verify if data already exists to prevent repetitions
	 * 
	 * @param value the attribute's value
	 * @return the position of the attribute's value, -1 in case of error
	 */
	public abstract int addValue(T value);

	/**
	 * @return the unique name that identifies this attribute
	 */
	public abstract String getName();
	
	/**
	 * @return a simplified version of this attribute's name
	 */
	public abstract String getSimpleName(); 
	
	/**
	 * @return the number of different possible values for this attribute 
	 */
	public int getNumOfPossibleValues(){
		return this.data.size();
	}
	
	/**
	 * @param valuePos the position of this value in the attribute's list of possible values
	 * @return a copy of the value at the indicated position
	 */
	public T getValueAt(int valuePos) {
		final T value = this.data.get(valuePos);
		try {
			return CloneHelper.makeCopyOf(value);
		} catch (CloneNotSupportedException e) {
			logger.warn("Cannot make copy of "+value+". Returning direct value");
			return this.data.get(valuePos);
		}
	}
	
	/**
	 * @param valuePos
	 * @return a string representation of the object at the designated position in JSON format
	 * @deprecated no longer used
	 */
	@Deprecated
	public String getParsedValueAt(int valuePos){
//		final Object obj = this.data.get(valuePos);
//		if(obj != null){
//			return LbOParser.getParser().parseObject(obj);
//		}else{
//			logger.error("could not get value at position " + valuePos + " from attribute " + this.toString());
//			return null;
//		}
		return null;
	}
	
	//TODO: XMLRep??
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getName());
		sb.append("\n");
		for(Object obj : data){
			sb.append(obj);
			sb.append("\n");
		}
		return sb.toString();
	}
	
}
