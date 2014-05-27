/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.swi.si;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Interface representation of an agent part root
 * 
 *         Agent part roots can be agent parts or the static image
 * 
 * @version 0.1
 * @since Jul 5, 2011
 */
public interface APRoot {

    public <T> T getRoot();
}
