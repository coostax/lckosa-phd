/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.engines.builder;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import pt.iscte.pramc.sit.agent.VisualSoftwareAgent;
import pt.iscte.pramc.sit.annotations.VisibleAction;
import pt.iscte.pramc.sit.annotations.VisibleActuator;
import pt.iscte.pramc.sit.annotations.VisibleAgentPart;
import pt.iscte.pramc.sit.annotations.VisibleAttribute;
import pt.iscte.pramc.sit.annotations.VisibleSensor;
import pt.iscte.pramc.sit.annotations.VisibleSensorImpl;
import pt.iscte.pramc.sit.engines.perception.AgentPerception;
import pt.iscte.pramc.sit.ext.ActuatorInstance;
import pt.iscte.pramc.sit.ext.DataSrcInstance;
import pt.iscte.pramc.sit.swi.SoftwareImage;
import pt.iscte.pramc.sit.swi.si.Action;
import pt.iscte.pramc.sit.swi.si.Actuator;
import pt.iscte.pramc.sit.swi.si.AgentPart;
import pt.iscte.pramc.sit.swi.si.Sensor;
import pt.iscte.pramc.sit.swi.si.VisualAttribute;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Holds the relation between the software image the visual software
 *         agent and its perception
 * 
 *         Contains the methods for initializing the agent's software image
 * 
 *         The static aspects of the software image are build through agent code
 *         introspection. The following rules must be followed to provide a
 *         correct identification of all agent aspects: - The agent main class
 *         must be annotated with VisibleAgent - The agent part main classes
 *         must be declared on the main agent class as: - singular attributes -
 *         A collection of attributes such as a List or an Array
 * 
 *         Static image building procceeds as follows: - Locating agent parts: -
 *         The agent main class attributes are introspected to find those whose
 *         classes are annotated with VisibleAgentPart
 * 
 * @version 2.0
 * @since Jun 16, 2011
 */
public class BuilderTools {

    public final static String SENSOR_INIT = "S";
    public final static String ACTION_INIT = "A";
    public final static String VA_INIT = "V";
    public final static String DIVIDER = ":";

    private final VisualSoftwareAgent agent;
    private final AgentPerception perception;
    private final AgentActions actions;

    /**
     * default constructor, receives a visual software agent and initializes its
     * software image
     * 
     * @param agent
     *            the agent whose software image will be built and later
     *            associated to
     */
    public BuilderTools(VisualSoftwareAgent agent) {
	super();
	this.agent = agent;
	this.perception = agent.getAgentPerception();
	this.actions = agent.getAgentActions();
    }

