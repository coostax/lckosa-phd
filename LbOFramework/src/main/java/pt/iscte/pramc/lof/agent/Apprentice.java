/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import pt.iscte.pramc.lof.domain.BehaviourAttribute;
import pt.iscte.pramc.lof.domain.ConditionAttribute;
import pt.iscte.pramc.lof.domain.LbOAttribute;
import pt.iscte.pramc.lof.domain.LbOInstance;
import pt.iscte.pramc.lof.domain.Step;
import pt.iscte.pramc.lof.engine.LearningMethod;
import pt.iscte.pramc.lof.engine.common.SolutionProvider;
import pt.iscte.pramc.lof.engine.common.holders.PossibleBehaviour;
import pt.iscte.pramc.lof.engine.eval.DSEvaluator;
import pt.iscte.pramc.lof.engine.eval.EvaluationEngine;
import pt.iscte.pramc.lof.engine.executor.ActionExecutorEngine;
import pt.iscte.pramc.lof.engine.learning.MirrorLearningEngine;
import pt.iscte.pramc.lof.engine.memory.SequentialMemoryEngine;
import pt.iscte.pramc.lof.engine.perception.PerceptionEngine;
import pt.iscte.pramc.lof.engine.vision.SoftwareVisionEngine;
import pt.iscte.pramc.lof.exception.AttributeNotFoundException;
import pt.iscte.pramc.lof.exception.CannotFindProperSolutionException;
import pt.iscte.pramc.sit.agent.VisualSoftwareAgent;
import pt.iscte.pramc.sit.engines.builder.AgentRegistry;
import pt.iscte.pramc.sit.ext.NoAction;
import pt.iscte.pramc.sit.ext.PropertiesLoader;
import pt.iscte.pramc.sit.swi.di.ActionInstance;
import pt.iscte.pramc.sit.swi.si.DataSource;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         The apprentice agent's main class.
 * 
 *         This abstract class allows developers to abstract from the
 *         mechanisms behind learning by observation. Agents extending this
 *         class must be annotated with @VisibleAgent for a correct use of the
 *         software image
 * 
 * 
 * @version 0.1 initial version
 * @version 1.0 introduction of separate control mechanisms for engines that are concurrent threads
 * @version 2.0 normalized the confidence factor and thresholds
 * @since Jul 12, 2011
 */
public abstract class Apprentice implements VisualSoftwareAgent{

	public enum Stage{
		LEARNING,
		EXECUTION
	}
	
	private final Logger logger;
	
	//-----------DEFAULT PROPERTIES----------------
	private static final String DEFAULT_PROPS_FILE = "learning.properties";
	private static final int DEFAULT_CONFIDENCE_INIT = 0;
	private static final double DEFAULT_CONF_TRESHOLD_MIN = 5.0;
	private static final double DEFAULT_CONF_TRESHOLD_MAX = 10.0;
	private static final boolean DEFAULT_GENERALIZATION_FLAG = true;
	private static final String DEFAULT_BASEONT_SRCPATH = "http://embodiedlearningbyobservation.googlecode.com/svn/ontology/softwareimage.owl";
	private static final String DEFAULT_DOMAINONT_SRCPATH = "http://embodiedlearningbyobservation.googlecode.com/svn/ontology/learningscenarios.owl";
	private static final String DEFAULT_BASEONT_NAMESPACE = "http://embodiedlearningbyobservation.googlecode.com/svn/ontology/softwareimage.owl#";
	private static final int DEFAULT_CONDITION_LEARNING_TIMEOUT = 10;
	private static final double DEFAULT_CONFIDENCE_FACTOR = 5.0;
	private static final boolean DEFAULT_USE_NEGATIVE_TRAINING = true;
	/**
	 * use recall in learning and executionEng
	 */
	private static final boolean DEFAULT_RECALL_FLAG = true;
	/**
	 * Store action instance with all data
	 */
	private static final boolean DEFAULT_ACTIONINSTANCE_FLAG = true;
	/**
	 * Use similar behaviours in recall
	 */
	private static final boolean DEFAULT_USE_SIMILAR_BH_FLAG = true;
	
	//-------------- INTERNAL PROPERTIES --------------
	private final String propsFile;
	private final Double thresholdMax;
	private final Double thresholdMin;
	private final int specialCLTimeout;
	private int scltCounter;
	private final double confidenceFactor;
	
