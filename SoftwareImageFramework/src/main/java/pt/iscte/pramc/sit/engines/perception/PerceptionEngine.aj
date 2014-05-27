/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.engines.perception;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import pt.iscte.pramc.sit.annotations.VisibleAgentPart;
import pt.iscte.pramc.sit.annotations.VisibleAttribute;
import pt.iscte.pramc.sit.annotations.VisibleSensor;
import pt.iscte.pramc.sit.annotations.VisibleSensorImpl;
import pt.iscte.pramc.sit.ext.DataSrcInstance;
import pt.iscte.pramc.sit.swi.di.Condition;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * The perception engine is responsible for updating the agent's perception whenever a method annotated with VisibleSensor is invoked.
 * 
 * Sensor acquisition methods (annotated with VisibleSensor) explicitly requires that sensor data is stored in a class attribute. The name of this attribute must be specified in the annotated method.
 * 
 * Since version 2.0 of the software image toolkit, sensor instances in the software image point directly to the instance that implements the sensor. This allows a complete discrimination between instances of the same sensor class in the agent's perception.
 *  
 * Perception data is stored along with the agent instance and software image instance, in the AgentRegistry. Location of the correct Agent/SoftwareImage/Perception triplet is made through the Perception instance. 
 *
 * @version 2.0
 * @since Jun 17, 2011
 */
public aspect PerceptionEngine {
	
	/**
	 *  The logger
	 */
	static Logger logger = Logger.getLogger(PerceptionEngine.class);
	
	/**
	 * provide sensor instances with correct interface
	 */
	declare parents: (@VisibleSensorImpl *) implements DataSrcInstance;

	declare parents: (@VisibleAgentPart *) implements DataSrcInstance;

	//default methods for the DataSrcInstance interface

	private Map<String,Condition> DataSrcInstance.associatedCondition = new HashMap<String, Condition>();
	
	/**
	 * Sets the condition for the following field to this data source instance
	 * @param cond the condition to set
	 * @param fieldName the name of the field associated to this condition
	 */
	public void DataSrcInstance.setAssociatedCondition(String fieldName, Condition cond){
		if(this.associatedCondition.containsKey(fieldName)){
			logger.error("The field " + fieldName + " was associated before");
			this.associatedCondition.remove(fieldName);
		}
		this.associatedCondition.put(fieldName,cond); 
	}
	
	/**
	 * @param fieldName the name of the field associated with the condition
	 * @return the condition associated to the supplied field
	 */
	public Condition DataSrcInstance.getAssociatedConditionForField(String fieldName){
		if(this.associatedCondition.containsKey(fieldName)){
			return this.associatedCondition.get(fieldName);
		}
		logger.error("No condition for field " + fieldName);
		return null;
	}

	/**
	 * Execution of any method with the VisibleSensor annotation
	 */
	pointcut sensorData(Object executor, VisibleSensor tag) : execution ( @VisibleSensor * * (..) ) && @annotation(tag) && this(executor);

	pointcut visualAttributeData(Object executor, VisibleAttribute tag) : set(@VisibleAttribute * *) && @annotation(tag) && this(executor);
	
	/**
	 * After the execution of the sensor data method, get the new sensor data from the class field, pointed by the tag
	 * @param executor
	 * @param tag
	 */
	after(Object executor, VisibleSensor tag):sensorData(executor,tag){
		try {
			if(executor instanceof DataSrcInstance){
				boolean found = false;
				// get sensor data from the field with the same name as the argument
				// from @VisibleSensor
				for (Field field : executor.getClass().getDeclaredFields()) {
					if(field.getName().equals(tag.fieldName())) {
						final boolean accessibility = field.isAccessible();
						// force accessibility
						field.setAccessible(true);
						Object data = field.get(executor);
						//update the datasource's condition
						try {
							((DataSrcInstance)executor).getAssociatedConditionForField(field.getName()).setData(data);
							found = true;
						} catch (CloneNotSupportedException e) {
							logger.error("Clone not supported. " + e.getCause() + "; " + e.getMessage());
						}
						//return accessibility
						field.setAccessible(accessibility);
						break;
					}
				}
				if(!found){
					logger.warn("sensor data field " + tag.fieldName() + " was not found in "+executor.getClass().getCanonicalName()+". Perception could not be correctly updated");
				}
			}else{
				logger.error("Executor <"+executor.getClass().getCanonicalName()+"> does not implement DataSrcInstance");
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	after(Object executor, VisibleAttribute tag):visualAttributeData(executor,tag){
		try {
			if(executor instanceof DataSrcInstance){
				boolean found = false;
				// get the visual attribute data from the field with this annotation
				for (Field field : executor.getClass().getDeclaredFields()) {
					if(field.isAnnotationPresent(VisibleAttribute.class) && field.getAnnotation(VisibleAttribute.class).descriptor().equals(tag.descriptor())) {
						final boolean accessibility = field.isAccessible();
						// force accessibility
						field.setAccessible(true);
						Object data = field.get(executor);
						Condition cond = ((DataSrcInstance)executor).getAssociatedConditionForField(field.getName());
						if(cond != null){
							//update the agent's perception
							try {
								cond.setData(data);
								found = true;
							} catch (CloneNotSupportedException e) {
								logger.error("clone not supported. " + e.getCause() + "; " + e.getMessage());
							}
						}
						else{
							logger.error("condition for visual attribute "+executor.getClass().getSimpleName()+":"+tag.descriptor()+" not found");
						}
						//AgentRegistry.getAgentRegistry().storeSensorDataFromSensorInstance(data,executor);
						//return accessibility
						field.setAccessible(accessibility);
						break;
					}
				}
				if(!found){
					logger.warn("visual atrribute " + tag.descriptor() + " was not found in "+executor.getClass().getCanonicalName()+". Perception could not be correctly updated");
				}
			}else{
				logger.error("executor <"+executor.getClass().getCanonicalName()+"> does not implement DataSrcInstance");
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
