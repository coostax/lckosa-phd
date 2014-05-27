/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.tests.sit.owl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * 
 *         Tests for the JENA API
 * 
 * @since Oct 25, 2011
 * @version 0.1
 */
public class TestJena {

    // private static final String LEARNING_SCENARIOS_ONT_URL =
    // "https://code.google.com/p/embodiedlearningbyobservation/source/browse/ontology/learningscenarios.owl";
    // private static final String SOFTWARE_IMAGE_ONT_URL =
    // "https://code.google.com/p/embodiedlearningbyobservation/source/browse/ontology/softwareimage.owl";

    private static final String LEARNING_SCENARIOS_ONT_URL = "file:/home/paulo/workspace/SoftwareImageFramework/src/main/resources/ontology/learningscenarios.owl";
    private static final String SOFTWARE_IMAGE_ONT_URL = "file:/home/paulo/workspace/SoftwareImageFramework/src/main/resources/ontology/softwareimage.owl";

    public static String NAMESPACE = "http://embodiedlearningbyobservation.googlecode.com/svn/ontology/softwareimage.owl#";

    public OntModel loadOntology() {
	// read the file
	final String source = SOFTWARE_IMAGE_ONT_URL;

	// create model
	final OntModel m = ModelFactory.createOntologyModel(
		OntModelSpec.OWL_MEM, null);

	// read the source document
	m.read(source);
	return m;
    }

    /**
     * Test ontology import
     */
    @Test
    public void testOntologyImport() {
	final OntModel baseModel = ModelFactory.createOntologyModel(
		OntModelSpec.OWL_MEM, null);
	baseModel.read(SOFTWARE_IMAGE_ONT_URL);

	final OntModel domainModel = ModelFactory.createOntologyModel(
		OntModelSpec.OWL_MEM, baseModel);
	// read the source document
	domainModel.read(LEARNING_SCENARIOS_ONT_URL);
	// domainModel.addLoadedImport("file:/home/paulo/workspace/ontology/softwareimage.owl");

	// read individual described in ontology file
	final Individual hand = domainModel
		.getIndividual("http://embodiedlearningbyobservation.googlecode.com/svn/ontology/learningscearios.owl#agentHand");
	assertNotNull(hand);

	// read individual described in imported ontology
	final Individual lsensor = domainModel
		.getIndividual("http://embodiedlearningbyobservation.googlecode.com/svn/ontology/softwareimage.owl#LightSensorA");
	assertNotNull(lsensor);

	// read property from imported ontology
	final ObjectProperty matches = domainModel
		.getObjectProperty("http://embodiedlearningbyobservation.googlecode.com/svn/ontology/softwareimage.owl#matches");
	assertNotNull(matches);

	// assertTrue(hand.hasProperty(matches));
	assertTrue(lsensor.hasProperty(matches));
    }

    /**
     * Tests the OntEngine
     */
    @Test
    public void testOntEngine() {
	final OntModel model = loadOntology();
	assertNotNull(model);
	// ensure the model has classes
	assertTrue(model.listClasses().hasNext());
    }

    /**
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    @Test
    public void testMethodInstantiation() throws InstantiationException,
	    IllegalAccessException, ClassNotFoundException {
	final OntModel model = loadOntology();
	// ensure the model has classes
	assertTrue(model.listClasses().hasNext());
	// read a class from the ontology
	final Individual atomic = model.getIndividual(NAMESPACE
		+ "LightSensorA");
	assertNotNull(atomic);
	// turn this into a method-------------
	assertTrue(seeTranslator(model, atomic));
    }

    /**
     * @param model
     * @param atomic
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    private boolean seeTranslator(final OntModel model, final Individual atomic)
	    throws InstantiationException, IllegalAccessException,
	    ClassNotFoundException {
	if (atomic != null) {
	    final ObjectProperty matches = model.getObjectProperty(NAMESPACE
		    + "matches");
	    final DatatypeProperty translatorImpl = model
		    .getDatatypeProperty(NAMESPACE + "implementedBy");
	    if (matches != null) {
		if (translatorImpl != null) {
		    if (atomic.hasProperty(matches)) {
			final StmtIterator observables = atomic
				.listProperties(matches);
			while (observables.hasNext()) {
			    final Resource translator = observables.next()
				    .getResource();
			    if (translator != null
				    && translator.hasProperty(translatorImpl)) {
				// get the implementation class for the
				// translation from A to B;
				final String classname = translator
					.getProperty(translatorImpl)
					.getString();
				System.out.println("Implementor is: "
					+ classname);
				Class.forName(classname).newInstance();
			    } else {
				System.out
					.println("ERROR_ONT_001:  not a translator individual");
				return false;
			    }
			}
		    }
		} else {
		    System.out
			    .println("ERROR_ONT_002: implementedBy property not found");
		    return false;
		}
	    } else {
		System.err
			.println("ERROR_ONT_003: canObserve property not found");
		return false;
	    }
	} else {
	    System.err.println("ERROR_ONT_004: object is null");
	    return false;
	}
	return true;
    }

}
