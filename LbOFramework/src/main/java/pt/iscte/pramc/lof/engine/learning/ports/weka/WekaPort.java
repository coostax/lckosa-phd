/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.learning.ports.weka;

//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import pt.iscte.pramc.lof.domain.Step;
//import pt.iscte.pramc.lof.domain.tools.LbOParser;
//import pt.iscte.pramc.lof.engine.common.Settings;
//import pt.iscte.pramc.lof.engine.common.holders.PossibleBehaviour;
//import pt.iscte.pramc.lof.engine.learning.ports.AlgorithmConnector;
//import pt.iscte.pramc.lof.engine.learning.ports.weka.holders.AttributeHolder;
//import pt.iscte.pramc.sit.swi.di.ActionInstance;
//import pt.iscte.pramc.sit.swi.di.Condition;
//import pt.iscte.pramc.sit.swi.di.Snapshot;
//import weka.classifiers.Classifier;
//import weka.classifiers.lazy.IBk;
//import weka.core.Attribute;
//import weka.core.FastVector;
//import weka.core.Instance;
//import weka.core.Instances;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Connects the Weka library of algorithms to the learning method
 * 
 * @version 1.0 uses XML descriptions for the objects stored in the dataset
 * @since Jul 1, 2011
 * @deprecated use WekaConnector instead
 */
