/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.ext;

import java.io.Serializable;

import pt.iscte.pramc.sit.swi.di.Condition;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Interface representation of a data source instance.
 * 
 *         Relates a static image sensor with the agent sensor
 * 
 * @version 2.0
 * @since Jul 5, 2011
 */
public interface DataSrcInstance extends Serializable {

    /**
     * Sets the condition space for this data source instance
     * 
     * @param cond
     *            the condition to set
     * @param fieldName
     *            the name of the field associated to this condition
     */
    public void setAssociatedCondition(String fieldName, Condition cond);

    /**
     * @param fieldName
     *            the name of the field associated to the condition
     * @return the associated condition
     */
    public Condition getAssociatedConditionForField(String fieldName);

}
