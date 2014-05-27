/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.ontology;

import pt.iscte.pramc.sit.ext.Pair;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Contains all operations related to the atomic element ontology
 * 
 *         Ontology classes are identified by string values. Using the JENA API
 *         for ontology manipulation
 * 
 * @version 0.1
 * @since Oct 26, 2011
 */
public class OntEngine {

    // private static final String DOMAIN_ONT_PATH = new String(
    // PropertiesLoader.loadPropertyFrom("learning.properties",
    // "ontology.domain.sourcepath",
    // "http://embodiedlearningbyobservation.googlecode.com/svn/ontology/learningscenarios.owl"));
    //
    // private static final String BASE_ONT_PATH = new String(
    // PropertiesLoader.loadPropertyFrom("learning.properties",
    // "ontology.base.sourcepath",
    // "http://embodiedlearningbyobservation.googlecode.com/svn/ontology/softwareimage.owl"));
    //
    // //software image ontology properties
    // private static final String BASE_ONT_NAMESPACE = new String(
    // PropertiesLoader.loadPropertyFrom("learning.properties",
    // "ontology.base.namespace",
    // "http://embodiedlearningbyobservation.googlecode.com/svn/ontology/softwareimage.owl#"));

    // private static final String MATCHES_PROP = BASE_ONT_NAMESPACE+"matches";
    //
    // private static final String IMPLEMENTED_BY_PROP =
    // BASE_ONT_NAMESPACE+"implementedBy";

    private static final String MATCHES_PROP = "matches";

    private static final String IMPLEMENTED_BY_PROP = "implementedBy";

    // private final static OntModel ontology = loadOntology();

    private static OntModel loadOntology(String baseOntPath,
	    String domainOntPath) {
	OntModel m;
	if (baseOntPath != domainOntPath) {
	    OntModel baseModel = ModelFactory.createOntologyModel(
		    OntModelSpec.OWL_MEM, null);
	    baseModel.read(baseOntPath);
	    // create model from base
	    m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM,
		    baseModel);
	} else {
	    // create model
	    m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
	}
	// read the source document
	m.read(domainOntPath);
	return m;
    }

    /**
     * 
     * @param oc1
     *            the first ontology class for the translator
     * @param oc2
     *            the second ontology class for the translator
     * @return a translator instance if found in the ontology, null if nothing
     *         is found
     */
    public static Pair<Boolean, Translator> calculateSimilarity(String oc1,
	    String oc2, String baseOntPath, String domainOntPath,
	    String namespace) {
	OntModel ontology = loadOntology(baseOntPath, domainOntPath);
	Individual first = ontology.getIndividual(oc1);
	if (first == null) {
	    System.err.println("ERROR_OntEngine_001: individual " + oc1
		    + " not found");
	    return new Pair<Boolean, Translator>(false, null);
	}
	Individual second = ontology.getIndividual(oc2);
	if (second == null) {
	    System.err.println("ERROR_OntEngine_002: individual " + oc2
		    + " not found");
	    return new Pair<Boolean, Translator>(false, null);
	}
	// load base properties
	ObjectProperty matches = ontology.getObjectProperty(namespace
		+ MATCHES_PROP);
	if (matches == null) {
	    System.err.println("ERROR_OntEngine_003: property " + namespace
		    + MATCHES_PROP + " not found");
	    return new Pair<Boolean, Translator>(false, null);
	}
	DatatypeProperty implBy = ontology.getDatatypeProperty(namespace
		+ IMPLEMENTED_BY_PROP);
	if (implBy == null) {
	    System.err.println("ERROR_OntEngine_004: property " + namespace
		    + MATCHES_PROP + " not found");
	    return new Pair<Boolean, Translator>(false, null);
	}
	// continue only if both individuals have match properties
	if (first.hasProperty(matches) && second.hasProperty(matches)) {
	    // iterate first individual
	    StmtIterator matchesForFirst = first.listProperties(matches);
	    StmtIterator matchesForSecond = second.listProperties(matches);
	    while (matchesForFirst.hasNext()) {
		Resource rs1 = matchesForFirst.next().getResource();
		while (matchesForSecond.hasNext()) {
		    Resource rs2 = matchesForSecond.next().getResource();
		    // when both resources are the same it means that first and
		    // second match
		    if (rs1.hasURI(rs2.getURI())) {
			Translator trl = null;
			// see if the resource has a translation class
			if (rs1.hasProperty(implBy)) {
			    final String classname = rs1.getProperty(implBy)
				    .getString();
			    try {
				trl = (Translator) Class.forName(classname)
					.newInstance();
			    } catch (InstantiationException e) {
				System.err
					.println("ERROR_OntEngine_005: could not instantiate "
						+ classname);
				return new Pair<Boolean, Translator>(true, null);
			    } catch (IllegalAccessException e) {
				System.err
					.println("ERROR_OntEngine_006: illegal access to "
						+ classname);
				return new Pair<Boolean, Translator>(true, null);
			    } catch (ClassNotFoundException e) {
				System.err
					.println("ERROR_OntEngine_007: class "
						+ classname + " not found");
				return new Pair<Boolean, Translator>(true, null);
			    }
			}
			return new Pair<Boolean, Translator>(true, trl);
		    }
		}
	    }
	}
	return new Pair<Boolean, Translator>(false, null);
    }
}
