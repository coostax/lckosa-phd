/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.learning.ports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.iscte.pramc.lof.domain.BehaviourAttribute;
import pt.iscte.pramc.lof.domain.ConditionAttribute;
import pt.iscte.pramc.lof.domain.LbOAttribute;
import pt.iscte.pramc.lof.domain.LbOInstance;
import pt.iscte.pramc.lof.domain.Step;
import pt.iscte.pramc.lof.engine.common.SolutionProvider;
import pt.iscte.pramc.lof.engine.common.holders.PossibleBehaviour;
import pt.iscte.pramc.lof.exception.AttributeNotFoundException;
import pt.iscte.pramc.lof.exception.CannotFindProperSolutionException;
import pt.iscte.pramc.sit.ext.PropertiesLoader;
import pt.iscte.pramc.sit.swi.di.ActionInstance;
import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Connects the WEKA library to the mirror learning algorithm
 * 
 * 	The WEKA library gives access to a set of classification algorithms such as KStar, NNGE and others
 * 
 * The connector needs to be directed to a specific classification algorithm to be initialized
 * 
 * The different types of algorithms for WEKA can be found in the LearningAlgorithmType enumerate under the WEKA Library 
 * 
 * @see pt.iscte.pramc.lof.engine.learning.LearningAlgorithmType
 * 
 * @version 2.0 updated to support new holders for agent memory
 * @since Jul 1, 2011
 */
public class WekaConnector implements AlgorithmConnector {
	
	private static final int DEFAULT_CAPACITY = 200;
	
	private int datasetCapacity;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// the attributes
	private FastVector attributes;
	
	/**
	 * the dataset where instances are stored
	 */
	private Instances trainset;

	/**
	 * the classifier used for this algorithm 
	 */
	private final Classifier algorithm;

	/**
	 * The position of the behaviour attribute in the attribute list
	 */
	private int BehaviourAttIndex;
	
	//----------CONSTRUCTORS-------

	/**
	 * Default constructor. Uses a IbK classifier with k=0
	 */
	public WekaConnector() {
		this.attributes = null;
		this.trainset = null;
		this.datasetCapacity = DEFAULT_CAPACITY;
		algorithm = new IBk();
	}

	/**
	 * Constructor with class indicator
	 * @param clazz the class of the classifier to use
	 */
	public WekaConnector(Class<?extends Classifier> clazz) {
		this.attributes = null;
		this.trainset = null; 
		this.datasetCapacity = DEFAULT_CAPACITY;
		Classifier aux = null;
		try {
			aux = clazz.newInstance();
		} catch (InstantiationException e) {
			logger.warn("Could not instantiate "+clazz.getName() + ". Using IBk algorithm");
		} catch (IllegalAccessException e) {
			logger.warn("Could not instantiate "+clazz.getName() + ". Using IBk algorithm");
		}
		algorithm = aux == null ? new IBk() : aux;
	}
	
	/**
	 * Constructor with direct link to classifier
	 * @param classifier the classifier to use
	 */
	public WekaConnector(Classifier classifier) {
		this.attributes = null;
		this.trainset = null;
		this.datasetCapacity = DEFAULT_CAPACITY;
		algorithm = classifier;
	}
	
	/**
	 * Sets the dataset capacity property from a props file
	 */
	@Override
	public void setProperties(String propsFile) {
		if(propsFile != null){
			datasetCapacity = PropertiesLoader.loadPropertyFrom(propsFile,
					"mirror.weka.dataset.capacity", DEFAULT_CAPACITY);
		}
	}
	
	//------------ IMPLEMENTATION of ALGORITHM CONNECTOR ------------
	
