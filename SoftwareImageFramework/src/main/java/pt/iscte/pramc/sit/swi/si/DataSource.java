/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.swi.si;

import pt.iscte.pramc.sit.ext.DataSrcInstance;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Interface representation of a data source. Data sources are useful to
 *         build the agent's perception
 * 
 * @version 0.1
 * @since Jun 16, 2011
 */
public abstract class DataSource implements Atomic {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * @return the descriptor for this data source
     */
    public abstract String getDescriptor();

    /**
     * @return the agent instance associated to this datasource
     */
    public abstract DataSrcInstance getInstance();

}
