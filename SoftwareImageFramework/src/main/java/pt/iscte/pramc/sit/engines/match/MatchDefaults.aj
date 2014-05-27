/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.engines.match;

import org.apache.log4j.Logger;

import pt.iscte.pramc.sit.agent.VisualSoftwareAgent;
import pt.iscte.pramc.sit.exceptions.MatchingWithSelfException;
import pt.iscte.pramc.sit.ext.PropertiesLoader;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * Default implementation of the getSimilarity(), canObserve() and match() methods in the VisualSoftwareAgent interface
 *
 * @version 0.1
 * @since Jul 7, 2011
 */
public aspect MatchDefaults {

	static Logger logger = Logger.getLogger(MatchDefaults.class);
	
	/**
	 * Indicates how the supplied and the own software image have the same atomic parts organized in the same fashion
	 * @param another
	 * @return a value between 0.0 (completely different) to 1.0 (with the same organization and atomic elements)
	 */
	public double VisualSoftwareAgent.getSimilarity(VisualSoftwareAgent another){
		return this.getSoftwareImage().getSimilarity(another.getSoftwareImage());
	}
	
	/**
	 * Validates if the supplied apprentice can learn from this agent
	 * @param apprentice
	 * @return true if the apprentice is able to learn from this agent
	 */
	public boolean VisualSoftwareAgent.canObserve(VisualSoftwareAgent apprentice){
		try{
			return this.match(apprentice,true) != null;
		}catch (Exception e) {
			logger.error(e);
			return false;
		}
	}
	
	/**
	 * Matches two software images and builds a matching matrix.
	 * Takes into consideration the fact that for learning to be effective the expert's image must be contained in the apprentice's image
	 * The apprentice's image can have more elements than the expert's as long as all expert's elements match with one of the apprentice's
	 * 
	 * This matching method also allows generalization between atomic elements through the organization of the atomic element in their ontology  
	 *  
	 *  Loads properties from default learning.properties file
	 *  
	 * @param apprentice
	 * @param allowGeneralization flag indicating if generalization is allowed or not
	 * @return
	 * @throws MatchingWithSelfException when matching with itself
	 */
	public TranslationMatrix VisualSoftwareAgent.match(VisualSoftwareAgent apprentice,boolean allowGeneralization) throws MatchingWithSelfException{
		MatchAlgorithm ma = new MatchAlgorithm(PropertiesLoader.loadPropertyFrom("learning.properties", "ontology.base.sourcepath", "http://embodiedlearningbyobservation.googlecode.com/svn/ontology/softwareimage.owl"),
				PropertiesLoader.loadPropertyFrom("learning.properties", "ontology.domain.sourcepath", "http://embodiedlearningbyobservation.googlecode.com/svn/ontology/learningscenarios.owl"),
				PropertiesLoader.loadPropertyFrom("learning.properties", "ontology.base.namespace", "http://embodiedlearningbyobservation.googlecode.com/svn/ontology/softwareimage.owl#") 
				);
		return ma.match(this, apprentice, allowGeneralization);
	}
}
