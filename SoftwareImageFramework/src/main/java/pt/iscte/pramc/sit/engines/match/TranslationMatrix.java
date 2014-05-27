/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.engines.match;

import java.util.ArrayList;
import java.util.Iterator;

import pt.iscte.pramc.sit.exceptions.NoMacthForAtomicElementException;
import pt.iscte.pramc.sit.ext.NoAction;
import pt.iscte.pramc.sit.ext.Pair;
import pt.iscte.pramc.sit.ontology.Translator;
import pt.iscte.pramc.sit.swi.si.Action;
import pt.iscte.pramc.sit.swi.si.Atomic;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         The translation matrix allows matching and translation between the
 *         expert's atomic elements in the software image (sensors, visual
 *         attributes and actions) with the atomic elements in the apprentice.
 * 
 *         It is used as reference for converting expert snapshots to apprentice
 *         episodes
 * 
 * @version 0.1
 * @since Jul 22, 2011
 */
public class TranslationMatrix extends ArrayList<AtomicRelation> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * Obtains the matching element from the matrix
     * 
     * @param element
     * @return the match for the required atomic element along with the
     *         translator if it exists
     * @throws NoMacthForAtomicElementException
     *             whenever a match is not found
     */
    public Pair<Atomic, Translator> getMatchFor(Atomic element)
	    throws NoMacthForAtomicElementException {
	final Iterator<AtomicRelation> iterator = iterator();
	// provide for the NoAction instance
	if (element instanceof Action
		&& ((Action) element).isSimilar(NoAction.noActionAtom)) {
	    return new Pair<Atomic, Translator>(NoAction.noActionAtom, null);
	}
	while (iterator.hasNext()) {
	    AtomicRelation rel = iterator.next();
	    if (rel.getFirst().equals(element)) {
		return new Pair<Atomic, Translator>(rel.getSecond(),
			rel.getTranslator());
	    } else if (rel.getSecond().equals(element)) {
		return new Pair<Atomic, Translator>(rel.getFirst(),
			rel.getTranslator());
	    }
	}
	// no match found
	throw new NoMacthForAtomicElementException(element);
    }

    public boolean addMatchPair(Atomic expertElem, Atomic apprenticeElem,
	    Translator translator) {
	AtomicRelation rel = new AtomicRelation(expertElem, apprenticeElem,
		translator);
	return add(rel);
    }

    /**
     * Joins this translation matrix with another one
     * 
     * @param mm
     * @return true if join succeeded.
     */
    public boolean join(TranslationMatrix mm) {
	return addAll(mm);
    }
}
