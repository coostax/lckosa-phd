/**
 *  Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.swi.si;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pt.iscte.pramc.sit.swi.SoftwareImage;

/**
 * Represents an agent part in the software visual image. Agent parts contain
 * sets of sensors and actuators. The static visual image of a visual software
 * agent is the composition of several AgentParts. Agent parts can be made out
 * of internal agent parts
 * 
 * 
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * @version 2.0
 * @since 04/2010
 */
public class AgentPart implements Serializable, APRoot {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * A developer comment for this agent part
     */
    private final String comment;

    /**
     * A link to the static image that roots this agent part
     */
    private APRoot root;

    /**
     * The agent part's sensor array
     */
    private final List<Sensor> sensors;

    /**
     * The agent part's actuator array
     */
    private final List<Actuator> actuators;

    /**
     * The agent part's visual attributes
     */
    private final List<VisualAttribute> visualAttributes;

    /**
     * The agent part's internal parts array
     */
    private final List<AgentPart> internalAgentParts;

    /**
     * Default constructor. Receives the initial data to create an agent part
     * representation
     * 
     * @param sensors
     * @param actuators
     * @param internalAgentParts
     * @param comment
     */
    public AgentPart(String comment) {
	super();
	this.root = null;
	this.comment = comment;
	this.sensors = new ArrayList<Sensor>();
	this.actuators = new ArrayList<Actuator>();
	this.visualAttributes = new ArrayList<VisualAttribute>();
	this.internalAgentParts = new ArrayList<AgentPart>();
    }

    /**
     * Copy constructor Create a new AgentPart with a copy of the attribute from
     * the supplied part
     * 
     * @param toCopy
     *            part to copy from
     */
    public AgentPart(AgentPart toCopy) {
	super();
	// comment
	this.comment = new String(toCopy.getComment());
	// actuators
	this.actuators = new ArrayList<Actuator>(toCopy.getActuators());
	this.sensors = new ArrayList<Sensor>(toCopy.getSensors());
	this.visualAttributes = new ArrayList<VisualAttribute>(
		toCopy.getVisualAttributes());
	this.internalAgentParts = new ArrayList<AgentPart>(
		toCopy.getInternalAgentParts());

    }

    /**
     * Sets the root for this static image
     * 
     * @param root
     */
    public void setRoot(APRoot root) {
	this.root = root;
    }

    /**
     * @return the static image or AgentPart that roots this agent part
     */
    @Override
    public APRoot getRoot() {
	return root;
    }

    public SoftwareImage getAssociatedSoftwareImage() {
	APRoot aux = this.root;
	while (this.root instanceof AgentPart) {
	    aux = ((AgentPart) aux).getRoot();
	}
	// aux is the static image
	return ((StaticImage) aux).getRoot();
    }

    /**
     * Add a new sensor to this part's sensor array Sets the sensor's root
     * 
     * @param s
     */
    public void addSensor(Sensor s) {
	this.sensors.add(s);
	s.setRoot(this);
    }

    /**
     * Add an array of sensors to this part's sensor array
     * 
     * @param sensors
     *            the sensor array to add to this part
     */
    public void addSensorArray(List<Sensor> sensors) {
	for (Sensor s : sensors) {
	    addSensor(s);
	}
    }

    /**
     * Add a new actuator to this part's actuator array Sets the actuator's root
     * 
     * @param s
     */
    public void addActuator(Actuator a) {
	this.actuators.add(a);
	a.setRoot(this);
    }

    /**
     * Adds a new visual attribute to this agent part
     * 
     * @param va
     * @return true if correcly added
     */
    public boolean addVisualAttribute(VisualAttribute va) {
	boolean ok = visualAttributes.add(va);
	if (ok) {
	    va.setRoot(this);
	}
	return ok;
    }

    /**
     * s Add a new internal part to this part's internal part array Sets the
     * internal part root
     * 
     * @param s
     */
    public void addInternalPart(AgentPart p) {
	this.internalAgentParts.add(p);
	p.setRoot(this);
    }

    // ACCESSORS

    /**
     * @return the descriptor
     */
    public String getComment() {
	return comment;
    }

    /**
     * @return the sensors
     */
    public List<Sensor> getSensors() {
	return sensors;
    }

    /**
     * @return the actuators
     */
    public List<Actuator> getActuators() {
	return actuators;
    }

    /**
     * @return the visualAttributes
     */
    public List<VisualAttribute> getVisualAttributes() {
	return visualAttributes;
    }

    /**
     * @return the internalAgentParts
     */
    public List<AgentPart> getInternalAgentParts() {
	return internalAgentParts;
    }

