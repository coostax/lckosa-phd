/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.ext;

import pt.iscte.pramc.sit.ext.cloning.Copiable;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Object representation of a null sensor data
 * 
 * @version 2.0
 * @since Jun 17, 2011
 */
public class NoData implements Copiable {

    @Override
    public String toString() {
	return "EMPTY";
    }

    @Override
    public Object copy() {
	return new NoData();
    }

    /**
     * All no data objects are equal
     */
    @Override
    public boolean equals(Object obj) {
	if (obj instanceof NoData) {
	    return true;
	}
	return super.equals(obj);
    }
}
