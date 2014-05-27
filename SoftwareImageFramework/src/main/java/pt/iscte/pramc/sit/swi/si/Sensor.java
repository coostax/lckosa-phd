/**
 *  Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.swi.si;

import pt.iscte.pramc.sit.ext.DataSrcInstance;
import pt.iscte.pramc.sit.ext.cloning.CloneHelper;
import pt.iscte.pramc.sit.swi.SoftwareImage;

/**
 * Represents an agent's singular sensor in the software image Sensors are
 * responsible for acquiring environment data. Sensors in the visual image
 * supply information according to the specified data types.
 * 
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * @version 1.0
 * @since 04/2010
 */
public class Sensor extends DataSource {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private final String descriptor;

    private final String comment;

    private final DataSrcInstance instance;

    private AgentPart root;

    /**
     * Default constructor
     * 
     * @param descriptor
     * @param dataTypeClass
     * @param implementingClass
     */
    public Sensor(String descriptor, String comment, DataSrcInstance instance) {
	super();
	this.descriptor = descriptor;
	this.comment = comment;
	this.instance = instance;
    }

    /**
     * Copy constructor Creates a new sensor with the same characteristics as
     * the one provided
     * 
     * @param toCopy
     *            the sensor to copy
     */
    public Sensor(Sensor toCopy) {
	this.descriptor = new String(toCopy.descriptor);
	this.comment = new String(toCopy.getComment());
	DataSrcInstance inst = null;
	try {
	    inst = (DataSrcInstance) CloneHelper.makeCopyOf(toCopy
		    .getInstance());
	} catch (CloneNotSupportedException e) {
	    SoftwareImage.swiLogger.warn("Could not clone instance "
		    + toCopy.getInstance().getClass().getName()
		    + " for sensor " + this.toString()
		    + ". Making a direct link");
	    inst = toCopy.getInstance();
	}
	this.instance = inst;
    }

    public String getDescriptor() {
	return descriptor;
    }

    public String getComment() {
	return comment;
    }

    /**
     * @return sensor description or ast description
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	if (descriptor.indexOf("#") != -1) {// strip ontology description
	    return descriptor.substring(descriptor.indexOf("#") + 1);
	}
	return descriptor;
    }

    /**
     * Verifies if two sensors are similar. Sensors are similar if they share
     * the same description
     * 
     * @param otherSensor
     *            the other sensor to compare
     * @return true if sensors share the same descriptor
     */
    public boolean isSimilar(Sensor otherSensor) {
	return this.descriptor.equals(otherSensor.getDescriptor());
    }

    /**
     * 
     * @param instance
     * @return true if this sensor makes reference to the supplied instance
     */
    public boolean refersTo(Object instance) {
	if (instance instanceof DataSrcInstance) {
	    return this.instance.equals(instance);
	} else {
	    return false;
	}
    }

    /**
     * @return the associated sensor instance
     */
    public DataSrcInstance getInstance() {
	return instance;
    }

    /**
     * @return the root
     */
    public AgentPart getRoot() {
	return root;
    }

    /**
     * @param root
     *            the root to set
     */
    public void setRoot(AgentPart root) {
	this.root = root;
    }
}