	//-------- ENGINES-------
	/**
	 * The software vision engine
	 */
	protected final SoftwareVisionEngine softwareVisionEng;

	/**
	 * The memoryEng method
	 */
	private final SequentialMemoryEngine memoryEng;
	
	/**
	 * The learning method
	 */
	private final MirrorLearningEngine mirrorEng;

	/**
	 * The executionEng engine
	 */
	private final ActionExecutorEngine executionEng;

	/**
	 * The evaluationEng engine
	 */
	private final EvaluationEngine evaluationEng;
	
	/**
	 * The perception engine 
	 */
	private final PerceptionEngine perceptionEng;
	
	/**
	 * The best behaviour selected by the apprentice Used for validation
	 * purposes
	 */
	private List<ActionInstance> bestBehaviour;

	//agent state
	
	/**
	 * Defines the learning algorithm's stage (Learning or Execution)
	 */
	private Stage stage;

	/**
	 * Defines the last sequence of actions performed by the agent
	 */
	private Step lastPerformedStep;

	/**
	 * Determines the case conditions to look for
	 */
	private List<LbOInstance<?>> conditionsToLookFor;
	
	/**
	 * Determines the previous conditions that were looked for
	 */
	private List<LbOInstance<?>> previousConditionsLookedFor;
	
	//--------------constructors-------------------
	
	/**
	 * Default no args constructor
	 * sets default properties file
	 * Initializes all engines
	 */
	public Apprentice() {
		this(DEFAULT_PROPS_FILE);
	}
	
	/**
	 * Constructor wih configurable confidence thresholds and default properties file
	 * @param thMax the value for the maximum confidence threshold
	 * @param thMin the value for the minimum confidence threshold
	 */
	public Apprentice(Double thMax, Double thMin){
		this(DEFAULT_PROPS_FILE,thMax,thMin);
	}
	
	/**
	 * Constructor
	 * 
	 * Initializes the apprentice with all properties provided by the props file
	 * @param propsFileLoc the name and location of the properties file
	 */
	public Apprentice(String propsFileLoc) {
		this(propsFileLoc,
				PropertiesLoader.loadPropertyFrom(propsFileLoc,
				"confidence.treshold.max", DEFAULT_CONF_TRESHOLD_MAX),
				PropertiesLoader.loadPropertyFrom(propsFileLoc,
						"confidence.treshold.min", DEFAULT_CONF_TRESHOLD_MIN));
	}
	
	/**
	 * Constructor
	 * Initializes the apprentice with all properties except the maximum and minimum confidence thresholds
	 *  provided by the props file
	 * @param thMax the value for the maximum confidence threshold
	 * @param thMin the value for the minimum confidence threshold
	 * @param propsFileLoc the name and location of the properties file
	 */
	public Apprentice(String propsFileLoc,Double thMax, Double thMin) {
		this.logger = Logger.getLogger(this.getClass());
		this.propsFile = propsFileLoc;
		this.thresholdMax = thMax;
		this.thresholdMin = thMin;
		this.specialCLTimeout = PropertiesLoader.loadPropertyFrom(propsFile,
				"learningStage.findCondition.timeout", DEFAULT_CONDITION_LEARNING_TIMEOUT);
		this.confidenceFactor = PropertiesLoader.loadPropertyFrom(propsFile,
				"confidence.reduction.factor", DEFAULT_CONFIDENCE_FACTOR);
		this.softwareVisionEng = new SoftwareVisionEngine(this, 
				PropertiesLoader.loadPropertyFrom(propsFile,
				"flag.allow.generalization", DEFAULT_GENERALIZATION_FLAG),
				PropertiesLoader.loadPropertyFrom(propsFile,
						"ontology.base.sourcepath", DEFAULT_BASEONT_SRCPATH),
				PropertiesLoader.loadPropertyFrom(propsFile,
						"ontology.domain.sourcepath", DEFAULT_DOMAINONT_SRCPATH),
				PropertiesLoader.loadPropertyFrom(propsFile,
						"ontology.base.namespace", DEFAULT_BASEONT_NAMESPACE)
				);
		this.stage = Stage.LEARNING;
		this.conditionsToLookFor = null;
		//initialize engines
		this.memoryEng = new SequentialMemoryEngine(this, PropertiesLoader.loadPropertyFrom(propsFile,
				"flag.enableRecall",DEFAULT_RECALL_FLAG), PropertiesLoader.loadPropertyFrom(propsFile,
				"flag.storeActionInstanceParams",DEFAULT_ACTIONINSTANCE_FLAG),
				PropertiesLoader.loadPropertyFrom(propsFile,
						"flag.useSimilarBehaviours",DEFAULT_USE_SIMILAR_BH_FLAG));
		this.mirrorEng = new MirrorLearningEngine(this,propsFile);
		this.executionEng = new ActionExecutorEngine(this);
		this.evaluationEng = new EvaluationEngine(this, memoryEng, mirrorEng,PropertiesLoader.loadPropertyFrom(propsFile,
				"confidence.initValue", DEFAULT_CONFIDENCE_INIT),
				PropertiesLoader.loadPropertyFrom(propsFile,
						"eval.useNegativeTraining", DEFAULT_USE_NEGATIVE_TRAINING));
		this.perceptionEng = new PerceptionEngine(this);
	}
	
