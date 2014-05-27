/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.swi.di;

import java.io.Serializable;

import org.apache.log4j.Logger;

import pt.iscte.pramc.sit.ext.NoData;
import pt.iscte.pramc.sit.ext.cloning.CloneHelper;
import pt.iscte.pramc.sit.ext.cloning.Copiable;
import pt.iscte.pramc.sit.swi.si.DataSource;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Represents a condition for the condition-action cases
 * 
 *         A condition is identified by: - a source, the sensor or visual
 *         attribute where the information comes from - an object representing
 *         the data provided by the source
 * 
 * @version 2.0 added cloning support to prevent changes on the agent's data
 * @since Jun 16, 2011
 */
public class Condition implements Serializable, Copiable<Condition> {

    static Logger logger = Logger.getLogger(Condition.class);

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * The source of this data
     */
    private final DataSource source;

    /**
     * The data itself
     */
    private Object data;

    /**
     * Default constructor. Assumes the passed data has no relations to the
     * agent's inner workings
     * 
     * @param source
     * @param data
     */
    public Condition(DataSource origin, Object data) {
	super();
	this.source = origin;
	this.data = data;
    }

    /**
     * Copy constructor. Creates a new copy of the condition
     * 
     * @param toCopy
     */
    public Condition(Condition toCopy) {
	DataSource newSrc;
	try {
	    newSrc = (DataSource) CloneHelper.makeCopyOf(toCopy.getSource());
	} catch (CloneNotSupportedException e) {
	    logger.error("Source " + toCopy.getSource().getDescriptor()
		    + " could not be cloned. Making a direct copy");
	    newSrc = toCopy.getSource();
	}
	this.source = newSrc;
	try {
	    setData(toCopy.getData());
	} catch (CloneNotSupportedException e) {
	    logger.error("Could not clone condition " + toCopy.toString()
		    + ". Creating a condition with no data");
	}
    }

    /**
     * @return the sensor source
     */
    public DataSource getSource() {
	return source;
    }

    /**
     * @return the data
     */
    public Object getData() {
	if (data == null) {
	    return new NoData();
	}
	return data;
    }

    /**
     * Prevents null data with a special noData object Stores the copy of the
     * supplied data
     * 
     * @throws CloneNotSupportedException
     */
    public void setData(Object data) throws CloneNotSupportedException {
	if (data != null) {
	    this.data = CloneHelper.makeCopyOf(data);
	} else {
	    this.data = new NoData();
	}
    }

    @Override
    public String toString() {
	StringBuffer sb = new StringBuffer(source.getClass().getSimpleName());
	sb.append("(");
	String src = source.getDescriptor();
	if (src.indexOf("#") != -1) {// strip ontology description
	    sb.append(src.substring(src.indexOf("#") + 1));
	} else {
	    sb.append(src);
	}
	sb.append(")::");
	if (data != null) {
	    if (data.getClass().isArray()) {
		sb.append("{");
		for (Object obj : (Object[]) data) {
		    sb.append(obj.toString());
		    sb.append(", ");
		}
		sb.append("}");
	    } else {
		sb.append(data.toString());
	    }
	} else {
	    sb.append("NO DATA");
	}
	return sb.toString();
    }

    /**
     * Creates a copy of this condition
     */
    @Override
    public Condition copy() throws CloneNotSupportedException {
	return new Condition(this);
    }
}
