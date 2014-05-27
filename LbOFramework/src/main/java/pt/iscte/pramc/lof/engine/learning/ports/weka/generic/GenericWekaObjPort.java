/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.learning.ports.weka.generic;

//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import pt.iscte.pramc.lof.domain.Step;
//import pt.iscte.pramc.lof.engine.common.Settings;
//import pt.iscte.pramc.lof.engine.common.dataset.object.DSCell;
//import pt.iscte.pramc.lof.engine.common.dataset.object.WekaAttHolder;
//import pt.iscte.pramc.lof.engine.common.holders.PossibleBehaviour;
//import pt.iscte.pramc.lof.engine.learning.ports.AlgorithmConnector;
//import pt.iscte.pramc.lof.engine.memory.SequentialStorage;
//import pt.iscte.pramc.lof.exception.CannotFindProperSolutionException;
//import pt.iscte.pramc.sit.swi.di.ActionInstance;
//import pt.iscte.pramc.sit.swi.di.Condition;
//import weka.classifiers.Classifier;
//import weka.classifiers.bayes.NaiveBayesSimple;
//import weka.core.FastVector;
//import weka.core.Instance;
//import weka.core.Instances;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         An implementation of a CBR like method using the Weka library
 * 
 *         Attributes are represented as objects
 *
 * 
 * @version 3.0
 * @since Jul 1, 2011
 * @deprecated use WekaConnector instead
 */
