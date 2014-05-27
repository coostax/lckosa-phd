/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.common.holders;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Default storage for an agent's internal attribute
 * 
 *         Internal attributes are usually related to a single object. They must
 *         have a description to distinguish them
 * 
 * @version 0.1
 * @since Jul 20, 2011
 */
public class InternalAttributeHolder {

	public final String descriptor;

	public final Object value;

	/**
	 * @param descriptor
	 * @param value
	 */
	public InternalAttributeHolder(String descriptor, Object value) {
		super();
		this.descriptor = descriptor;
		this.value = value;
	}

}
