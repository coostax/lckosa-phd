/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.vision;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pt.iscte.pramc.lof.agent.Apprentice;
import pt.iscte.pramc.lof.domain.BehaviourInstance;
import pt.iscte.pramc.lof.domain.ConditionAttribute;
import pt.iscte.pramc.lof.domain.LbOInstance;
import pt.iscte.pramc.lof.domain.Step;
import pt.iscte.pramc.lof.engine.LTEngine;
import pt.iscte.pramc.lof.exception.AttributeNotFoundException;
import pt.iscte.pramc.lof.exception.NoSimilarAgentFoundException;
import pt.iscte.pramc.lof.exception.NotObservingExpertException;
import pt.iscte.pramc.sit.agent.VisualSoftwareAgent;
import pt.iscte.pramc.sit.engines.builder.AgentRegistry;
import pt.iscte.pramc.sit.engines.match.MatchAlgorithm;
import pt.iscte.pramc.sit.engines.match.TranslationMatrix;
import pt.iscte.pramc.sit.engines.update.Observer;
import pt.iscte.pramc.sit.exceptions.MatchingWithSelfException;
import pt.iscte.pramc.sit.exceptions.NoMacthForAtomicElementException;
import pt.iscte.pramc.sit.exceptions.ObserverException;
import pt.iscte.pramc.sit.ext.Pair;
import pt.iscte.pramc.sit.ontology.Translator;
import pt.iscte.pramc.sit.swi.di.ActionInstance;
import pt.iscte.pramc.sit.swi.di.Condition;
import pt.iscte.pramc.sit.swi.di.Snapshot;
import pt.iscte.pramc.sit.swi.si.Action;
import pt.iscte.pramc.sit.swi.si.Atomic;
import pt.iscte.pramc.sit.swi.si.DataSource;
import pt.iscte.pramc.tools.TimeMeasurement;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         The software vision is responsible for finding and starting the
 *         observation of similar expert agents. Uses the tools provided by the
 *         software image toolkit to find and compare the apprentice's image
 *         with the expert's image
 *         
 *         The software vision follows the observer pattern to react to updates in the expert's software image
 * 
 *  		Snapshot timestamping is essential to identify the Step order. While the apprentice is reading the history records from the expert new snapshots are being generated.
 *  These new snapshots need to be placed after history data has been read, so the vision sensor makes use of a buffer that stores these new snapshots until all history records are read.
 * 
 * @version 0.1 initial version
 * @version 1.0 software vision is now an independent thread
 * @version 1.5 solved syncronization problems with a Step buffer and by timestamping snapshots
 * @since Aug 2, 2011
 */
public class SoftwareVisionEngine extends LTEngine implements Observer{

	private static Logger logger = Logger.getLogger(SoftwareVisionEngine.class);
	
	//---timers
	/**
	 * The time to find an expert to observe
	 */
	private long ttfe;
	
	/**
	 * The time to compare the currently observed expert
	 */
	private long ttce;
	
	/**
	 * The time to read the expert's history
	 */
	private long ttrh;
	/**
	 * the time to observe a snapshot from the expert's dinamic image 
	 */
	private long ttso;
	
	/**
	 * Keeps track of observed expert agents
	 */
	private final List<Pair<VisualSoftwareAgent, TranslationMatrix>> observedExperts;

	/**
	 * Generalization flag for agent comparison
	 */
	private final boolean allowGeneralization;

	/**
	 * The currently observed expert agent
	 */
	private Pair<VisualSoftwareAgent, TranslationMatrix> currentlyObserving;

	/**
	 * The place where all agents with software image are registered
	 */
	private final AgentRegistry registry = AgentRegistry.getAgentRegistry();

	/**
	 * Indicates that the expert is focused on an expert and has already read its historical data 
	 */
	private boolean hasReadHistory;
	
	/**
	 * Buffer that stores newly observed snapshots
	 */
	private final List<Step> buffer;
	
	private final MatchAlgorithm matchAlgorithm;
	
	//constructors
	
	/**
	 * Default constructor. Receives the apprentice's software image
	 * 
	 * @param associatedSoftwareImage
	 */
	public SoftwareVisionEngine(Apprentice agent, boolean allowGeneralization, String baseOntPath, String domainOntPath, String baseOntNameSpace) {
		super(agent);
		this.hasReadHistory = false;
		this.allowGeneralization = allowGeneralization;
		this.observedExperts = new ArrayList<Pair<VisualSoftwareAgent, TranslationMatrix>>();
		this.currentlyObserving = null;
		this.matchAlgorithm = new MatchAlgorithm(baseOntPath, domainOntPath, baseOntNameSpace);
		//TODO: add a comparator to this list
		this.buffer = new ArrayList<Step>();
	}