    @Override
    public String toString() {
	StringBuffer text = new StringBuffer();
	// describe visual attributes
	for (VisualAttribute va : getVisualAttributes()) {
	    text.append("  [visualAttribute] ");
	    text.append(va.getComment() + " \n");
	    text.append(va.toString());
	    text.append("\n");
	    text.append("  [/visualAttribute] \n");
	}
	// describe sensors
	for (Sensor sensor : getSensors()) {
	    text.append("  [sensor] ");
	    text.append(sensor.getComment() + " \n");
	    text.append(sensor.toString());
	    text.append("\n");
	    text.append("  [/sensor] \n");
	}
	// describe actuators
	for (Actuator actuator : getActuators()) {
	    text.append("  [actuator] ");
	    text.append(actuator.getComment() + " \n");
	    text.append(actuator.toString());
	    text.append("  [/actuator] \n");
	}
	// describe internal parts
	for (AgentPart part : getInternalAgentParts()) {
	    text.append("  [internalPart] ");
	    text.append(part.getComment() + " \n");
	    text.append(part.toString());
	    text.append("  [/internalPart] \n");
	}
	return text.toString();
    }

    /**
     * Gets a similarity measurement between agent parts.
     * 
     * Two agent parts are 100% similar if:
     * 
     * - they have the same sensors - they have the same actuators - they have
     * the same internal parts
     * 
     * @param otherPart
     *            the other agent's part
     * @return 1.0 if agent parts are 100% similar. A value between 0 and 0.99
     *         if agent parts share some similarities
     */
    public double getSimilarity(AgentPart otherPart) {
	double internals = 0.0;
	double sensors = 0.0;
	double actuators = 0.0;
	double visualatts = 0.0;
	// compare internal parts
	// proceed only if number of internal parts is the same
	if (this.getNumOfInternalParts().equals(
		otherPart.getNumOfInternalParts())) {
	    internals = getInternalPartSimilarities(otherPart);
	}
	// Sensor array
	// proceed only if number of sensors is the same
	if (this.getNumOfSensors().equals(otherPart.getNumOfSensors())) {
	    sensors = getSensorSimilarities(otherPart);
	}
	// Actuator array
	// proceed only if number of actuators is the same
	if (this.getNumOfActuators().equals(otherPart.getNumOfActuators())) {
	    actuators = getActuatorSimilarities(otherPart);
	}
	// Visual attribute array
	// proceed only if number of visual attributes is the same
	if (this.getNumOfVisualAttributes().equals(
		otherPart.getNumOfVisualAttributes())) {
	    visualatts = getVisualAttributeSimilarities(otherPart);
	}
	// similarity measure is the combination of these three
	return (internals + sensors + actuators + visualatts) / 4.0;
    }

    /**
     * Similarity measure for visual attribute arrays. Does not take care if the
     * arrays have the same size
     * 
     * @param otherPart
     * @return
     */
    private double getVisualAttributeSimilarities(AgentPart otherPart) {
	if (this.visualAttributes != null && this.visualAttributes.size() > 0) { // has
										 // visual
										 // attributes
	    double vAtts = 0.0;
	    for (VisualAttribute myVa : this.visualAttributes) {
		INNER: for (VisualAttribute otherVa : otherPart
			.getVisualAttributes()) {
		    if (myVa.isSimilar(otherVa)) {
			vAtts += 1.0;
			break INNER;
		    }
		}
	    }
	    return vAtts / new Double(this.visualAttributes.size());
	} else { // has no visual attributes
	    return 1.0;
	}
    }

    /**
     * Similarity measure for actuator arrays not taking care if they have the
     * same size
     * 
     * @param otherPart
     *            the agent part to compare
     * @return 1.0 if agent actuator arrays are 100% similar. A value between 0
     *         and 0.99 if actuator arrays share some similarities
     */
    private double getActuatorSimilarities(AgentPart otherPart) {
	if (this.actuators != null && this.actuators.size() > 0) { // has
								   // actuators
	    double actuators = 0.0;
	    for (Actuator myActuator : this.actuators) {
		for (Actuator otherActuator : otherPart.getActuators()) {
		    actuators += myActuator.getSimilarity(otherActuator);
		}
	    }
	    return actuators == 0.0 ? 0.0 : (actuators / new Double(
		    getNumOfActuators()));
	} else {
	    return 1.0;
	}
    }