	/**
	 * Trains this algorithm with data from the agent's memory
	 * Prepares the attributes list and trainset 
	 * @see pt.iscte.pramc.lof.engine.learning.ports.AlgorithmConnector#train(java.util.List)
	 */
	@Override
	public boolean train(final List<LbOAttribute<?>> atts, final List<Step> steps) {
		BehaviourAttIndex = -1;
		//prepare attribute list
		this.attributes = new FastVector(atts.size());
		for(int i = 0; i != atts.size(); ++i){
			LbOAttribute<?> attribute = atts.get(i);
			if(attribute instanceof BehaviourAttribute){
				BehaviourAttIndex = i;
			}
			attributes.addElement(buildAttributeFrom(attribute));
		}
		if(BehaviourAttIndex == -1){
			if(atts.size() != 0)
				logger.error("Behaviour attribute not found");
			this.attributes = null;
			this.trainset = null;
			return false;
		}else if(BehaviourAttIndex != atts.size()-1){
			logger.warn("Behaviour attribute is not the last one, it should be in " + (atts.size()-1) + " but it is in " +BehaviourAttIndex);
		}
		//prepare train set
		this.trainset = new Instances("LBO",attributes,datasetCapacity);
		//set the class BehaviourAttIndex it is supposed to be the last one
		this.trainset.setClassIndex(BehaviourAttIndex);
		//load the train data
		for(Step stp : steps){
			try{
				this.trainset.add(buildInstanceFrom(stp));
			}catch (AttributeNotFoundException e) {
				logger.error(e.getMessage());
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Builds a list of possible behaviours for the provided list of conditions
	 * @see pt.iscte.pramc.lof.engine.learning.ports.AlgorithmConnector#provideSolutionsFor(java.util.List)
	 */
	@Override
	public List<PossibleBehaviour> provideSolutionsFor(
			List<LbOInstance<?>> conditions, BehaviourAttribute bhAtt)
			throws CannotFindProperSolutionException {
		if(attributes != null && attributes.capacity() > 0){
			if(trainset != null && trainset.numAttributes() > 0){
				try {
					// prepare test instance from conditions
					Instance wTestInst = buildInstanceFrom(conditions);//attributes.getWekaInstance(testInst);
					wTestInst.setDataset(trainset);
					algorithm.buildClassifier(trainset);
					// get behaviour distribution
					double[] fdistribution = algorithm
							.distributionForInstance(wTestInst);
					// build list of possible behaviours
					List<PossibleBehaviour> pbh = new ArrayList<PossibleBehaviour>();
					for (int i = 0; i != fdistribution.length; ++i) {
						//retrieve string rep for behaviour from attribute
						String behaviourRep = ((Attribute)attributes.elementAt(BehaviourAttIndex)).value(i);
						//build behaviour through parser
						List<ActionInstance> behaviour = bhAtt.getValueAt(Integer.valueOf(behaviourRep));//parser.behaviourFromString(behaviourRep,conditions) ;
//								recoverBehaviourIn(i);
//								attributes.getBehaviourAt(i,
//								conditions);
						pbh.add(new PossibleBehaviour(behaviour, fdistribution[i],SolutionProvider.MIRROR));
					}
					// order the results
					Collections.sort(pbh,
							PossibleBehaviour.COMPARE_BY_GREATEST_PROBABILITY);
					return pbh;
				} catch (Exception e) {
					logger.warn("Could not estimate behaviours. Cause: "+ e.getLocalizedMessage());
					throw new CannotFindProperSolutionException(e);
				}
			}
			throw new CannotFindProperSolutionException("no trainset for this learning situation");
		}
		throw new CannotFindProperSolutionException("no attributes set for this learning situation");
	}

	/**
	 * @return information on the algorithm
	 */
	@Override
	public String getInfo() {
		return algorithm.toString();
	}

	/**
	 * Sets a new capacity for the dataset
	 * @param datasetCapacity
	 */
	public void setDatasetCapacity(int datasetCapacity) {
		this.datasetCapacity = datasetCapacity;
	}
	
	//------ HELPER METHODS ----
	
	/**
	 * Builds a WEKA instance from a Step
	 * @param stp
	 * @return a Weka Instance
	 * @throws AttributeNotFoundException when attributes are out of place
	 */
	private Instance buildInstanceFrom(Step stp) throws AttributeNotFoundException {
		//holds the values for this instance
//		double[] values = new double[attributes.size()];
//		for(int i = 0; i != attributes.size(); ++i){
//			values[i] = stp.getInstanceAt(i).getValuePosition();
//		}
//		return new Instance(1.0, values);
		return buildInstanceFrom(stp.getAll());
	}

	/**
	 * Builds a weka instance from a list of instances
	 * @param instances
	 * @return
	 * @throws AttributeNotFoundException  when attributes are out of place
	 */
	private Instance buildInstanceFrom(List<LbOInstance<?>> instances) throws AttributeNotFoundException{
		//holds the values for this instance
		double[] values = new double[attributes.size()];
		for (int i = 0; i != instances.size(); ++i) {
			//see if instances(i) has the same attribute as attributes(i)
			final LbOInstance<?> inst = instances.get(i);
			final Attribute att = (Attribute)attributes.elementAt(i);
			if(inst.getAttribute().getName().equals(att.name())){
				values[i] = inst.getValuePosition();
			}else{
				//logger.warn("Misplaced attribute at " +i+ ": attribute " + att.name() + " is not the same as attribute instance " + inst.getAttribute().getName());
				//find the attribute in attribute list
				boolean found = false;
				for(int j = 0; j != attributes.size(); ++j){
					final Attribute test = (Attribute)attributes.elementAt(j);
					if(test.name().equals(inst.getAttribute().getName())){
						values[j] = inst.getValuePosition();
						found = true;
						break;
					}
				}
				if(!found){
					throw new AttributeNotFoundException("Attribute " + inst.getAttribute() + " is not in this agorithm's attribute list: "+ attributes.toString());
				}
			}
		}
		return new Instance(1.0, values);
	}
	
	/**
	 * Builds a WEKA attribute from a LbOAttribute
	 * Uses JSON string representations to represent the possible values for this attribute 
	 * @param attribute
	 * @return a Weka attribute representing the supplied attribute
	 */
	//@SuppressWarnings("unchecked")
	private Attribute buildAttributeFrom(LbOAttribute<?> attribute) {
		//get all values and store them in a FastVector
		FastVector values = new FastVector();
		for(int i = 0; i != attribute.getNumOfPossibleValues(); ++i){
			if(attribute instanceof ConditionAttribute){
				values.addElement(new Integer(i).toString());//parser.parseObject(attribute.getValueAt(i)));
			}else{//is a behaviour attribute parse the list of action instances
				values.addElement(new Integer(i).toString());//parser.parseBehaviour(((List<ActionInstance>)(attribute.getValueAt(i)))));
			}
		}
		return new Attribute(attribute.getName(), values);
	}

	/**
	 * Provides a set of possible solutions for the provided list of conditions   
	 * @throws CannotFindProperSolutionException
	 *             when the dataset is not big enough to find a solution
	 * @see pt.iscte.pramc.lof.engine.learning.ports.AlgorithmConnector#provideSolutionsFor(java.util.List)
	 */
//	@Override
//	public List<PossibleBehaviour> provideSolutionsFor(
//			List<Condition> conditions)
//			throws CannotFindProperSolutionException {
//		if(this.memory == null){
//			throw new CannotFindProperSolutionException("Cannot access agent's memory");
//		}
//		//holder for condition values
//		//build a list of attributes from the memory method
//		for(int i = 0; i != memory.size();++i){
//			final Step stp = memory.get(i);
//			for(Condition cond : stp.getConditions()){
//			}
//		}
//		
//		// update attributes with provided conditions
//		List<DSCell> testInst = attributes.update(conditions);
//		try {
//			// prepare trainset
//			FastVector wAttributes = attributes.getWekaAttributes();
//			Instances trainset = new Instances("LBO", wAttributes,
//					Settings.DATASET_CAPACITY);
//			// set class indexs
//			trainset.setClassIndex(attributes.getBehaviourIndex());
//			// add instances stored in the dataset to the trainset
//			for (List<DSCell> instances : dataset) {
//				trainset.add(attributes.getWekaInstance(instances));
//			}
//			algorithm.buildClassifier(trainset);
//			// prepare test instance from conditions
//			Instance wTestInst = attributes.getWekaInstance(testInst);
//			wTestInst.setDataset(trainset);
//
//			// get action distribution
//			double[] fdistribution = algorithm
//					.distributionForInstance(wTestInst);
//			// build list of possible behaviours
//			List<PossibleBehaviour> pbh = new ArrayList<PossibleBehaviour>();
//			for (int i = 0; i != fdistribution.length; ++i) {
//				List<ActionInstance> behaviour = attributes.getBehaviourAt(i,
//						conditions);
//				pbh.add(new PossibleBehaviour(behaviour, fdistribution[i]));
//			}
//			// order the results
//			Collections.sort(pbh,
//					PossibleBehaviour.COMPARE_BY_GREATEST_PROBABILITY);
//			return pbh;
//		} catch (Exception e) {
//			throw new CannotFindProperSolutionException(e);
//		}
//	}

}
