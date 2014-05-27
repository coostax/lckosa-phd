/**
 *  Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.swi;

import java.io.Serializable;

import org.apache.log4j.Logger;

import pt.iscte.pramc.sit.agent.VisualSoftwareAgent;
import pt.iscte.pramc.sit.swi.di.DynamicImage;
import pt.iscte.pramc.sit.swi.si.StaticImage;

/**
 * Represents the software body visual image. It contains methods for software
 * image matching and observation. Each software agent has a software image that
 * can be automatically generated by a set of tools in the software image
 * toolkit
 * 
 * 
 * The software image is made by the Static visual image and the Dynamic visual
 * image. The Static visual image is a visual representation of the agent's body
 * parts. Each body part contains a set of sensors and actuators The Dynamic
 * visual image is a visual representation of the agent's behaviors. Besides
 * actions the dynamic image also stores the agent's perceptions before and
 * after the action is executed. Actions are the result of the agent's
 * environment manipulation by its actuators.
 * 
 * Software images need to be accessed outside of the agent's scope, for this
 * reason it needs to be serializable.
 * 
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * @version 1.0
 * @since 04/2010
 */
public final class SoftwareImage implements Serializable {

    /**
     * Logs problems related with software image instances
     */
    public static Logger swiLogger = Logger.getLogger(SoftwareImage.class);

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * This is a unique identifier for the agent that has this visual image
     */
    private final String agentUUID;

    private final VisualSoftwareAgent associatedAgent;

    /**
     * The software image's static element
     */
    private final StaticImage staticImage;
    /**
     * The software image's dynamic element
     */
    private final DynamicImage dynamicImage;

    /**
     * Provides the static image for mathing with other agents.
     */
    public StaticImage getStaticImage() {
	return staticImage;
    }

    /**
     * Allows other agents to make observations on the dynamic image
     * 
     * @return
     */
    public DynamicImage getDynamicImage() {
	return dynamicImage;
    }

    /**
     * @return the agentID
     */
    public String getAgentUUID() {
	return agentUUID;
    }

    /**
     * @return the visual software agent associated to this software image
     */
    public VisualSoftwareAgent getAssociatedAgent() {
	return associatedAgent;
    }

    /**
     * Default constructor
     * 
     * @param agentUUID
     * @param staticVisualImage
     * @param dynamicVisualImage
     */
    public SoftwareImage(VisualSoftwareAgent instance) {
	super();
	this.associatedAgent = instance;
	this.agentUUID = buildAgentUUID();
	this.staticImage = new StaticImage(this);
	this.dynamicImage = new DynamicImage(this);
    }

    /**
     * String constructor. Builds a software image with no association to a
     * agent instance
     * 
     * @param uuid
     */
    public SoftwareImage(String uuid) {
	super();
	this.associatedAgent = null;
	this.agentUUID = uuid;
	this.staticImage = new StaticImage(this);
	this.dynamicImage = new DynamicImage(this);
    }

    /**
     * Copy constructor Creates a copy of the suppplied software image with no
     * agent associated
     * 
     * @param toCopy
     *            the software image to copy
     */
    public SoftwareImage(SoftwareImage toCopy, String newUUID) {
	// agent ID
	this.agentUUID = new String(newUUID);
	this.associatedAgent = null;
	this.staticImage = new StaticImage(toCopy.getStaticImage(), this);
	this.dynamicImage = new DynamicImage(toCopy.getDynamicImage(), this);
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
    private String buildAgentUUID() {
	String result = associatedAgent.getClass().getSimpleName();
	result += "::";
	result += System.currentTimeMillis();
	return result;
    }

    /**
     * Provides a textual representation for this software image
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	StringBuffer text = new StringBuffer();
	text.append("[agent (");
	text.append(getAgentUUID());
	text.append(")] \n");
	text.append(getStaticImage().toString());
	text.append("[dynamic] \n");
	text.append(getDynamicImage().toString());
	text.append("[/dynamic] \n");
	text.append("[/agent]");
	return text.toString();
    }

    /**
     * Gets a similarity measurement between software images. Similarity
     * measurements refer only to static images Two static images are 100%
     * similar if they share the exact same sensors and actuators and if they
     * are organized exactly in the same structure
     * 
     * @param img
     *            the software image to compare
     * @return 1.0 if software images are 100% similar. A value between 0 and
     *         0.99 if software images share some similarities
     */
    public double getSimilarity(SoftwareImage img) {
	return this.staticImage.getSimilarity(img.getStaticImage());
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
	StringBuffer sb = new StringBuffer("Agent:");
	sb.append(agentUUID);
	sb.append("\n");
	sb.append(staticImage.getSimpleRepresentation());
	return sb.toString();
    }

}