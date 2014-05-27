/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.memory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import pt.iscte.pramc.lof.domain.LbOInstance;
import pt.iscte.pramc.lof.domain.Step;
import pt.iscte.pramc.lof.exception.MatchException;
import pt.iscte.pramc.sit.ext.Pair;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * Stores and manages the observed steps.
 * 
 * Only new steps are stored, if the step to store already exists in memory only the number of times it was observed is updated
 *
 * @version 0.1 initial version
 * @version 1.0 Updated storage for new types of step
 * @since Nov 2, 2011
 */
public class SequentialStorage implements Serializable{

	/**
	 * @author Paulo Costa (paulo.costa@iscte.pt)
	 *  
	 * Represents the base element that makes up the sequential storage
	 * 
	 * This base element is made out of the Step and a counter that indicates the number of times this step was observed
	 * 
	 * @since 8 de Nov de 2011
	 * @version 0.1
	 */
	private class Element implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public final Step step;
		public int numObservations;
		
		/**
		 * Default constructor
		 * @param e
		 */
		public Element(Step e) {
			this.step = e;
			this.numObservations = 1;
		}

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Element){
				return this.step.equals(((Element)obj).step);
			}else if(obj instanceof Step){
				return this.step.equals(obj);
			}
			return super.equals(obj);
		}
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return numObservations + ": " + step.toString();
		}
	}
	
	/**
	 * the storage array
	 */
	private final List<SequentialStorage.Element> storageArray;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor. Initializes the storage array
	 */
	public SequentialStorage(){
		this.storageArray = new ArrayList<SequentialStorage.Element>();
	}
	
	/**
	 * Adds a step to the sequential storage 
	 * If the step already exists in the sequential storage then its counter is updated
	 * @return the step stored in memory, weather is new or existing. Null if something went wrong 
	 */
	public Step add(Step step) {
		//updateAttributes(step);
		//update memory
		Element elem = new Element(step);
		int index = storageArray.indexOf(elem);
		if(index == -1){
			//the step was never observed, add it to the storage array
			return storageArray.add(elem) ? elem.step : null;
		}
		//the step was already observed, increase numObservations
		Element aux = storageArray.get(index);
		if(aux != null){
			aux.numObservations++;
			return aux.step;
		}
		return null;
	}
	
	/**
	 * @return the number of steps stored
	 */
	public int size(){
		return storageArray.size();
	}
	
	/**
	 * 
	 * @param step the step to be analyzed
	 * @return the number of times the provided step was observed
	 */
	public int getNumObservedFor(Step step){
		final int index = storageArray.indexOf(new Element(step)); 
		if(index == -1){ //step was never observed
			return 0;
		}
		return storageArray.get(index).numObservations;
	}
	
	/**
	 * Calculates the average observations for the steps
	 * @return an average value of the number of times the steps have been observed
	 */
	public double getAvgObservations(){
		if(storageArray.isEmpty()){
			return 0;
		}
		//if not empty them calculate average
		double counter = 0;
		for(Element elem: storageArray){
			counter += elem.numObservations;
		}
		return counter/storageArray.size();
	}
	
	/**
	 * Calculates the average number of following steps
	 * @return
	 */
	public double getAvgFollowing(){
		if(storageArray.isEmpty()){
			return 0;
		}
		//if not empty them calculate average
		double counter = 0;
		for(Element elem: storageArray){
			counter += elem.step.getNumOfFollowingSteps();
		}
		return counter/storageArray.size();
	}
	
	/**
	 * Gets the maximum number of same observed snapshots
	 * @return
	 */
	public int getMaxObservations(){
		if(storageArray.isEmpty()){
			return 0;
		}
		//if not empty them get maximum
		int max = 0;
		for(Element elem: storageArray){
			if(max < elem.numObservations){
				max = elem.numObservations;
			}
		}
		return max;
	}
	
	/**
	 * @param i the position of the stored step
	 * @return the step stored at the provided position
	 */
	public Step get(int i) {
		return storageArray.get(i).step;
	}
	
	/**
	 * @param instances
	 * @return the step associated to this instances, null if nothing is found
	 */
	public Step getFromInstances(final List<LbOInstance<?>> instances){
		int index = storageArray.indexOf(new Element(new Step(instances,null)));
		if(index != -1){
			return storageArray.get(index).step;
		}
		return null;
	}
	
	/**
	 * @return the agent's memory as an unmodifiable list of steps
	 */
	public List<Step> getAll(){
		List<Step> steps = new ArrayList<Step>(storageArray.size());
		for(SequentialStorage.Element elem : storageArray){
			steps.add(elem.step);
		}
		return Collections.unmodifiableList(steps);
	}

	/**
	 * @param instances the conditions to look for
	 * @return a list of steps that contain the provided conditions,  an empty list if nothing is found
	 */
	public List<Step> getForConditions(final List<LbOInstance<?>> instances){
		List<Step> steps = new ArrayList<Step>();
		for(SequentialStorage.Element elem : storageArray){
			try {
				//use step matcher to match steps
				final double prob= StepMatcher.match(elem.step.getConditions(), instances);
				if(prob == 1.0){
					//this step's conditions match the provided conditions, add to list
					steps.add(elem.step);
				}
			} catch (MatchException e) {
			}
		}
		return steps;
	}
	
	/**
	 * Provides a string representation for this storage
	 */
	@Override
	public String toString() {
		StringBuffer res = new StringBuffer("[Storage]\n");
		Iterator<Element> it =storageArray.iterator();
		int i = 0;
		while(it.hasNext()){
			Element elem = it.next();
			res.append("---[");
			res.append(i++);
			res.append("] observed ");
			res.append(elem.numObservations);
			res.append(" times: ");
			res.append("[Conditions]");
			Step st = elem.step;
			res.append(st.getConditions().toString());
			res.append("[/Conditions]->[Behaviour]");
			res.append(st.getBehaviour().toString());
			res.append("[/Behaviour]\n");
//			Iterator<Step> nextSteps = st.getFollowingSteps();
//			while(nextSteps.hasNext()){
			for(Pair<Double,Step> next : st.getFollowingStepsWithStrength()){
				res.append("----[Next(");
				res.append(next.getFirst());
				res.append(")]\n");
				res.append("--------[Conditions]");
				res.append(next.getSecond().getConditions().toString());
				res.append("[/Conditions]->[Behaviour]");
				res.append(next.getSecond().getBehaviour().toString());
				res.append("[/Behaviour]\n");
				res.append("----[/Next]\n");
			}
		}
		res.append("\n[/Storage]");
		return res.toString();
	}
	
}