    /**
     * Similarity measure for sensor arrays not taking care if they have the
     * same size
     * 
     * @param otherPart
     *            the agent part to compare
     * @return 1.0 if agent sensor arrays are 100% similar. A value between 0
     *         and 0.99 if sensor arrays share some similarities
     */
    private double getSensorSimilarities(AgentPart otherPart) {
	if (this.sensors != null && this.sensors.size() > 0) { // has sensors
	    double sensors = 0.0;
	    for (Sensor mySensor : this.sensors) {
		INNER: for (Sensor otherSensor : otherPart.getSensors()) {
		    if (mySensor.isSimilar(otherSensor)) {
			sensors += 1.0;
			break INNER;
		    }
		}
	    }
	    return sensors == 0.0 ? 0.0 : (sensors / new Double(
		    this.getNumOfSensors()));
	} else { // has no sensors
	    return 1.0;
	}
    }

    /**
     * Similarity measure for internal part arrays not taking care if they have
     * the same size
     * 
     * @param otherPart
     *            the agent part to compare
     * @return 1.0 if agent internal parts are 100% similar. A value between 0
     *         and 0.99 if agent internal parts share some similarities
     */
    private double getInternalPartSimilarities(AgentPart otherPart) {
	double internals = 0.0;
	if (this.getNumOfInternalParts() > 0) {
	    // both have internal parts, compare them with each other
	    for (AgentPart myInternal : this.internalAgentParts) {
		double maxSimilarity = 0.0;
		for (AgentPart otherInternal : otherPart
			.getInternalAgentParts()) {
		    double val = myInternal.getSimilarity(otherInternal);
		    if (val > maxSimilarity)
			maxSimilarity = val;
		}
		// maxSimilatiry contains the maximum similarity found for
		// myInternal
		internals += maxSimilarity;
	    }
	    // compared all my internal parts with other internal parts
	    // calculate final similarity for internals
	    internals = internals / new Double(this.internalAgentParts.size());
	} else {
	    // both do not have internal parts
	    internals = 1.0;
	}
	return internals;
    }

    /**
     * @return the number of sensors attached to this part
     */
    public Integer getNumOfSensors() {
	return this.sensors != null ? this.sensors.size() : 0;
    }

    /**
     * @return the number of internal parts attached to this part
     */
    public Integer getNumOfInternalParts() {
	return this.internalAgentParts != null ? this.internalAgentParts.size()
		: 0;
    }

    /**
     * @return the number of actuators attached to this part
     */
    public Integer getNumOfActuators() {
	return this.actuators != null ? this.actuators.size() : 0;
    }

    /**
     * @return the number of visual attributes that this part has
     */
    public Integer getNumOfVisualAttributes() {
	return this.visualAttributes != null ? this.visualAttributes.size() : 0;
    }

    /**
     * Provides a simplified representation for the agent's definition from its
     * static image as shown below:
     * 
     * 
     * Agent:<agentUUID>
     * 
     * ################### Part(1) ##############################
     * 
     * Sensors[<sensor 1 description>::<sensor 2 description>...::<sensor n
     * description>]
     * 
     * Actuators: (1): <action 1 description>::<action 2
     * description>::...::<action n description> ... (n): ...
     * 
     * ------------------InternalPart(1.1)-------------------------
     * 
     * -Sensors[<sensor 1 description>::<sensor 2 description>...::<sensor n
     * description>]
     * 
     * -Actuators: (1): <action 1 description>::<action 2
     * description>::...::<action n description> ... (n): ...
     * 
     * -------------------InternalPart(1.1.1)-------------------------
     * 
     * ...
     * 
     * ------------------InternalPart(1.2)-------------------------
     * 
     * ...
     * 
     * ################### Part(n) ##############################
     * 
     * ...
     */
    public String getSimpleRepresentation(String depth) {
	StringBuffer sb = new StringBuffer();
	// sensors
	sb.append("Sensors: ");
	for (Sensor sensor : this.sensors) {
	    sb.append(sensor.getDescriptor());
	    sb.append("::");
	}
	sb.delete(sb.length() - 2, sb.length());
	sb.append("\n");
	// actuators
	sb.append("Actuators:\n");
	int actuatorCount = 0;
	for (Actuator actuator : this.actuators) {
	    sb.append("\t(");
	    sb.append(actuatorCount);
	    sb.append("): ");
	    for (Action action : actuator.getActionSet()) {
		sb.append(action.getDescriptor());
		sb.append("::");
	    }
	    sb.delete(sb.length() - 2, sb.length());
	    sb.append("\n");
	    actuatorCount++;
	}
	// internal parts
	int internalCount = 0;
	for (AgentPart internal : this.internalAgentParts) {
	    sb.append("---------------------- InternalPart(" + depth + "."
		    + internalCount + ") ----------------------\n");
	    sb.append(internal.getSimpleRepresentation(depth + "."
		    + internalCount));
	    sb.append("\n");
	    internalCount++;
	}
	return sb.toString();
    }
}
