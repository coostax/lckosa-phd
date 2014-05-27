/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.domain;

import java.util.Arrays;

import pt.iscte.pramc.sit.ext.cloning.CloneHelper;
import pt.iscte.pramc.sit.swi.si.DataSource;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * A condition attribute holds a link to the datasource that produces the data for the conditions along with all the possible values of the data
 *
 * Since Condition data is an Object, ConditionAttribute datatype is Object
 *
 * @version 0.1 - initial version
 * @version 0.5 - Generics used in attribute constitution
 * @since Nov 16, 2011
 */
public class ConditionAttribute extends LbOAttribute<Object>{
	
	/**
	 * A link to the Sensor/VisualAttribute that produces data for this attribute
	 */
	private final  DataSource source;
	
	/**
	 * Default constructor;
	 * Recieves the datasource associated to the condition
	 * @param source
	 */
	public ConditionAttribute(DataSource source){
		super(AttributeType.CONDITION);
		this.source = source;
	}
	
	/**
	 * Adding objects in condition attributes resumes to seeing if the object already exists in the set and providing the position of that object in the set
	 * If objects do not exist then store a copy of them
	 * @see pt.iscte.pramc.lof.domain.LbOAttribute#addValue(java.lang.Object)
	 */
	@Override
	public int addValue(Object value) {
		//special 
		if(value.getClass().isArray()){
			for(Object obj : data){
				if(obj.getClass().isArray() && Arrays.equals((Object[])value,(Object[])obj)){
					return data.indexOf(obj);
				}
			}
		}
		if(data.contains(value)){
			//the value exists return its position on the list
			return data.indexOf(value);
		}
		//the value does not exist, add a copy of it and return the last position in the list
		try {
			Object copy = CloneHelper.makeCopyOf(value);
			data.add(copy);
		} catch (CloneNotSupportedException e) {
			logger.warn("Could not make a copy of "+value+" for " + getName() + ". Not cloneable. Storing original...");
			data.add(value);
		}
		return data.size()-1;

//		if(value instanceof Copiable){
//			try {
//				data.add(((Copiable<?>)value).copy());
//				return data.size()-1;
//			} catch (CloneNotSupportedException e) {
//				logger.warn("Could not make a copy of "+value+" for " + getName() + ". Not cloneable. Storing original...");
//				data.add(value);
//				return data.size()-1;
//			}
//		}
//		//find a copy constructor
//		try {
//			Constructor<?> c = value.getClass().getConstructor(value.getClass());
//			if(c != null){
//				Object inst = c.newInstance(value);
//				if(inst != null){
//					data.add(inst);
//					return data.size()-1;
//				}
//			}
//			//if cannot add through copy constructor
//			logger.warn("could not find copy constructor. Storing original...");
//			data.add(value);
//			return data.size()-1;
//		} catch (Exception e) {
//			logger.warn("could not copy instance though copy constructor. "+e.getMessage()+" Storing original...");
//			data.add(value);
//			return data.size()-1;
//		}
	}
	
	/**
	 * @see pt.iscte.pramc.lof.domain.LbOAttribute#getName()
	 */
	@Override
	public String getName() {
		return "Condition<"+source.getDescriptor()+">";
	}

	/**
	 * @see pt.iscte.pramc.lof.domain.LbOAttribute#getSimpleName()
	 */
	@Override
	public String getSimpleName() {
		String src = source.getDescriptor();
		if(src.indexOf("#") != -1){//strip ontology description
			return "Condition<"+src.substring(src.indexOf("#")+1)+">";
		}else{
			return getName();
		}
	}
	
	/**
	 * Two sources are the same if they have the same descriptor
	 * @param ds
	 * @return true if the source is the provided one
	 */
	public boolean isSource(DataSource ds) {
		if(source.getDescriptor().equals(ds.getDescriptor())){
			return true;
		}
		return false;
	}
	
	/**
	 * @return the data source associated to this condition attribute
	 */
	public DataSource getSource() {
		return source;
	}
	
}
