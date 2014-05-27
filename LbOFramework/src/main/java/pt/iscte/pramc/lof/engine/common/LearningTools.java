/**
 * Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.common;

import java.util.List;

import pt.iscte.pramc.sit.swi.di.ActionInstance;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 *
 * Provides a set of usefull tools for learning agents.
 *
 * @since 30 de Jan de 2012
 * @version 0.1 behaviour comparison methods
 */
public class LearningTools {

	public static final String INTERNAL_ARGS_REF = "//INTERNAL:";
	
	public static final String DEFAULT_ONT_REF = "http://embodiedlearningbyobservation.googlecode.com/svn/ontology/softwareimage.owl#";
	
	/**
	 * Validates if two behaviours are correct. 
	 * Two behaviours are correct if they have the same action instances in the same order
	 * @param first the first behaviour to compare
	 * @param second the second behaviour to compare with
	 * @return true if behaviours match, false otherwise
	 */
	public static boolean doBehavioursMatch(List<ActionInstance> first,List<ActionInstance> second){
		if(second.size() == first.size()){
			for(int i = 0; i != second.size(); ++i){
				ActionInstance correct = second.get(i);
				ActionInstance toCompare = first.get(i);
				if(!correct.isTheSameAs(toCompare)){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Indicates if the first behaviour is contained in the second. 
	 * All action instances on the first must also exist on the second following the same order 
	 * @param first
	 * @param second
	 * @return
	 */
	public static boolean isBhContainedIn(List<ActionInstance> first,List<ActionInstance> second){
		if(second.size() >= first.size()){
			for(int i = 0; i != first.size(); ++i){
				ActionInstance correct = second.get(i);
				ActionInstance toCompare = first.get(i);
				if(!correct.isTheSameAs(toCompare)){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
}