	//methods
	
	/**
	 * Finds a similar expert to be observed
	 * @return an expert agent and its translation matrix
	 * @throws NoSimilarAgentFoundException
	 * @throws NoMacthForAtomicElementException
	 */
	public Pair<VisualSoftwareAgent, TranslationMatrix> findExpertToObserve()
			throws NoSimilarAgentFoundException,
			NoMacthForAtomicElementException {
		final List<VisualSoftwareAgent> agents = registry
				.retrieveVisualSoftwareAgents();
		for (VisualSoftwareAgent expert : agents) {
			// make preference to new expert agents
			try {
				this.ttce = TimeMeasurement.getCpuTime();
				TranslationMatrix matrix = matchAlgorithm.match(expert,
						apprentice, allowGeneralization);
				this.ttce = TimeMeasurement.getCpuTime() - ttce;
				if (matrix != null && !previouslyObserved(expert) && !(expert instanceof Apprentice)) {
					final Pair<VisualSoftwareAgent, TranslationMatrix> toObserve = new Pair<VisualSoftwareAgent, TranslationMatrix>(
							expert, matrix);
					setFocusOn(toObserve);
					return toObserve;
				}
			} catch (MatchingWithSelfException e) {
				continue;
			} catch (ObserverException e) {
				continue;
			}
		}
		// no new expert found, observe the first in the list
		if (observedExperts.isEmpty()) {
			throw new NoSimilarAgentFoundException(apprentice);
		}
		final Pair<VisualSoftwareAgent, TranslationMatrix> toObserve = observedExperts
				.get(0);
		observedExperts.remove(0);
		try {
			setFocusOn(toObserve);
			return toObserve;
		} catch (ObserverException e) {
			throw new NoSimilarAgentFoundException(apprentice);
		}
	}

	/**
	 * Initializes the observation protocol. Registers the vision sensor in the expert's observer list
	 * @param toObserve
	 * @throws ObserverException 
	 */
	private void setFocusOn(
			final Pair<VisualSoftwareAgent, TranslationMatrix> toObserve) throws ObserverException {
		//register vision sensor
		toObserve.getFirst().attach(this);
		currentlyObserving = toObserve;
		observedExperts.add(toObserve);
	}

	/**
	 * Gathers historical data from the observed expert
	 * 
	 * @param observedExpert the expert agent being observed
	 * @return the list of episodes gathered from the expert
	 * @throws NoMacthForAtomicElementException
	 * @throws AttributeNotFoundException 
	 */
	public List<Step> gatherHistoryDataFrom(
			final Pair<VisualSoftwareAgent, TranslationMatrix> observedExpert)
			throws NoMacthForAtomicElementException, AttributeNotFoundException {
		List<Step> history = new ArrayList<Step>();
		//load current snapshot
		Snapshot current = observedExpert.getFirst().getSoftwareImage().getDynamicImage().getCurrentSnapshot();
		if(current != null){
			history.add(translateSnapshot(current, observedExpert.getSecond()));
		}
		// load all historical information from the expert
		for (Snapshot snp : observedExpert.getFirst().getSoftwareImage()
				.getDynamicImage().getHistoricalRecord()) {
			if (snp == null) {
				break;
			}
			history.add(translateSnapshot(snp, observedExpert.getSecond()));
		}
		return history;
	}

