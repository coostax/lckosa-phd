/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.engines.match;

import pt.iscte.pramc.sit.agent.VisualSoftwareAgent;
import pt.iscte.pramc.sit.exceptions.MatchingWithSelfException;
import pt.iscte.pramc.sit.ext.Pair;
import pt.iscte.pramc.sit.ontology.OntEngine;
import pt.iscte.pramc.sit.ontology.Translator;
import pt.iscte.pramc.sit.swi.si.Action;
import pt.iscte.pramc.sit.swi.si.Actuator;
import pt.iscte.pramc.sit.swi.si.AgentPart;
import pt.iscte.pramc.sit.swi.si.Atomic;
import pt.iscte.pramc.sit.swi.si.Sensor;
import pt.iscte.pramc.sit.swi.si.VisualAttribute;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * 
 *         Implementation of the software image match algorithm.
 * 
 * @since Aug 24, 2011
 * @version 2.0
 */
public class MatchAlgorithm {

    /**
     * The path for the base ontology
     */
    private final String baseOntPath;

    /**
     * The path for the domain ontology
     */
    private final String domainOntPath;

    /**
     * The base ontology namespace
     */
    private final String baseOntNS;

    /**
     * @param baseOntPath
     *            The path to the base ontology
     * @param domainOntPath
     *            the path to the domain ontology
     * @param baseOntNameSpace
     *            the namespace for the base ontology
     */
    public MatchAlgorithm(String baseOntPath, String domainOntPath,
	    String baseOntNameSpace) {
	super();
	this.baseOntPath = baseOntPath;
	this.domainOntPath = domainOntPath;
	this.baseOntNS = baseOntNameSpace;
    }

    /**
     * Matches an expert's static image with an apprentice's static image and
     * builds a translation matrix between the two
     * 
     * @param expert
     *            the expert's static image
     * @param apprentice
     *            the apprentice's static image
     * @param allowGeneralization
     * @return the translation matrix between expert and apprentice atomic
     *         elements
     * @throws MatchingWithSelfException
     */
    public TranslationMatrix match(VisualSoftwareAgent expert,
	    VisualSoftwareAgent apprentice, boolean allowGeneralization)
	    throws MatchingWithSelfException {
	// prevent agent to match with itself
	if (expert.getAgentUUID().equals(apprentice.getAgentUUID())) {
	    throw new MatchingWithSelfException(expert.getSoftwareImage());
	}
	// match agent parts
	TranslationMatrix matrix = new TranslationMatrix();
	int partCount = 0;
	for (AgentPart expertAp : expert.getSoftwareImage().getStaticImage()
		.getAgentParts()) {
	    for (AgentPart apprenticeAp : apprentice.getSoftwareImage()
		    .getStaticImage().getAgentParts()) {
		final TranslationMatrix matchParts = matchParts(expertAp,
			apprenticeAp, allowGeneralization);
		if (matchParts != null) {
		    matrix.join(matchParts);
		    partCount++;
		    break;
		}
	    }
	}
	// verify if all expert parts have been matched
	if (partCount == expert.getSoftwareImage().getStaticImage()
		.getNumOfAgentParts()) {
	    return matrix;
	}
	return null;
    }

    // ------HELPER METHODS-------------------