	/**
	 * This method is called at each iteration on the simulation.
	 * 
	 * Makes the necessary arrangements for learning by observation to take place
	 * 
	 * Follows the rules:
	 * -if the learning method's confidence is below the thresholdMax
	 * then: - if the agent is not observing then find an expert to observe - if
	 * the agent is observing then make a guess on the action to take by with
	 * the conditions taken from the expert, store the current expert episode
	 * and update the method's confidence according to the accurancy of the
	 * guessed actions Makes the calls for the agent's learning mechanism and
	 * decides what the agent should do
	 */
	public void learningRoutine() {
		//call agent specific routine
		callAgentRoutine();
		//only change stage if not looking for specific conditions
		if(conditionsToLookFor == null){
			// Agent is in learning stage and confidence is above thresholdMax: goto Execution stage
			if (isLearningStage() && getConfidence() > thresholdMax) {
				this.stage = Stage.EXECUTION;
			}else if(isExecutionStage() && getConfidence() < thresholdMin){// goto Learning stage
				this.stage = Stage.LEARNING;
			}
		}else{//see if conditions were found
			if(checkConditionsInMemory(conditionsToLookFor) && getConfidence() > thresholdMax)
				this.stage = Stage.EXECUTION;
		}
		//call engines
		this.softwareVisionEng.run();
		this.perceptionEng.run();
		this.memoryEng.run();
		this.mirrorEng.run();
		this.executionEng.run();
	}

	public abstract void callAgentRoutine();
	
	//------ Memory Related ------------------
	
	/**
	 * Learning stage method
	 * Stores the observed step in the agent's memoryEng
	 * @param step
	 */
	public void store(Step step) {
		//send to memoryEng method
		memoryEng.storeObservedStep(step);
	}

	/**
	 * Sets the last performed step
	 * @param instances the instances that make out this step. Can be null if need to remove last performed step
	 */
	public void setLastPerformedStep(List<LbOInstance<?>> instances) {
		if(instances != null){
			lastPerformedStep = memoryEng.getStepFor(instances);
			if(lastPerformedStep == null){
				logger.warn("The performed step " + instances + " was not found in memoryEng");
			}
		}else{
			lastPerformedStep = null;
		}
	}
	
	//-------------- Perception Related----------------------------
	
	/**
	 * Selects the first set of actions from the list and sends it to the executionEng method
	 * @param currentConditions
	 * @param behaviours the list of behaviours as provided by both engines
	 */
	public void prepareExecution(List<LbOInstance<?>> currentConditions, List<PossibleBehaviour> behaviours){
		if(!behaviours.isEmpty()){
			//get the first of the list of proper behaviours for the current conditions and send to executionEng
			executionEng.prepareExecution(currentConditions,behaviours.get(0));
		}else{
			//reduce confidence by a preset factor
			evaluationEng.reduceConfidence(confidenceFactor);
		}
	}
	
	/**
	 * Checks if the conditions currently observed exist in the agent's memoryEng
	 * If not the agent turns into a special learning stage where it tries to find the conditions
	 * A timeout for this special learning condition is initialized.
	 * 
	 * To prevent this special learning condition from happening put the timeout to zero
	 * @param currentConditions
	 * @return
	 */
	public boolean checkConditionsInMemory(
			List<LbOInstance<?>> currentConditions) {
		if(specialCLTimeout != 0 && !memoryEng.findConditions(currentConditions) && scltCounter <= specialCLTimeout && !currentConditions.equals(previousConditionsLookedFor)){
			//put agent in special learning stage
			this.stage = Stage.LEARNING;
			this.conditionsToLookFor = currentConditions;
			//increase timeout counter
			scltCounter++;
			return false;
		}
		//reset timeout
		scltCounter = 0;
		//copy field to previous
		this.previousConditionsLookedFor = this.conditionsToLookFor;
		//put conditionsToLookFor to null;
		this.conditionsToLookFor = null;
		return true;
	}
	
