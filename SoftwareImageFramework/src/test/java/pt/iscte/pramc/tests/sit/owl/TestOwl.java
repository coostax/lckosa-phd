/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.tests.sit.owl;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Tests made to the OWL API
 * 
 * @version 0.1
 * @since Oct 24, 2011
 */
public class TestOwl {

    private static final String SOFTWARE_IMAGE_ONT_WEB_URL = "http://embodiedlearningbyobservation.googlecode.com/svn/ontology/softwareimage.owl";
    private static final String SOFTWARE_IMAGE_ONT_FILE_URL = "/home/paulo/workspace/SoftwareImageFramework/src/main/resources/ontology/softwareimage.owl";

    /**
     * Load an ontology from the web
     * 
     * @throws OWLOntologyCreationException
     */
    public OWLOntology loadFromWeb() throws OWLOntologyCreationException {
	final OWLOntologyManager manager = OWLManager
		.createOWLOntologyManager();
	final IRI iri = IRI.create(SOFTWARE_IMAGE_ONT_WEB_URL);// "http://www.co-ode.org/ontologies/pizza/pizza.owl");
	final OWLOntology ontology = manager
		.loadOntologyFromOntologyDocument(iri);
	return ontology;
    }

    /**
     * Load an ontology from the web
     * 
     * @throws OWLOntologyCreationException
     */
    public OWLOntology loadFromFile() throws OWLOntologyCreationException {
	final OWLOntologyManager manager = OWLManager
		.createOWLOntologyManager();
	final File file = new File(SOFTWARE_IMAGE_ONT_FILE_URL);
	final OWLOntology swiOnt = manager
		.loadOntologyFromOntologyDocument(file);
	return swiOnt;
    }

    public OWLClass readClassFromOntology(final OWLOntology ont) {
	final OWLOntologyManager manager = OWLManager
		.createOWLOntologyManager();
	final OWLDataFactory factory = manager.getOWLDataFactory();
	final PrefixManager pm = new DefaultPrefixManager(
		"http://embodiedlearningbyobservation.googlecode.com/svn/ontology/softwareimage.owl#");
	// Now we use the prefix manager and just specify an abbreviated IRI
	final OWLClass sensor = factory.getOWLClass(":Sensor", pm);
	return sensor;
    }

    @Test
    public void testLoadFromWeb() throws OWLOntologyCreationException {
	final OWLOntology ont = loadFromWeb();
	final OWLClass sensor = readClassFromOntology(ont);
	assertNotNull(sensor);
    }

    @Test
    public void testLoadFromFile() throws OWLOntologyCreationException {
	final OWLOntology ont = loadFromFile();
	final OWLClass sensor = readClassFromOntology(ont);
	assertNotNull(sensor);
    }

}