@Deprecated
public abstract class WekaPort{// implements AlgorithmConnector {
	
}
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	private static final int DATASET_CAPACITY = 100;
//
//	// Holder for the possible values for each attribute
//	private final List<AttributeHolder> holder;
//
//	private final List<Snapshot> observedSnapshots;
//
//	private FastVector attList;
//
//	/**
//	 * Default no args constructor
//	 */
//	public WekaPort() {
//		holder = new ArrayList<AttributeHolder>();
//		observedSnapshots = new ArrayList<Snapshot>();
//		attList = null;
//	}
//
//	@Deprecated
//	public boolean load(Step toLoad) {
//		boolean firstTime = holder.isEmpty();
//		// update holder with set of possible conditions
//		updateHolderWith(toLoad.getConditions());
//		// update holder with set of possible behaviours
//		String behaviour = LbOParser.getParser().getXMLRepForBehaviourOn(toLoad);
//		if (firstTime) {
//			// first time running
//			AttributeHolder ah = new AttributeHolder(
//					Settings.BEHAVIOUR_ATTRIBUTE_NAME);
//			ah.possibleValues.addElement(behaviour);
//			holder.add(ah);
//		} else {
//			// not first time running, get last element
//			FastVector possibleBehaviours = holder.get(holder.size() - 1).possibleValues;
//			if (!possibleBehaviours.contains(behaviour)) {// add this behaviour
//				possibleBehaviours.addElement(behaviour);
//			}
//		}
//		// add snapshot to list of observedSnapshots
//		observedSnapshots.add(toLoad);
//
//		return true;
//	}
//
//	/**
//	 * Updates the data holder with new examples from the conditions
//	 * 
//	 * @param conditions
//	 *            the list of conditions to hold
//	 */
//	private void updateHolderWith(List<Condition> conditions) {
//		boolean firstRun = holder.isEmpty();
//		int index = 0;
//		for (Condition cond : conditions) {
//			String attName = LbOParser.buildUniqueAttributeDescriptionFor(cond,
//					index);
//			String val = LbOParser.getParser().getXMLRepForCondition(cond);
//			if (firstRun) {
//				// first time running add new attribute holders
//				AttributeHolder ah = new AttributeHolder(attName);
//				ah.possibleValues.addElement(val);
//				holder.add(ah);
//			} else {
//				// get list of possible values from correspondent position
//				FastVector possibleValues = holder.get(index).possibleValues;
//				// see if value exists in list of possible values
//				if (!possibleValues.contains(val)) {// value is not recognized
//					possibleValues.addElement(val);
//				}
//			}
//			index++;
//		}
//	}
//
//	// @Override
//	// public void loadAll(Snapshot[] cases) {
//	// for (Snapshot s : cases) {
//	// load(s);
//	// }
//	// }
//
//	/**
//	 * Builds a new dataset from the observedSnapshots Retrieves dataset
//	 * attributes from holder
//	 * 
//	 * @see pt.iscte.pramc.lof.engine.learning.ports.AlgorithmConnector#provideSolutionsFor(java.util.List)
//	 */
//	@Override
//	public List<PossibleBehaviour> provideSolutionsFor(
//			List<Condition> conditions) {
//		// add final conditions to holder
//		updateHolderWith(conditions);
//		// prepare the dataset
//		Instances dataSet = prepareDataset();
//		// load snapshots
//		loadSnapshotsTo(dataSet);
//		// build the classifier
//		// TODO: see what is the best one
//		Classifier alg = new IBk();
//		try {
//			alg.buildClassifier(dataSet);
//			Instance testInst = createTestInstanceFrom(conditions);
//			testInst.setDataset(dataSet);
//			// get distribution
//			double[] fdistribution = alg.distributionForInstance(testInst);
//			// int bestSuited = -1;
//			// double max = 0;
//			// build list of possible behaviours
//			List<PossibleBehaviour> pbh = new ArrayList<PossibleBehaviour>();
//			for (int i = 0; i != fdistribution.length; ++i) {
//				List<ActionInstance> ai = LbOParser
//						.getParser()
//						.getActionInstancesFromXml(
//								(String) holder.get(holder.size() - 1).possibleValues
//										.elementAt(i));
//				pbh.add(new PossibleBehaviour(ai, fdistribution[i]));
//			}
//			// order the results
//			Collections.sort(pbh,
//					PossibleBehaviour.COMPARE_BY_GREATEST_PROBABILITY);
//			return pbh;
//			// if(bestSuited != -1){
//			// return LbOParser.getTools().getActionInstancesFromXml(
//			// (String)holder.get(holder.size()-1).possibleValues.elementAt(bestSuited));
//			// }else{
//			// System.err.println("WekaCBR no best suited solution found");
//			// }
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//
//		// //load examples to the algorithm
//		// try {
//		// algorithm.buildClassifier(dataset);
//		// } catch (Exception e1) {
//		// e1.printStackTrace();
//		// return null;
//		// }
//		// //build test instance
//		// Instance testInstance = new Instance(conditions.size());
//		// //load conditions
//		// loadConditionsTo(conditions, testInstance);
//		// //set the dataset
//		// testInstance.setDataset(dataset);
//		// try {
//		// double[] fdistribution =
//		// algorithm.distributionForInstance(testInstance);
//		// System.out.println("------Weka test-------");
//		// for(double d : fdistribution){
//		// System.out.println("dist: " + d);
//		// }
//		// } catch (Exception e) {
//		// e.printStackTrace();
//		// }
//		// return null;
//	}
//
//	/**
//	 * Create a test instance from the provided conditions
//	 * 
//	 * @param conditions
//	 * @return
//	 */
//	private Instance createTestInstanceFrom(List<Condition> conditions) {
//		Instance example = new Instance(conditions.size() + 1);
//		loadConditionsTo(conditions, example);
//		return example;
//	}
//
//	/**
//	 * Loads the stored snapshots in the provided dataset
//	 * 
//	 * @param ds
//	 */
//	private void loadSnapshotsTo(Instances ds) {
//		for (Snapshot snapshot : observedSnapshots) {
//			Instance exp = createExampleFrom(snapshot);
//			ds.add(exp);
//		}
//	}
//
//	/**
//	 * Creates an example instance from a snapshot
//	 * 
//	 * @param toLoad
//	 * @param example
//	 */
//	private Instance createExampleFrom(Snapshot snapshot) {
//		Instance example = new Instance(snapshot.getConditions().size() + 1);
//		// conditions
//		loadConditionsTo(snapshot.getConditions(), example);
//		// behaviour
//		example.setValue((Attribute) attList.lastElement(), LbOParser.getParser()
//				.getXMLRepForBehaviourOn(snapshot));
//		return example;
//	}
//
//	/**
//	 * @param snapshot
//	 * @param example
//	 */
//	private void loadConditionsTo(List<Condition> conditions, Instance example) {
//		int i = 0;
//		for (Condition cond : conditions) {
//			final String value = LbOParser.getParser().getXMLRepForCondition(cond);
//			// see if this condition is already described on the attribute
//			Attribute att = (Attribute) attList.elementAt(i);
//			if (att.name().equals(
//					LbOParser.buildUniqueAttributeDescriptionFor(cond, i))) { // is
//																			// the
//																			// same
//																			// attribute
//				if (att.indexOfValue(value) == -1) { // value not described on
//														// attribute
//					System.err.println("WekaCBR: attribute value not declared "
//							+ att.name() + ": " + value);
//					return;
//				}
//				// set the condition on the example
//				example.setValue(att, value);
//			} else {
//				System.err
//						.println("WekaCBR: something is wrong with attribute names");
//				return;
//			}
//			++i;
//		}
//	}
//
//	/**
//	 * Prepares the dataset from the examples stored in the holder
//	 * 
//	 * @return the dataset to be used by the algorithm
//	 */
//	private Instances prepareDataset() {
//		// prepare the attribute list
//		attList = new FastVector();
//		for (AttributeHolder ah : holder) {
//			attList.addElement(new Attribute(ah.name, ah.possibleValues));
//		}
//		// initialize dataset
//		Instances dataSet = new Instances("LBO", attList, DATASET_CAPACITY);
//		// set the class index as the last attribute
//		dataSet.setClassIndex(attList.size() - 1);
//		return dataSet;
//	}
//
//	@Override
//	public String getInfo() {
//		return "Weka method version 1.0";
//	}
//}
