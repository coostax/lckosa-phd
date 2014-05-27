/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.engines.match;

import pt.iscte.pramc.sit.ontology.Translator;
import pt.iscte.pramc.sit.swi.si.Atomic;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Holds the pair and translator for each atomic element relationship
 * 
 * @version 0.1
 * @since Oct 29, 2011
 */
public class AtomicRelation {

    private final Atomic first;

    private final Atomic second;

    private final Translator translator;

    /**
     * Default constructor
     * 
     * @param first
     * @param second
     * @param translator
     */
    public AtomicRelation(Atomic first, Atomic second, Translator translator) {
	super();
	this.first = first;
	this.second = second;
	this.translator = translator;
    }

    /**
     * @return the first
     */
    public Atomic getFirst() {
	return first;
    }

    /**
     * @return the second
     */
    public Atomic getSecond() {
	return second;
    }

    /**
     * @return the translator
     */
    public Translator getTranslator() {
	return translator;
    }

}
