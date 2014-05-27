/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.ext;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Pairs two elements
 * 
 * @version 0.1
 * @since Jul 22, 2011
 */
public class Pair<K, V> {

    private final K first;

    private final V second;

    /**
     * Default constructor
     * 
     * @param first
     * @param second
     */
    public Pair(K first, V second) {
	super();
	this.first = first;
	this.second = second;
    }

    /**
     * @return the first
     */
    public K getFirst() {
	return first;
    }

    /**
     * @return the second
     */
    public V getSecond() {
	return second;
    }
}
