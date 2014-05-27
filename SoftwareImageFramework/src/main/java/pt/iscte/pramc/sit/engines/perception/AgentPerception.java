/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.engines.perception;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pt.iscte.pramc.sit.ext.cloning.Copiable;
import pt.iscte.pramc.sit.swi.di.Condition;
import pt.iscte.pramc.sit.swi.si.DataSource;
import pt.iscte.pramc.sit.swi.si.Sensor;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Contains the array of all sensor data and visual attribute data
 *         provided by this agent
 * 
 *         Each instance of agent perception is connected to a single visual
 *         software agent
 * 
 * @version 2.0
 * @since Jun 16, 2011
 */
public class AgentPerception {

    static Logger logger = Logger.getLogger(AgentPerception.class);

    private final List<Condition> perception;

    public AgentPerception() {
	this.perception = new ArrayList<Condition>();
    }

    /**
     * Adds a new data source to the perception
     * 
     * @param source
     *            the datasource to add
     * @param an
     *            identifier for the condition
     */
    public boolean addSource(DataSource source, String fieldName) {
	// validate if this source provides a clonable data
	validateCloneability(source, fieldName);
	// verify if this source already exists
	for (Condition cond : perception) {
	    if (cond.getSource().equals(source)) {
		logger.error("source " + source.toString() + " already exists");
		return false;
	    }
	}
	// create the new condition
	Condition cond = new Condition(source, null);
	// associate condition to datasource instance
	source.getInstance().setAssociatedCondition(fieldName, cond);
	// add this source as a new instance in the perception value
	return perception.add(cond);
    }

    /**
     * Sees if the source provides a cloneable data. On error it writes on the
     * agent's error file
     * 
     * @param source
     * @param fieldName
     */
    private void validateCloneability(DataSource source, String fieldName) {
	try {
	    Field f = source.getInstance().getClass()
		    .getDeclaredField(fieldName);
	    if (f == null) {
		logger.error("[CLONEABLE CHECK] Field " + fieldName + " from "
			+ source.getClass().getSimpleName()
			+ " is not cloneable");
	    } else {
		Class<?> clazz = f.getType();
		Constructor<?> con = null;
		try {
		    con = clazz.getConstructor(clazz);
		} catch (NoSuchMethodException e) {
		}
		if (con != null) {
		    logger.info("[CLONEABLE CHECK] Field " + fieldName
			    + " from "
			    + source.getInstance().getClass().getSimpleName()
			    + " is cloneable");
		} else if (Copiable.class.isAssignableFrom(clazz)
			|| Integer.class.isAssignableFrom(clazz)
			|| Double.class.isAssignableFrom(clazz)
			|| Long.class.isAssignableFrom(clazz)
			|| Float.class.isAssignableFrom(clazz)
			|| Character.class.isAssignableFrom(clazz)
			|| clazz.isPrimitive() || clazz.isEnum()
			|| clazz.isArray()) {
		    logger.info("[CLONEABLE CHECK] Field " + fieldName
			    + " from "
			    + source.getInstance().getClass().getName()
			    + " is cloneable");
		} else {
		    logger.error("[CLONEABLE CHECK] Field " + fieldName
			    + " from "
			    + source.getInstance().getClass().getName()
			    + " is not cloneable");
		}
	    }
	} catch (SecurityException e) {
	    logger.error("[CLONEABLE CHECK] Field " + fieldName + " from "
		    + source.getInstance().getClass().getName()
		    + " is not cloneable. " + e.getCause() + ": "
		    + e.getMessage());
	} catch (NoSuchFieldException e) {
	    logger.error("[CLONEABLE CHECK] Field" + fieldName + " from "
		    + source.getInstance().getClass().getName()
		    + " not found. " + e.getCause() + ": " + e.getMessage());
	}
    }

    /**
     * Provides a condition given a sensor instance
     * 
     * @param instance
     *            the instance of a sensor implementation class
     * @return The condition holder linked to the provided sensor. null if the
     *         sensor is not a source for this perception.
     */
    public Condition getConditionFromSensorInstance(Object instance) {
	for (Condition condition : perception) {
	    if (condition.getSource() instanceof Sensor) { // source must be a
							   // condition
		if (((Sensor) condition.getSource()).refersTo(instance)) {
		    // the sensor representation is linked to this sensor
		    // implementation instance
		    return condition;
		}
	    }
	}
	return null;
    }

    /**
     * @return the list of current conditions to add to a snapshot
     */
    public List<Condition> getCurrentConditions() {
	// prevent null data conditions
	return perception;
    }

    @Override
    public String toString() {
	String res = "[ ";
	for (Condition cond : perception) {
	    if (cond != null) {
		res += cond.toString() + "; ";
	    } else {
		res += "null; ";
	    }
	}
	res += " ]";
	return res;
    }
}