	//-------------- Mirror/Recall Related----------------------------
	
	/**
	 * Uses the mirrorEng and/or recall method to provide a list of solutions for the given conditions
	 * The solution is provided independently of the condition providence. 
	 * Conditions can come from the agent's perception or from expert observation 
	 * @param conditions the conditions to provide the solution 
	 * @return a list of solutions for the given conditions. A Do nothing if a proper behaviour cannot be found
	 */
	public List<PossibleBehaviour> provideBehavioursFor(List<LbOInstance<?>> conditions){
		List<PossibleBehaviour> behaviours = new ArrayList<PossibleBehaviour>();
		//train mirrorEng method with information in memoryEng
		mirrorEng.update();
		try {
			behaviours.addAll(providedWeightedBehavioursFor(conditions, mirrorEng));
		} catch (CannotFindProperSolutionException e) {
			logger.warn("Could not provide solutions through mirrorEng. "+e.getMessage());
		}
		if(memoryEng.canUseRecall()){
			try{
				//get from recall
				List<PossibleBehaviour> recallbh = providedWeightedBehavioursFor(conditions, memoryEng.getRecall());
				//combine with behaviours from memoryEng
				for(PossibleBehaviour r_pbh : recallbh){
					boolean exists = false;
					//see if already exists in behaviours
					INNER:for(PossibleBehaviour m_pbh : behaviours){
						if(m_pbh.behaviour.equals(r_pbh.behaviour)){
							double prob = m_pbh.probability;
							//remove and add new with combination of both probabilities
							behaviours.remove(m_pbh);
							//add to behaviours
							behaviours.add(new PossibleBehaviour(r_pbh.behaviour, prob + r_pbh.probability, SolutionProvider.BOTH));
							exists = true;
							break INNER;
						}
					}
					if(!exists){//add to behaviours
						behaviours.add(r_pbh);
					}
				}
			} catch (CannotFindProperSolutionException e) {
				logger.warn("Could not provide solutions through recall. "+e.getMessage());
			}
		}
		if(behaviours.isEmpty()){
			behaviours.add(new PossibleBehaviour(Collections.singletonList((ActionInstance)new NoAction()), 1.0,SolutionProvider.NONE));
		}
		//sort all behaviours
		Collections.sort(behaviours,PossibleBehaviour.COMPARE_BY_GREATEST_PROBABILITY);
		return behaviours;
	}
	
	
	private List<PossibleBehaviour> providedWeightedBehavioursFor(List<LbOInstance<?>> conditions,LearningMethod estimator) throws CannotFindProperSolutionException{
		//provide behaviours
		final List<PossibleBehaviour> bhs = estimator.estimateBehaviourFor(conditions);
		final List<PossibleBehaviour> result = new ArrayList<PossibleBehaviour>(bhs.size());
		//multiply by estimator weight on evaluationEng
		double wgt = evaluationEng.getWeightFor(estimator);
		for(PossibleBehaviour pbh : bhs){
			result.add(new PossibleBehaviour(pbh.behaviour, pbh.probability * wgt, pbh.provider));
		}
		return result;
	}
	
	//------------Evaluation related--------
	
	public boolean addPreEvaluator(DSEvaluator evaluator){
		return this.evaluationEng.addPreEvaluator(evaluator);
	}
	
	public boolean addPostEvaluator(DSEvaluator evaluator){
		return this.evaluationEng.addPostEvaluator(evaluator);
	}
	
	//------------ACCESSORS-----------------
	
	/**
	 * Confidence is stored in the evaluationEng method 
	 * @return the apprentice's confidence
	 */
	public double getConfidence() {
		return evaluationEng.getConfidence();
	}

	/**
	 * @return the last actions executed by the agent in the form of a step
	 */
	public Step getLastPerformedStep(){
		return lastPerformedStep;
	}
	