	/**
	 * Checks if the expert has been previously observed
	 * 
	 * @param expert
	 * @return
	 */
	private boolean previouslyObserved(VisualSoftwareAgent expert) {
		for (Pair<VisualSoftwareAgent, TranslationMatrix> pair : observedExperts) {
			if (pair.getFirst().getAgentUUID().equals(expert.getAgentUUID())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Stops observation by removing the apprentice's attention on the expert
	 * @throws ObserverException when it is impossible to detach this vision sensor from the expert agent
	 */
	private void stopObservingExpert() {
		if(currentlyObserving != null){
			try {
				currentlyObserving.getFirst().detach(this);
			} catch (ObserverException e) {
				logger.error("Could not stop  observing expert",e);
			}
		}
		//clear the buffer
		distributeBufferedSteps();
		if(!buffer.isEmpty()){
			logger.error("Could not empty buffer!!");
		}
		currentlyObserving = null;
		hasReadHistory = false;
	}

	/**
	 * Captures the expert's current snapshot and builds an episode from it
	 * 
	 * @return a new episode from the expert's current snapshot
	 * @throws NoMacthForAtomicElementException
	 *             if no match is found in the translation matrix
	 * @throws AttributeNotFoundException 
	 * @throws NotObservingExpertException
	 *             whenever the agent is not observing an expert
	 */
	public Step captureCurrentSnapshotFrom(Pair<VisualSoftwareAgent, TranslationMatrix> expertInfo) throws NoMacthForAtomicElementException, AttributeNotFoundException {
		ttso = TimeMeasurement.getCpuTime();
		Snapshot cSnap = expertInfo.getFirst().getSoftwareImage()
				.getDynamicImage().getCurrentSnapshot();
		Step res = translateSnapshot(cSnap, expertInfo.getSecond());
		ttso = TimeMeasurement.getCpuTime() - ttso;
		return res;
	}

	/**
	 * Translates a snapshot into a step that has the apprentice's
	 * perspective according to the supplied translation matrix
	 * 
	 * @param snapshot
	 *            the snapshot to translate
	 * @return the translated episode or null if no translation matrix is
	 *         available
	 * @throws NotObservingExpertException
	 * @throws NoMacthForAtomicElementException
	 *             when a match for the specified atomic element is not found in
	 *             the translation matrix
	 * @throws AttributeNotFoundException 
	 */
	public Step translateSnapshot(final Snapshot snapshot,
			final TranslationMatrix tMatrix)
			throws NoMacthForAtomicElementException, AttributeNotFoundException {
		List<LbOInstance<?>> instances = new ArrayList<LbOInstance<?>>();
		// change condition source
		for (Condition cond : snapshot.getConditions()) {
			//manipulate individual conditions
			final Pair<Atomic,Translator> condPair = tMatrix.getMatchFor(cond.getSource());
			//if it needs translation then translate
			if(condPair.getSecond() != null){
				//get attribute from apprentice
				ConditionAttribute attribute = apprentice.getAttributeFrom((DataSource)condPair.getFirst(),true);
				//add new instance
				instances.add(new LbOInstance<Object>(attribute,
						condPair.getSecond().translate(cond.getData())));
			}else{
				//get attribute from apprentice
				ConditionAttribute attribute = apprentice.getAttributeFrom((DataSource) condPair.getFirst(),true);
				//add new instance
				instances.add(new LbOInstance<Object>(attribute, cond.getData()));
			}
		}
		// change action instances to correspondents in the apprentice
		List<ActionInstance> behaviour = new ArrayList<ActionInstance>();
		for (ActionInstance ai : snapshot.getBehaviour()) {
			final Pair<Atomic,Translator> actionPair = tMatrix.getMatchFor(ai
					.getAssociatedAction()); 
			final Action newAction = (Action) actionPair.getFirst();
			//TODO: see if this translation works
			if (ai.getParameters() != null && !ai.getParameters().isEmpty()) {
				Translator paramTrans = actionPair.getSecond();
				if(paramTrans != null){//translate action parameters
					List<Object> newParams = new ArrayList<Object>();
					for(Object obj : ai.getParameters()){
						newParams.add(paramTrans.translate(obj));
					}
					behaviour.add(new ActionInstance(newAction,newParams));
				}else{
					behaviour.add(new ActionInstance(newAction, ai.getParameters()));
				}
			} else {
				behaviour.add(new ActionInstance(newAction));
			}
		}
		//add translated behaviour to instaces
		instances.add(new BehaviourInstance(apprentice.getBehaviourAttribute(), behaviour));
		//build a new step
		return new Step(instances, tMatrix);
	}

	/**
	 * Checks if the apprentice is observing an expert
	 * 
	 * @return
	 */
	public synchronized boolean isObservingExpert() {
		return hasReadHistory && currentlyObserving != null;
	}
	
	/**
	 * Checks if the apprentice is observing the indicated expert
	 * 
	 * @return
	 */
	public synchronized boolean isObservingExpert(VisualSoftwareAgent agent) {
		return hasReadHistory && currentlyObserving != null && currentlyObserving.getFirst() == agent;
	}
		

	@Override
	/**
	 * The vision sensor's control mechanism
	 * 
	 * - If the agent is in the learning stage and it is not observing an expert than start observing the expert. 
	 * 
	 * When starting to observe an expert: register observer and read all of its historical record
	 *  
	 * 
	 * @see pt.iscte.pramc.lof.engine.AbstractEngine#mechanismCycle()
	 */
	public void mechanismCycle() {
		//on learning stage
		if(apprentice.isLearningStage()){
			//start observing if not 
			if(currentlyObserving == null){
				Pair<VisualSoftwareAgent, TranslationMatrix> expertInfo = null;
				try {
					ttfe = TimeMeasurement.getCpuTime();
					ttrh = ttfe;
					expertInfo = findExpertToObserve();
					ttfe = TimeMeasurement.getCpuTime() - ttfe;
				} catch (NoSimilarAgentFoundException e1) {
					logger.warn(e1.getMessage());
				} catch (NoMacthForAtomicElementException e1) {
					logger.warn(e1.getMessage());
				}
				if(expertInfo != null){
					//if managed to focus attention on expert, read its history records
					try {
						buffer.addAll(gatherHistoryDataFrom(expertInfo));
//						final List<Step> history = gatherHistoryDataFrom(expertInfo);
//						for(Step stp : history){
//							apprentice.distributeObservedStep(stp);
//						}
						ttrh = TimeMeasurement.getCpuTime() - ttrh;
						logger.info("Successfully gathered history from " + expertInfo.getFirst().getSoftwareImage().getAgentUUID());
					} catch (NoMacthForAtomicElementException e) {
						logger.warn("Failed to read history data. " + e.getMessage());
					} catch (AttributeNotFoundException e) {
						logger.warn("Failed to read history data. " + e.getMessage());
					}
					//finally set the hasReadHistory as true, even if failed to load history
					hasReadHistory = true;
				}
			}
			//empty the buffer
			distributeBufferedSteps();
			//TODO: stop observing after N observations
		}else if(apprentice.isExecutionStage()){//on execution stage
			//if still observing stop the observation
			if(currentlyObserving != null){
				stopObservingExpert();
			}
//			try {
//				wait(500);
//			} catch (InterruptedException e) {
//				logger.error(e);
//			}
		}else{
			logger.error("undefined agent stage");
		}
	}
	
	@Override
	/**
	 * Each time the vision sensor is notified it reads the current snapshot from the associated expert
	 * and stores it in a specialized buffer
	 * @see pt.iscte.pramc.sit.engines.update.Observer#notifyDIUpdate()
	 */
	public synchronized void notifyDIUpdate() {
		if (currentlyObserving == null) {
			logger.warn("Apprentice is not focused on any expert");
		}else{
			try {
				buffer.add(captureCurrentSnapshotFrom(currentlyObserving));
				//logger.info("Expert has done something");
			} catch (NoMacthForAtomicElementException e) {
				logger.error(e.getMessage());
			} catch (AttributeNotFoundException e) {
				logger.error(e.getMessage());
			}
		}
	}
	
	/**
	 * Distributes the observed snaphots that are stored in the buffer
	 * distribution only stars after loading the history data
	 */
	private synchronized void distributeBufferedSteps(){
		if(currentlyObserving == null){
			if(!buffer.isEmpty()){
				logger.error("Buffer with data from a previously observed agent, deleting it");
				buffer.clear();
			}
		}else{ //is currently observing an expert
			if(hasReadHistory && !buffer.isEmpty()){
				//after reading history data distribute steps
				for(Step stp : buffer){
					//logger.info("distributing " + stp);
					try{
						apprentice.store(stp);
					} catch (ArrayIndexOutOfBoundsException e){
						logger.error("Could not store step "+stp, e);
					}
				}
				//clear the buffer
				buffer.clear();
			}
		}
	}
	
	/**
	 * Stops observing an expert when this method is stopped
	 * @see pt.iscte.pramc.lof.engine.LTEngine#stopEngine()
	 */
	@Override
	public synchronized void stopEngine() {
		this.stopObservingExpert();
		super.stopEngine();
	}

	/**
	 * Indicates if the buffer is empty
	 * @return
	 */
	public boolean isBufferEmpty() {
		return buffer.isEmpty();
	}
	
	//--- METRICS------------
	
	/**
	 * @return the time to find an expert to observe
	 */
	public long getTtfe() {
		return ttfe;
	}
	
	/**
	 * @return the time expended comparing the observed expert 
	 */
	public long getTtce() {
		return ttce;
	}
	
	/**
	 * 
	 * @return the time to read history
	 */
	public long getTtrh() {
		return ttrh;
	}
	
	/**
	 * 
	 * @return the time to observe a snapshot
	 */
	public long getTtso() {
		return ttso;
	}
	
	
	
	//---- FOR JUNIT TESTS-------
	
	/**
	 * @return the expert agent being currently observed
	 */
	public VisualSoftwareAgent getCurrentlyObservedAgent(){
		return currentlyObserving != null ? currentlyObserving.getFirst() : null;
	} 
	
}
