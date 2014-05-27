/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.swi.si;

import pt.iscte.pramc.sit.ext.DataSrcInstance;
import pt.iscte.pramc.sit.ext.cloning.CloneHelper;
import pt.iscte.pramc.sit.swi.SoftwareImage;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Representation of an agent's visual attribute in the software image.
 * 
 * 
 * @version 2.0
 * @since Jun 16, 2011
 */
public class VisualAttribute extends DataSource {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private final String descriptor;

    private final String comment;

    private final DataSrcInstance instance;

    private final String attributeName;

    private AgentPart root;

    /**
     * Default constructor. Creates a new visual attribute representation
     * 
     * @param descriptor
     * @param comment
     * @param instance
     */
    public VisualAttribute(String descriptor, String comment,
	    DataSrcInstance classInstance, String attributeName) {
	super();
	this.descriptor = descriptor;
	this.comment = comment;
	this.instance = classInstance;
	this.attributeName = attributeName;
    }

    /**
     * Creates a copy of this visual attribute
     * 
     * @param toCopy
     */
    public VisualAttribute(VisualAttribute toCopy) {
	this.descriptor = new String(toCopy.getDescriptor());
	this.comment = new String(toCopy.getComment());
	DataSrcInstance inst = null;
	try {
	    inst = (DataSrcInstance) CloneHelper.makeCopyOf(toCopy
		    .getInstance());
	} catch (CloneNotSupportedException e) {
	    SoftwareImage.swiLogger.warn("Could not clone instance "
		    + toCopy.getInstance().getClass().getName()
		    + " for visual attribute " + this.toString()
		    + ". Making a direct link");
	    inst = toCopy.getInstance();
	}
	this.instance = inst;
	this.attributeName = new String(toCopy.getAttributeName());
    }

    /**
     * @return the descriptor
     */
    public String getDescriptor() {
	return descriptor;
    }

    /**
     * @return the comment
     */
    public String getComment() {
	return comment;
    }

    /**
     * @return the attribute's name
     */
    public String getAttributeName() {
	return attributeName;
    }

    /**
     * @param instance
     *            the instance to compare
     * @return true if this visual attribute references the supplied instance
     */
    public boolean refersTo(Object classInstance, String fieldName) {
	return this.instance.equals(classInstance)
		&& this.attributeName.equals(fieldName);
    }

    /**
     * Similarity indicator for visual attributes. Visual attributes are similar
     * if they have the same description
     * 
     * @param otherVa
     *            the other visual attribute to calculate the similarity
     * @return true if attributes share the same descriptor, false if not
     */
    public boolean isSimilar(VisualAttribute otherVa) {
	if (otherVa != null && otherVa.getDescriptor().equals(this.descriptor)) {
	    return true;
	}
	return false;
    }

    @Override
    public String toString() {
	if (descriptor.indexOf("#") != -1) {// strip ontology description
	    return descriptor.substring(descriptor.indexOf("#") + 1);
	}
	return descriptor;
    }

    @Override
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