@Deprecated
public class GenericWekaObjPort{ //implements AlgorithmConnector {
}
//
///**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	// the attributes
//	private final WekaAttHolder attributes;
//	// the dataset where instances are stored
//	private final List<List<DSCell>> dataset;
//
//	private final Classifier algorithm;
//
//	/**
//	 * Default constructor. Associates a NayveBayesSimple classifier
//	 */
//	public GenericWekaObjPort() {
//		attributes = new WekaAttHolder();
//		dataset = new ArrayList<List<DSCell>>();
//		algorithm = new NaiveBayesSimple();
//	}
//
//	/**
//	 * Associates the classifier
//	 * 
//	 * @throws IllegalAccessException
//	 * @throws InstantiationException
//	 */
//	public GenericWekaObjPort(Class<? extends Classifier> clazz)
//			throws InstantiationException, IllegalAccessException {
//		attributes = new WekaAttHolder();
//		dataset = new ArrayList<List<DSCell>>();
//		algorithm = clazz.newInstance();
//	}
//
//	@Deprecated
//	public boolean load(Step toLoad) {
//		// update dataset and attribute holder
//		return dataset.add(attributes.update(toLoad));
//	}
//
//	/**
//	 * @param holder
//	 */
//	// @Override
//	// public void loadAll(Snapshot[] cases) {
//	// for (Snapshot s : cases) {
//	// if (s == null) {
//	// break;
//	// }
//	// load(s);
//	// }
//	// }
//
//	/**
//	 * Builds WEKA attributes from the attribute holder and the stored dataset
//	 * 
//	 * @throws CannotFindProperSolutionException
//	 *             when the dataset is not big enough to find a solution
//	 * @see pt.iscte.pramc.lof.engine.learning.ports.AlgorithmConnector#provideSolutionsFor(java.util.List)
//	 */
//	@Override
//	public List<PossibleBehaviour> provideSolutionsFor(
//			List<Condition> conditions)
//			throws CannotFindProperSolutionException {
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
//			throw new CannotFindProperSolutionException(e.getMessage()
//					+ " Cause: " + e.getCause());
//		}
//		// //ensure condition values are represented in the attribute's list of
//		// possible values
//		// ensureConditionRepresentation(conditions);
//		// //build vector with weka attributes
//		// FastVector wekaAtts = new
//		// FastVector(attributes.getAttributes().length);
//		// for(LbOAttribute engineAtt : attributes.getAttributes()){
//		// wekaAtts.addElement(LbOParser.getTools().convertToWekaAttribute(engineAtt));
//		// }
//		// //build the weka trainset
//		// Instances trainset = new Instances("LBO", wekaAtts,
//		// DATASET_CAPACITY);
//		// //set the class index
//		// trainset.setClassIndex(attributes.getBehaviourIndex());
//		// //fill the trainset
//		// final Iterator<DSCell[]> it = attributes.getData();
//		// while(it.hasNext()){
//		// trainset.add(LbOParser.getTools().convertToWekaInstance(it.next(),wekaAtts));
//		// }
//		// try {
//		// //create a RuleAssociation classifier
//		// ConjunctiveRule algorithm = new ConjunctiveRule();
//		// algorithm.buildClassifier(trainset);
//		// //prepare test instance from conditions
//		// Instance testInst = LbOParser.getTools().convertToWekaInstance(
//		// attributes.buildDataSetLineFrom(conditions, new
//		// InternalAttributeHolder[0]),
//		// wekaAtts);
//		//
//		// //get action distribution
//		// double[] fdistribution = algorithm.distributionForInstance(testInst);
//		// //build list of possible behaviours
//		// List<PossibleBehaviour> pbh = new ArrayList<PossibleBehaviour>();
//		// for(int i = 0; i != fdistribution.length; ++i){
//		// List<ActionInstance> behaviour =
//		// attributes.getBehaviourAt(i,conditions);
//		// pbh.add(new PossibleBehaviour(behaviour, fdistribution[i]));
//		// }
//		// //order the results
//		// Collections.sort(pbh,
//		// PossibleBehaviour.COMPARE_BY_GREATEST_PROBABILITY);
//		// return pbh;
//		// } catch (Exception e) {
//		// e.printStackTrace();
//		// throw new CannotFindProperSolutionException(e.getMessage());
//		// }
//	}
//
//	@Override
//	public String getInfo() {
//		return algorithm.toString();
//	}
//
//	@Override
//	public boolean loadAttributes(SequentialStorage storage) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	/**
//	 * Ensures the provided condition values are represented in the resective
//	 * attribute's list of possible values
//	 * 
//	 * @param conditions
//	 */
//	// private void ensureConditionRepresentation(List<Condition> conditions) {
//	// for(Condition cond : conditions){
//	// LbOAttribute att = attributes.getAttributeForCondition(cond);
//	// if(att != null){
//	// att.getIndexOf(cond.getData());
//	// }else{
//	// System.err.println("WekaConnector: attribute not found for condition " +
//	// cond);
//	// }
//	// }
//	// }
//
//	/**
//	 * Builds the test instance for Weka
//	 * 
//	 * @param conditions
//	 * @param attLength
//	 * @param trainset
//	 * @return
//	 */
//	// private Instance buildWekaTestInstance(List<Condition> conditions,
//	// Instances trainset) {
//	// final int attLength = attributes.getAttributes().length;
//	// Instance toTest = new Instance(attLength);
//	// for(int i = 0; i != conditions.size(); ++i){
//	// LbOAttribute att = attributes.getAttributes()[i];
//	// //add value from condition if value does not exist
//	// int value = att.getIndexOf(conditions.get(i).getData());
//	// final Attribute wAtt = new
//	// Attribute(att.getName(),LbOParser.getTools().buildFastVectorFrom(att.getAllPossibleValues()));
//	// //fill test instance with values
//	// toTest.setValue(wAtt, value);
//	// }
//	// //set the train data to this instance
//	// toTest.setDataset(trainset);
//	// return toTest;
//	// }
//
//	/**
//	 * Builds the attribute set for Weka
//	 * 
//	 * @param conditions
//	 * @param attLength
//	 * @return
//	 * @throws CannotFindProperSolutionException
//	 */
//	// private FastVector buildWekaAttributes(List<Condition> conditions) throws
//	// CannotFindProperSolutionException {
//	// final int attLength = attributes.getAttributes().length;
//	// FastVector wekaAtts = new FastVector(attLength);
//	// //build the weka proper attribute list from the attributes while adding
//	// the new attribute values from the conditions
//	// for(int i = 0; i != attLength; ++i){
//	// LbOAttribute dsAtt = attributes.getAttributes()[i];
//	// //certify conditions if passed
//	// if(conditions != null){
//	// if(i < conditions.size()){
//	// final Condition cond = conditions.get(i);
//	// if(dsAtt.isAssociatedTo(cond)){
//	// //add value from condition if value does not exist
//	// dsAtt.getIndexOf(conditions.get(i).getData());
//	// }else{
//	// throw new CannotFindProperSolutionException("WekaConnector: Attribute "+
//	// dsAtt.getName() + " is not associated to condition " + cond);
//	// }
//	// }
//	// }
//	// //internal attributes and behaviour
//	// try{
//	// final Attribute wAtt = new
//	// Attribute(dsAtt.getName(),LbOParser.getTools().buildFastVectorFrom(dsAtt.getAllPossibleValues()));
//	// wekaAtts.addElement(wAtt);
//	// }catch (Exception e) {
//	// System.err.println(">>>>>:"+dsAtt.getAllPossibleValues());
//	// }
//	// }
//	// return wekaAtts;
//	// }
//
//	/**
//	 * Fills the weka trainset
//	 * 
//	 * @param attLength
//	 * @param wekaAtts
//	 * @return
//	 * @throws CannotFindProperSolutionException
//	 */
//	// private Instances buildWekaTrainSet(FastVector wekaAtts)
//	// throws CannotFindProperSolutionException {
//	// final int attLength = attributes.getAttributes().length;
//	// //build training set
//	// Instances trainset = new Instances("LBO",wekaAtts,DATASET_CAPACITY);
//	// //set class index as the last one
//	// trainset.setClassIndex(attLength-1);
//	// //load examples to the trainset
//	// Iterator<DSCell[]> it = attributes.getData();
//	// while(it.hasNext()){
//	// //build instance
//	// Instance trainData = new Instance(attLength);
//	// int count = 0;
//	// for(DSCell attValue : it.next()){
//	// Attribute att = (Attribute)wekaAtts.elementAt(count);
//	// if(att.name().equals(attValue.getAttributeName())){
//	// trainData.setValue(att, attValue.getValue());
//	// }else{
//	// throw new
//	// CannotFindProperSolutionException("Attributes are different: "+att.name()+" ; "+attValue.getAttributeName());
//	// }
//	// count++;
//	// }
//	// //add instance to trainset
//	// trainset.add(trainData);
//	// }
//	// return trainset;
//	// }
//
//	// public FastVector getRuleSet() throws CannotFindProperSolutionException{
//	// FastVector wekaAtts = new FastVector(attLength);
//	// //build weka test attributes
//	// for(int i = 0; i != attLength; ++i){
//	// final LbOAttribute att = attributes.getAttributes()[i];
//	// final Attribute wAtt = new Attribute(att.getName());
//	// wekaAtts.addElement(wAtt);
//	// }
//	// //build weka attribute set
//	// FastVector wekaAtts = buildWekaAttributes(null);
//	// //build weka train set
//	// Instances trainset = buildWekaTrainSet(wekaAtts);
//	// //create a RuleAssociation classifier
//	// JRip algorithm = new JRip();
//	// try {
//	// algorithm.buildClassifier(trainset);
//	// return algorithm.getRuleset();
//	// } catch (Exception e) {
//	// throw new CannotFindProperSolutionException(e.getMessage());
//	// }
//	//
//	// }
//
//}
