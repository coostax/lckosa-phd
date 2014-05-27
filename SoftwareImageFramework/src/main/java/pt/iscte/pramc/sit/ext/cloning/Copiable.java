/**
 * Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.ext.cloning;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * 
 *         Interface representation of a copiable (cloneable) object
 * 
 *         Cloneable objects are objects that are able to make an unlinked copy
 *         of themselves.
 * 
 *         It can be used to represent the data provided by sensors and visual
 *         attributes. All sensors and visual attributes must provide datatypes
 *         that can be cloned to prevent unwanted changes in the agent's dynamic
 *         image.
 * 
 *         The dynamic image must be populated with new objects with no links to
 *         the agent internals
 * 
 *         DynamicData instances must implement the clone() method
 * 
 * @since 8 de Nov de 2011
 * @version 0.1
 */
public interface Copiable<T> extends Cloneable {

    /**
     * @return a new object with the same information as this one
     */
    public T copy() throws CloneNotSupportedException;

}
