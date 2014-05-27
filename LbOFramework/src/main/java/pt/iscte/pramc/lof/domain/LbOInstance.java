/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.domain;

import org.apache.log4j.Logger;


/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 * Instances represent a specific condition or behavior in the agent's memory
 *
 * @version 0.1 initial version
 * @version 0.5 Generics used to identify instance datatype 
 * @since Nov 18, 2011
 */
public class LbOInstance<T> {

	Logger logger = Logger.getLogger(LbOInstance.class);
	
	private final LbOAttribute<T> attribute;
	
	private final int valuePos;
	
	/**
	 * Creates an instance from an attribute and a  data value for that attribute
	 */
	public LbOInstance(LbOAttribute<T> att, T data){
		this.attribute = att;
		this.valuePos = attribute.addValue(data);
	}
	
	public T getValue(){
		try{
		if(valuePos != -1){
			return this.attribute.getValueAt(valuePos);
		}
		}catch (IndexOutOfBoundsException e) {
			logger.error("Could not get value for attribute " + attribute.getName() + " at position " + valuePos + ". Out of bounds");		
		}
		logger.error("Could not get value for attribute "+attribute.getName()+ "; position is -1");
		return null;
	}

	/**
	 * @return the attribute associated to this instance 
	 */
	public LbOAttribute<T> getAttribute() {
		return attribute;
	}
	
	/**
	 * @return true if this instance represents a condition, false otherwise
	 */
	public boolean isCondition() {
		return this.attribute instanceof ConditionAttribute;
	}
	
	/**
	 * @return true if this instance represents a condition, false otherwise
	 */
	public boolean isBehaviour() {
		return this.attribute instanceof BehaviourAttribute;
	}
	
	/**
	 * @return the position of this value in the attribute list
	 */
	public int getValuePosition(){
		return valuePos;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(attribute.getSimpleName()); 
		sb.append("::");
		Object data = getValue();
		if(data.getClass().isArray()){
			sb.append("{");
			for(Object obj : (Object[])data){
				sb.append(obj.toString());
				sb.append(", ");
			}
			sb.append("}");
		}else{
			sb.append(data.toString());
		}
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof LbOInstance<?>){
			return this.attribute.equals(((LbOInstance) obj).attribute) && this.valuePos == ((LbOInstance) obj).valuePos;
		}
		return super.equals(obj);
	}
	
}