	/**
	 * Sees if the agent is in the learning stage
	 * @return true when in the learning stage
	 */
	public boolean isLearningStage() { 
		return Stage.LEARNING.equals(stage);
	}
	
	/**
	 * Sees if the agent is in the executionEng stage
	 * @return true when in the executionEng stage
	 */
	public boolean isExecutionStage() {
		return Stage.EXECUTION.equals(stage);
	}
	
	public boolean isObserving(){
		return softwareVisionEng.isObservingExpert();
	}
	
	public boolean isObserving(VisualSoftwareAgent expert){
		return softwareVisionEng.isObservingExpert(expert);
	}
	
	public boolean isBufferEmpty(){
		return softwareVisionEng.isBufferEmpty();
	}
	
	/**
	 * finalization method
	 */
	public void shutdown() {
		//stop the vision sensor thread
		softwareVisionEng.stopEngine();
		memoryEng.stopEngine();
		//remove from registry TODO: see if necessary
		AgentRegistry.getAgentRegistry().removeAgent(this);
	}
	
	/**
	 * @return all the information from the agent's memoryEng
	 */
	public List<Step> getAllFromMemory(){
		return this.memoryEng.getAll();
	}
	
	/**
	 * @return the weights of the estimators, [0] - recall, [1] - classification
	 */
	public double[] getEstimatorWeights(){
		return new double[] {
				this.evaluationEng.getRecallMethodWeight(),
				this.evaluationEng.getClassificationMethodWeight()
				};
	}
	
	/**
	 * @return the evaluationEng method
	 */
	public EvaluationEngine getEvaluationEngine() {
		return evaluationEng;
	}
	
	/**
	 * Retrieves a condition attribute from the agent's memoryEng. If the attribute does not exist then it is created
	 * @param first the datasource that describes the attribute
	 * @param allowCreation flag that allows creating a new attribute from the datasource if attribute does not exist
	 * @return the attribute, null if something went wrong
	 * @throws AttributeNotFoundException whenever the datasource has no associated attribute
	 */
	public ConditionAttribute getAttributeFrom(DataSource ds,boolean allowCreation) throws AttributeNotFoundException {
		return this.memoryEng.getAttributeFrom(ds,allowCreation);
	}
	
	/**
	 * Retrieves the behaviour attribute from the agent's memoryEng.  If the attribute does not exist then it is created
	 * @return  the attribute, null if something went wrong
	 */
	public BehaviourAttribute getBehaviourAttribute(){
		return this.memoryEng.getBehaviourAttribute();
	}
	
	// -----------METRICS--------------------------
	
	/**
	 * @return the time to find an expert to observe
	 */
	public long getTtfe() {
		return this.softwareVisionEng.getTtfe();
	}
	
	/**
	 * @return the time expended comparing the observed expert 
	 */
	public long getTtce() {
		return this.softwareVisionEng.getTtce();
	}
	
	/**
	 * 
	 * @return the time to read history
	 */
	public long getTtrh() {
		return this.softwareVisionEng.getTtrh();
	}
	
	/**
	 * 
	 * @return the time to observe a snapshot
	 */
	public long getTtso() {
		return this.softwareVisionEng.getTtso();
	}
	
	// ----------FOR JUNIT TESTS ---------------

	protected SequentialMemoryEngine getMemoryEngine() {
		return memoryEng;
	}
	
	protected MirrorLearningEngine getLearningEngine() {
		return mirrorEng;
	}
	
	/**
	 * @return the currently observed step
	 */
	public Step getCurrentStep() {
		return memoryEng.getLastObserved();
	}

	/**
	 * @return the latest set of possible behaviours calculated by the expert
	 */
//	public List<PossibleBehaviour> getLatestPossibleBehaviours() {
//		return learningEngine.getLatestPossibleBehaviours();
//	}

	public List<ActionInstance> getBestBehaviour() {
		return bestBehaviour;
	}

	public String showStorage(){
		return memoryEng.printStorage();
	}
	
	public boolean isMemorySize(int size){
		return memoryEng.getMemorySize() == size;
	}
	
	public double getAvgObservations(){
		return memoryEng.getAvgObservations();
	}
	
	public int getMaxObservations(){
		return memoryEng.getMaxObservations();
	}
	
	public int getMemorySize() {
		return memoryEng.getMemorySize();
	}
	
	public List<LbOAttribute<?>> getAttributes() {
		return memoryEng.getAttributes();
	}
	
}