    /**
     * Builds a software image for the stored agent
     */
    public boolean buildSoftwareImage() {
	SoftwareImage swi = agent.getSoftwareImage();
	// build static image
	// see if agent class is also an agent part
	if (agent.getClass().isAnnotationPresent(VisibleAgentPart.class)) {
	    addAgentPartToSwI(swi, buildAgentPartFromObj(agent));
	}
	// get agent parts from fields
	for (Field field : agent.getClass().getDeclaredFields()) {
	    if (field.getType().isAnnotationPresent(VisibleAgentPart.class)) {
		// a simple attribute
		try {
		    boolean access = field.isAccessible();
		    if (!field.isAccessible()) {
			field.setAccessible(true);
		    }
		    addAgentPartToSwI(swi,
			    buildAgentPartFromObj(getField(field, agent)));
		    field.setAccessible(access);
		} catch (Exception e) {
		    e.printStackTrace();
		    return false;
		}
	    } else if (field.getType().isArray()) {
		// an array of fields. see if the instances of this array are
		// agent parts
		try {
		    // get field array
		    Object obj = getField(field, agent);
		    if (obj != null) {
			for (int i = 0; i != Array.getLength(obj); ++i) {
			    if (Array
				    .get(obj, i)
				    .getClass()
				    .isAnnotationPresent(VisibleAgentPart.class)) {
				addAgentPartToSwI(
					swi,
					buildAgentPartFromObj(Array.get(obj, i)));
			    }
			}
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		    return false;
		}
	    } else { // other options, List of objects...
		try {
		    Object obj = getField(field, agent);
		    if (obj instanceof List<?>) {
			for (Object insideList : ((List<?>) obj)) {
			    addAgentPartToSwI(swi,
				    buildAgentPartFromObj(insideList));
			}
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		    return false;
		}
	    }
	}
	return true;
    }

    /**
     * Adds the following agent part to the supplied software image
     * 
     * @param swi
     * @param ap
     */
    private void addAgentPartToSwI(SoftwareImage swi, AgentPart ap) {
	if (ap != null) {
	    // add part to static image
	    if (swi.getStaticImage().getAgentParts().contains(ap)) {
		System.err.println("ERROR (BuildTools): the agent part:"
			+ ap.toString() + " already exists");
	    } else {
		swi.getStaticImage().addAgentPart(ap);
	    }
	}
    }

    /**
     * Ensures field access
     * 
     * @param field
     * @param obj
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private Object getField(Field field, Object obj)
	    throws IllegalArgumentException, IllegalAccessException {
	boolean access = field.isAccessible();
	if (!field.isAccessible()) {
	    field.setAccessible(true);
	}
	Object ret = field.get(obj);
	field.setAccessible(access);
	return ret;
    }

    /**
     * Adds a new agent part to the supplied static image
     * 
     * @param apInstance
     *            the AgentPart implementation class
     * @param si
     *            the static image representation of the agent
     * @return true if agent part correctly added, false if not
     */
    private AgentPart buildAgentPartFromObj(Object apInstance) {
	VisibleAgentPart tag = apInstance.getClass().getAnnotation(
		VisibleAgentPart.class);
	AgentPart ap = new AgentPart(tag != null ? tag.value() : "");
	// look for sensors and actions declared on part instance methods
	for (Method method : apInstance.getClass().getDeclaredMethods()) {
	    if (method.isAnnotationPresent(VisibleSensor.class)) {
		if (apInstance instanceof DataSrcInstance) {
		    // get annotation
		    VisibleSensor stag = method
			    .getAnnotation(VisibleSensor.class);
		    // create a new sensor
		    Sensor sensor = new Sensor(stag.descriptor(),
			    stag.comment(), (DataSrcInstance) apInstance);
		    // add sensor to this agent part
		    ap.addSensor(sensor);
		    // add sensor source to this agent
		    perception.addSource(sensor, stag.fieldName());
		} else {
		    System.err.println("ERROR (VSAHolder): sensor instance "
			    + apInstance.getClass()
			    + " is not a data source instance");
		}
	    } else if (method.isAnnotationPresent(VisibleAction.class)) {
		// get annotation
		VisibleAction atag = method.getAnnotation(VisibleAction.class);
		// in this case the actuator is the agent itself, verify if
		// actuator is already instantiated
		Actuator act = null;
		for (Actuator toCompare : ap.getActuators()) {
		    if (toCompare.isImplementedBy(apInstance)) {
			act = toCompare;
			break;
		    }
		}
		if (act == null) {
		    if (apInstance instanceof ActuatorInstance) {
			// no actuator has been created, create new one
			act = new Actuator(tag != null ? tag.value() : "",
				(ActuatorInstance) apInstance);
			ap.addActuator(act);
		    } else {
			System.err
				.println("ERROR(BuildTools): Actuator implementation class "
					+ apInstance.getClass()
						.getCanonicalName()
					+ " does not implement ActuatorInstance");
		    }
		}
		//
		final Action vAction;
		// create action with parameters and add it to this actuator
		if (atag.params() != null && atag.params().length != 0) {
		    // retrieve parameters
		    List<String> params = new ArrayList<String>();
		    for (String val : atag.params()) {
			params.add(val);
		    }
		    vAction = new Action(atag.descriptor(), atag.comment(),
			    params);
		} else {
		    vAction = new Action(atag.descriptor(), atag.comment());
		}
		// add the created action to the actuator
		act.addAction(vAction);
		// add a new association to AgentActions
		actions.associate(vAction, method, apInstance);
	    }
	}
	// look for sensors, actuators, visual attributes and internal parts in
	// class fields
	for (Field field : apInstance.getClass().getDeclaredFields()) {
	    if (field.isAnnotationPresent(VisibleAttribute.class)) {
		// this field is a visual attribute
		// get annotation
		VisibleAttribute vaTag = field
			.getAnnotation(VisibleAttribute.class);
		try {
		    if (apInstance instanceof DataSrcInstance) {
			VisualAttribute va = new VisualAttribute(
				vaTag.descriptor(), vaTag.comment(),
				(DataSrcInstance) apInstance, field.getName());
			// add visual attribute to this agent part
			ap.addVisualAttribute(va);
			// add the visual attribute as a source
			perception.addSource(va, field.getName());
		    } else {
			System.err
				.println("ERROR(BuildTools): visual attribute instance "
					+ apInstance.getClass()
					+ " is not a data source instance");
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    } else {
		// find actuators, sensor implementations and internal parts
		try {
		    // get object referenced by the field
		    Object obj = getField(field, apInstance);
		    if (obj != null) {
			if (field.getType().isArray()) {// field is an array
			    for (int i = 0; i != Array.getLength(obj); ++i) {
				processAttributeInstance(Array.get(obj, i), ap);
			    }
			} else if (obj instanceof List<?>) {// field is a list
			    for (Object listObj : ((List<?>) obj)) {
				processAttributeInstance(listObj, ap);
			    }
			} else {
			    processAttributeInstance(obj, ap);
			}
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}
	return ap;
    }

    /**
     * Gathers information from agent part instance field: - look for
     * VisibleSensorImpl class annotations and add sensor to the agent part -
     * look for VisibleActuator class annotations and add actuator to the agent
     * part - look for VisibleAgentPart class annotations and add actuator to
     * the agent part
     * 
     * @param object
     * @param ap
     */
    private void processAttributeInstance(Object object, AgentPart ap) {
	if (object.getClass().isAnnotationPresent(VisibleSensorImpl.class)) {// Sensor
	    boolean found = false;
	    // find sensor implementation method
	    for (Method method : object.getClass().getDeclaredMethods()) {
		if (method.isAnnotationPresent(VisibleSensor.class)) {
		    if (object instanceof DataSrcInstance) {
			// prepare annotation data
			VisibleSensor sTag = method
				.getAnnotation(VisibleSensor.class);
			// create new sensor
			Sensor sensor = new Sensor(sTag.descriptor(),
				sTag.comment(), (DataSrcInstance) object);
			// add it to the agent part
			ap.addSensor(sensor);
			// add source to perception
			perception.addSource(sensor, sTag.fieldName());
			found = true;
		    } else {
			System.err
				.println("ERROR (BuildTools): sensor instance "
					+ object.getClass()
					+ " is not a data source instance");
		    }
		    break;
		}
	    }
	    if (!found) {
		System.err
			.println("ERROR (BuildTools): Sensor implementation class "
				+ object.getClass().getCanonicalName()
				+ " does not have an annotated sensor method");
	    }
	} else if (object.getClass().isAnnotationPresent(VisibleActuator.class)) {// Actuator
	    if (object instanceof ActuatorInstance) {
		// create actuator
		Actuator actuator = new Actuator(object.getClass()
			.getAnnotation(VisibleActuator.class).value(),
			(ActuatorInstance) object);
		boolean found = false;
		// find action methods
		for (Method method : object.getClass().getDeclaredMethods()) {
		    if (method.isAnnotationPresent(VisibleAction.class)) {
			// prepare annotation data
			VisibleAction aTag = method
				.getAnnotation(VisibleAction.class);
			// create new action and add it to the actuator
			final Action vAction;
			// create action with parameters and add it to this
			// actuator
			if (aTag.params() != null && aTag.params().length == 0) {
			    // retrieve parameters
			    List<String> params = new ArrayList<String>();
			    for (String val : aTag.params()) {
				params.add(val);
			    }
			    vAction = new Action(aTag.descriptor(),
				    aTag.comment(), params);
			} else {
			    vAction = new Action(aTag.descriptor(),
				    aTag.comment());
			}
			// add action to actuator
			actuator.addAction(vAction);
			// associate to action list
			actions.associate(vAction, method, object);
			found = true;
		    }
		}
		if (!found) {
		    System.err
			    .println("ERROR(BuildTools): Actuator implementation class "
				    + object.getClass().getCanonicalName()
				    + " does not have an action associated to it");
		} else {
		    ap.addActuator(actuator);
		}
	    } else {
		System.err
			.println("ERROR(BuildTools): Actuator implementation class "
				+ object.getClass().getCanonicalName()
				+ " does not implement ActuatorInstance");
	    }
	} else if (object.getClass()
		.isAnnotationPresent(VisibleAgentPart.class)) {// internal parts
	    ap.addInternalPart(buildAgentPartFromObj(object));
	}
    }

    /**
     * Builds an automatic agent UUID for the software image
     * 
     * @param type
     *            the agent class type
     * @param tag
     *            the tag associated to this agent
     * @return
     */
    // public String buildAgentUUIDFrom(Class<?> type){
    // VisibleAgent anon = type.getAnnotation(VisibleAgent.class);
    // String result = type.getSimpleName()+":";
    // result += (anon != null ? anon.value() : "") +":";
    // result += System.currentTimeMillis();
    // return result;
    // }

    /**
     * Two holders are equal if the agents they hold are equal
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (obj instanceof BuilderTools) {
	    return ((BuilderTools) obj).agent.equals(this.agent);
	} else {
	    return super.equals(obj);
	}
    }
}