    /**
     * Matches two agent parts according to the matching rules between
     * apprentice and expert The apprentice's part can have more elements than
     * the expert's as long as all expert's elements match with one of the
     * apprentice's
     * 
     * @param expertAp
     * @param apprenticeAp
     * @param allowGeneralization
     *            the generalization flag
     * @return
     */
    private TranslationMatrix matchParts(AgentPart expertAp,
	    AgentPart apprenticeAp, boolean allowGeneralization) {
	// the matching matrix
	TranslationMatrix mm = new TranslationMatrix();
	// match sensors
	int sCount = 0;
	for (Sensor expertSns : expertAp.getSensors()) {
	    for (Sensor apprenticeSns : apprenticeAp.getSensors()) {
		Pair<Boolean, Translator> result = matchAtomic(expertSns,
			apprenticeSns, allowGeneralization);
		if (result.getFirst()) {
		    // add a new match
		    mm.add(new AtomicRelation(expertSns, apprenticeSns, result
			    .getSecond()));
		    sCount++;
		    break;
		}
	    }
	}
	// verify if all expert sensors where matched
	if (sCount != expertAp.getNumOfSensors()) {
	    return null;
	}
	// match Visual Attributes
	int vaCount = 0;
	for (VisualAttribute expertVa : expertAp.getVisualAttributes()) {
	    for (VisualAttribute apprenticeVa : apprenticeAp
		    .getVisualAttributes()) {
		Pair<Boolean, Translator> result = matchAtomic(expertVa,
			apprenticeVa, allowGeneralization);
		if (result.getFirst()) {
		    // add a new match
		    mm.add(new AtomicRelation(expertVa, apprenticeVa, result
			    .getSecond()));
		    vaCount++;
		    break;
		}
	    }
	}
	// verify if all expert visual attributes where matched
	if (vaCount != expertAp.getNumOfVisualAttributes()) {
	    return null;
	}
	// match actuators
	int actCount = 0;
	for (Actuator expertAct : expertAp.getActuators()) {
	    for (Actuator apprenticeAct : apprenticeAp.getActuators()) {
		TranslationMatrix aux = matchActuator(expertAct, apprenticeAct,
			allowGeneralization);
		if (aux != null) {
		    mm.join(aux);
		    actCount++;
		    break;
		}
	    }
	}
	// verify if all expert actuators where matched
	if (actCount != expertAp.getNumOfActuators()) {
	    return null;
	}
	// match internal parts
	int internalCount = 0;
	for (AgentPart expertIp : expertAp.getInternalAgentParts()) {
	    for (AgentPart apprenticeIp : apprenticeAp.getInternalAgentParts()) {
		TranslationMatrix aux = matchParts(expertIp, apprenticeIp,
			allowGeneralization);
		if (aux != null) {
		    mm.join(aux);
		    internalCount++;
		    break;
		}
	    }
	}
	// verify if all expert internal parts where matched
	if (internalCount != expertAp.getNumOfInternalParts()) {
	    return null;
	}
	// return resulting matching matrix
	return mm;
    }

    /**
     * Matches two actuators according to the matching rules between apprentice
     * and expert The apprentice's actuator can have more elements than the
     * expert's as long as all expert's elements match with one of the
     * apprentice's
     * 
     * @param expertAct
     * @param apprenticeAct
     * @param allowGeneralization
     *            the generalization flag
     * @return
     */
    private TranslationMatrix matchActuator(Actuator expertAct,
	    Actuator apprenticeAct, boolean allowGeneralization) {
	TranslationMatrix mm = new TranslationMatrix();
	int aCount = 0;
	for (Action expertAction : expertAct.getActionSet()) {
	    for (Action apprenticeAction : apprenticeAct.getActionSet()) {
		final Pair<Boolean, Translator> result = matchAtomic(
			expertAction, apprenticeAction, allowGeneralization);
		if (result.getFirst()) {
		    // add a new match
		    mm.add(new AtomicRelation(expertAction, apprenticeAction,
			    result.getSecond()));
		    aCount++;
		    break;
		}
	    }
	}
	// verify if all expert actions where matched
	if (aCount != expertAct.getNumberOfActions()) {
	    return null;
	}
	return mm;
    }

    /**
     * Matches two atomic elements (Sensor,VisualElement,Action) according to
     * the matching rules between apprentice and expert Atomic elements can be
     * generalized or not depending on the value of the flag
     * 
     * @param expertAtm
     * @param apprenticeAtm
     * @param allowGeneralization
     *            the generalization flag
     * @return
     */
    private Pair<Boolean, Translator> matchAtomic(Atomic expertAtm,
	    Atomic apprenticeAtm, boolean allowGeneralization) {
	if (allowGeneralization) {
	    Pair<Boolean, Translator> res = OntEngine.calculateSimilarity(
		    expertAtm.getDescriptor(), apprenticeAtm.getDescriptor(),
		    baseOntPath, domainOntPath, baseOntNS);
	    if (res.getFirst()) {
		return res;
	    }
	}
	// match by descriptor
	return new Pair<Boolean, Translator>(expertAtm.getDescriptor()
		.equalsIgnoreCase(apprenticeAtm.getDescriptor()), null);
    }

}
