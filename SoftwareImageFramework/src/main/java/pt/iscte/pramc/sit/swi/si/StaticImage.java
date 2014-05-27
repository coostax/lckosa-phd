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
 * The Static visual image is a visual representation of the agent's body parts.
 * Body parts contain sets of sensors, actuators and other internal body parts
 * 
 * The static visual image is associated with the root software image by the
 * root element
 * 
 * @author Paulo Costa (paulo.costa@gmail.com)
 * @version 1.0
 * @since 04/2010
 */
public final class StaticImage implements Serializable, APRoot {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * Agent part list
     */
    private final List<AgentPart> agentParts;

    /**
     * The root software image for this static image
     */
    private final SoftwareImage root;

    /**
     * Default constructor
     * 
     * @param agentParts
     */
    public StaticImage(SoftwareImage root) {
	super();
	this.root = root;
	this.agentParts = new ArrayList<AgentPart>();
    }

    /**
     * Copy constructor Creates a new static image as a copy of the supplied
     * static image
     * 
     * @param toCopy
     */
    public StaticImage(StaticImage toCopy, SoftwareImage root) {
	this(root);
	// copy agent parts
	for (AgentPart ap : toCopy.getAgentParts()) {
	    addAgentPart(new AgentPart(ap));
	}
    }

    /**
     * @return the agentParts
     */
    public List<AgentPart> getAgentParts() {
	return agentParts;
    }

    /**
     * Adds a new agent part to this software image Sets the agent part's root
     * as this
     * 
     * @param part
     */
    public void addAgentPart(AgentPart part) {
	this.agentParts.add(part);
	part.setRoot(this);
    }

    /**
     * @see pt.iscte.pramc.sit.swi.si.APRoot#getRoot()
     */
    @SuppressWarnings("unchecked")
    @Override
    public SoftwareImage getRoot() {
	return root;
    }

    @Override
    public String toString() {
	StringBuffer text = new StringBuffer();
	text.append("[static] \n");
	int count = 1;
	for (AgentPart ap : getAgentParts()) {
	    text.append(" [part (" + count + ")] ");
	    text.append(ap.getComment() + " \n");
	    text.append(ap.toString());
	    text.append(" [/part] \n");
	    count++;
	}
	text.append("[/static] \n");
	return text.toString();
    }

    /**
     * Gets a similarity measurement between static images. Two static images
     * are 100% similar if they share the exact same sensors and actuators and
     * if they are organized exactly in the same structure
     * 
     * @param si
     *            the software image to compare
     * @return 1.0 if static images are 100% similar. A value between 0 and 0.99
     *         if static images share some similarities
     */
    public double getSimilarity(StaticImage si) {
	// check if both agents have the same number of parts
	if (this.getNumOfAgentParts().equals(si.getNumOfAgentParts())) {
	    // compare each part with each one of the other image's parts
	    return getPartSimilarity(si);
	}
	return 0.0;
    }

    /**
     * Calculates the similarity between agent part arrays not taking care if
     * those arrays have different sizes
     * 
     * @param si
     *            the other static image to compare
     * @return 1.0 if agent part arrays are 100% similar. A value between 0 and
     *         0.99 if agent part arrays share some similarities
     */
    private double getPartSimilarity(StaticImage si) {
	double similarity = 0.0;
	for (AgentPart myPart : agentParts) {
	    double bestSimilarity = 0.0;
	    for (AgentPart otherPart : si.getAgentParts()) {
		double result = myPart.getSimilarity(otherPart);
		if (result > bestSimilarity) {
		    bestSimilarity = result;
		}
	    }
	    similarity += bestSimilarity;
	}
	return similarity == 0.0 ? 0.0 : (similarity / new Double(
		getNumOfAgentParts()));
    }

    /**
     * @return the number of agent parts this static image has
     */
    public Integer getNumOfAgentParts() {
	return agentParts != null ? agentParts.size() : 0;
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
     * Actuators[(Actuator 1): <action 1 description>::<action 2
     * description>::...::<action n description> ; ... ; (Actuator n): ...]
     * 
     * ------------------InternalPart(1.1)-------------------------
     * 
     * -Sensors[<sensor 1 description>::<sensor 2 description>...::<sensor n
     * description>]
     * 
     * -Actuators[(Actuator 1): <action 1 description>::<action 2
     * description>::...::<action n description> ; ... ; (Actuator n): ...]
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
    public String getSimpleRepresentation() {
	StringBuffer sb = new StringBuffer();
	int counter = 0;
	for (AgentPart part : this.agentParts) {
	    sb.append("################### Part(" + counter
		    + ") ##############################\n");
	    sb.append(part.getSimpleRepresentation("" + counter));
	    counter++;
	}
	return sb.toString();
    }
}
