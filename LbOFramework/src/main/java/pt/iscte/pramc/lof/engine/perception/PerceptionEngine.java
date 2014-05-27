/**
 * Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.perception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.iscte.pramc.lof.agent.Apprentice;
import pt.iscte.pramc.lof.domain.LbOInstance;
import pt.iscte.pramc.lof.engine.LTEngine;
import pt.iscte.pramc.lof.engine.common.holders.PossibleBehaviour;
import pt.iscte.pramc.lof.exception.AttributeNotFoundException;
import pt.iscte.pramc.sit.agent.VisualSoftwareAgent;
import pt.iscte.pramc.sit.swi.di.Condition;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 *
 * The perception method is responsible for collecting the apprentice's perceptions
 * 
 * This method is also responsible for verifying if the perceived conditions exist in memory or if it is new information.
 * In case of being new information the apprentice returns to the learning stage until this new perception is observed
 * 
 * The perception is a set of conditions (from the environment of from visual attributes) that represent the environment's state
 * 
 * Makes use of the SoftwareImage's perception method to retrieve the agent's current perception
 *
 * @since 31 de Jan de 2012
 * @version 0.1 
 */
public class PerceptionEngine extends LTEngine{
	
	/**
	 * Default constuctor
	 * @param apprentice
	 */
	public PerceptionEngine(Apprentice apprentice) {
		super(apprentice);
	}

	/**
	 * Perception only works on execution stage
	 * Reads the conditions from the apprentice's software image perception and transforms them accordingly
	 */
	@Override
	protected void mechanismCycle() {
		if(apprentice.isExecutionStage()){
			//clean current conditions
			List<LbOInstance<?>> currentConditions = new ArrayList<LbOInstance<?>>();
			//read conditions from software image tools
			final List<Condition> conditions = ((VisualSoftwareAgent)apprentice).getAgentPerception().getCurrentConditions();
			//transform conditions into instances
			for(Condition cond : conditions){
				try {
					LbOInstance<Object> instance = new LbOInstance<Object>(apprentice.getAttributeFrom(cond.getSource(),false),cond.getData());
					currentConditions.add(instance);
				} catch (AttributeNotFoundException e) {
					logger.error("Cannot continue perception update.",e);
					currentConditions = Collections.emptyList();
					break;
				}
			}
			//check if conditions exist in memory and proceed as necessary
			if(!currentConditions.isEmpty() && apprentice.checkConditionsInMemory(currentConditions)){
				//behaviour selection
				List<PossibleBehaviour> behaviours = apprentice.provideBehavioursFor(currentConditions);
				//prepare execution 
				apprentice.prepareExecution(currentConditions,behaviours);
			}
		}
		
	}
}
