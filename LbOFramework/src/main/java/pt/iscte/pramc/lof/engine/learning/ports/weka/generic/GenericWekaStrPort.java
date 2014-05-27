/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.learning.ports.weka.generic;

//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.StringTokenizer;
//
//import pt.iscte.pramc.lof.domain.Step;
//import pt.iscte.pramc.lof.engine.common.Settings;
//import pt.iscte.pramc.lof.engine.common.dataset.string.StringDSCell;
//import pt.iscte.pramc.lof.engine.common.dataset.string.StringDataSet;
//import pt.iscte.pramc.lof.engine.common.holders.PossibleBehaviour;
//import pt.iscte.pramc.lof.engine.learning.ports.AlgorithmConnector;
//import pt.iscte.pramc.lof.engine.memory.SequentialStorage;
//import pt.iscte.pramc.lof.exception.CannotFindProperSolutionException;
//import pt.iscte.pramc.sit.swi.di.ActionInstance;
//import pt.iscte.pramc.sit.swi.di.Condition;
//import pt.iscte.pramc.sit.swi.si.Action;
//import weka.classifiers.Classifier;
//import weka.classifiers.bayes.NaiveBayesSimple;
//import weka.core.Attribute;
//import weka.core.FastVector;
//import weka.core.Instance;
//import weka.core.Instances;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         An implementation of a CBR like method using the Weka library
 * 
 *         Attributes are represented as strings
 * 
 * 
 * @deprecated use WekaConnector instead
 * @version 2.0
 * @since Jul 1, 2011
 */
@Deprecated
public class GenericWekaStrPort {//implements AlgorithmConnector {
}
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	// the dataset
//	private final StringDataSet dataset;
//
//	// the list of possible values for each attribute
//	private final Map<String, FastVector> possibleValues;
//
//	// private Class<?extends Classifier> classifyerType;
//
//	private Classifier algorithm;
//
//	/**
//	 * Default constructor
//	 */
//	public GenericWekaStrPort() {
//		dataset = new StringDataSet();
//		possibleValues = new HashMap<String, FastVector>();
//		algorithm = new NaiveBayesSimple();
//	}
//
//	/**
//	 * Constructor with classifier
//	 * 
//	 * @throws IllegalAccessException
//	 * @throws InstantiationException
//	 */
//	public GenericWekaStrPort(Class<? extends Classifier> clazz)
//			throws InstantiationException, IllegalAccessException {
//		dataset = new StringDataSet();
//		possibleValues = new HashMap<String, FastVector>();
//		algorithm = clazz.newInstance();
//	}
//
//	@Deprecated
//	public boolean load(Step toLoad) {
//		// add the snapshot to the dataset
//		StringDSCell[] holder = dataset.addData(toLoad);
//		if (holder != null) {
//			return updatePossibleValues(holder);
//		}
//		System.err.println("ERROR(WEKACBR): could not load snapshot " + toLoad);
//		return false;
//	}
//
//	/**
//	 * @param holder
//	 */
//	private boolean updatePossibleValues(StringDSCell[] holder) {
//		// update list of possible values
//		for (StringDSCell data : holder) {
//			if (data != null) {
//				FastVector values;
//				// get key from possible values
//				if (possibleValues.containsKey(data.attribute)) {
//					values = possibleValues.get(data.attribute);
//				} else {
//					values = new FastVector();
//					possibleValues.put(data.attribute, values);
//				}
//				// if value is not on the list add it
//				if (!values.contains(data.value)) {
//					values.addElement(data.value);
//				}
//			} else {
//				return false;
//			}
//		}
//		return true;
//	}
//
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
//	 * Builds a WEKA dataset from the proper dataset and search for solutions on
//	 * that dataset
//	 * 
//	 * @throws CannotFindProperSolutionException
//	 * @see pt.iscte.pramc.lof.engine.learning.ports.AlgorithmConnector#provideSolutionsFor(java.util.List)
//	 */
//	@Override
//	public List<PossibleBehaviour> provideSolutionsFor(
//			List<Condition> conditions)
//			throws CannotFindProperSolutionException {
//		String[] attributeNames = dataset.getAttributes();
//		// prepare a dataset line from the conditions
//		StringDSCell[] line = dataset.buildDataSetLineFrom(conditions);
//		// update list of possible values with this new instance
//		updatePossibleValues(line);
//
//		// prepare prototype input data
//		FastVector prototypeData = new FastVector(attributeNames.length);
//		// add attributes with linked list of possible values
//		for (String attName : attributeNames) {
//			if (possibleValues.containsKey(attName)) {
//				prototypeData.addElement(new Attribute(attName, possibleValues
//						.get(attName)));
//			} else {
//				System.err
//						.println("WARNING(WEKACBR): attribute "
//								+ attName
//								+ " not in list of possible values, this may be an internal attribute");
//			}
//		}
//		// build training set
//		Instances trainset = new Instances("LBO", prototypeData,
//				Settings.DATASET_CAPACITY);
//		// set class index as the last one
//		trainset.setClassIndex(attributeNames.length - 1);
//		// load examples to the trainset
//		Iterator<StringDSCell[]> it = dataset.getData();
//		while (it.hasNext()) {
//			// build instance
//			Instance trainData = new Instance(attributeNames.length);
//			int count = 0;
//			for (StringDSCell dh : it.next()) {
//				Attribute att = (Attribute) prototypeData.elementAt(count);
//				if (att.name().equals(dh.attribute)) {
//					trainData.setValue(att, dh.value);
//				} else {
//					System.err
//							.println("ERROR(WEKACBR):attributes are different: "
//									+ att.name() + " ; " + dh.attribute);
//				}
//				count++;
//			}
//			// add instance to trainset
//			trainset.add(trainData);
//		}
//		try {
//			// add a decision table majority classifier
//			// Classifier algorithm = classifyerType.newInstance();
//			algorithm.buildClassifier(trainset);
//			// evaluation module
//			// Evaluation eval = new Evaluation(trainset);
//			// eval.evaluateModel(algorithm, trainset);
//			// //print statistics
//			// System.out.println(eval.toSummaryString());
//			//
//			// expected behaviour
//			// build instance from condition list
//			Instance toTest = new Instance(attributeNames.length);
//			// fill instance with attribute values from line
//			for (int i = 0; i != attributeNames.length - 1; ++i) {
//				// attributes are on the same order
//				if (attributeNames[i].equals(line[i].attribute)) {
//					toTest.setValue((Attribute) prototypeData.elementAt(i),
//							line[i].value);
//				} else {
//					System.err
//							.println("ERROR(WEKACBR):attributes are on different order");
//				}
//			}
//			// set the train data to this instance
//			toTest.setDataset(trainset);
//			// get action distribution
//			double[] fdistribution = algorithm.distributionForInstance(toTest);
//			// build list of possible behaviours
//			List<PossibleBehaviour> pbh = new ArrayList<PossibleBehaviour>();
//			for (int i = 0; i != fdistribution.length; ++i) {
//				List<ActionInstance> behaviour = buildBehaviourFrom(
//						conditions,
//						dataset.getBehaviourFrom(possibleValues
//								.get(Settings.BEHAVIOUR_ATTRIBUTE_NAME)
//								.elementAt(i).toString()));
//				pbh.add(new PossibleBehaviour(behaviour, fdistribution[i]));
//			}
//			// order the results
//			Collections.sort(pbh,
//					PossibleBehaviour.COMPARE_BY_GREATEST_PROBABILITY);
//			return pbh;
//			// double bestActionVal = -1;
//			// String selectedActions = "";
//			// for(int i = 0; i != fdistribution.length; ++i){
//			// if(bestActionVal < fdistribution[i]){
//			// //this is a new best action
//			// bestActionVal = fdistribution[i];
//			// selectedActions =
//			// possibleValues.get(LbOParser.BEHAVIOUR_ATTRIBUTE_NAME).elementAt(i).toString();
//			// }
//			// //System.out.println("Action: "+possibleValues.get(LbOParser.BEHAVIOUR_ATTRIBUTE_NAME).elementAt(i)+" with probability: "+fdistribution[i]);
//			// }
//			// //get action array and build action instances from it
//			// return
//			// buildBehaviourFrom(conditions,dataset.getBehaviourFrom(selectedActions));
//		} catch (Exception e) {
//			throw new CannotFindProperSolutionException(e.getMessage());
//		}
//	}
//
//	/**
//	 * Builds a list of action instances from a list of performed actions given
//	 * a set of conditions
//	 * 
//	 * @param conditions
//	 * @param behaviourFrom
//	 * @return
//	 */
//	private List<ActionInstance> buildBehaviourFrom(List<Condition> conditions,
//			List<Action> actions) {
//		List<ActionInstance> result = new ArrayList<ActionInstance>();
//		for (Action action : actions) {
//			if (action.hasParams()) { // is a parametrized action
//				List<Object> parameters = new ArrayList<Object>();
//				for (String param : action.getParams()) {
//					StringTokenizer tokens = new StringTokenizer(param, ".");
//					if (tokens.hasMoreElements()) {
//						// find matching condition
//						String condDescription = tokens.nextToken();
//						Condition match = null;
//						INNER: for (Condition candidate : conditions) {
//							if (candidate.getSource().getDescriptor()
//									.equals(condDescription)) {
//								match = candidate;
//								break INNER;
//							}
//						}
//						// if not found show message
//						if (match != null) {
//							if (tokens.hasMoreElements()) {
//								String att = tokens.nextToken();
//								// find attribute in the condition data
//								Object data = match.getData();
//								try {
//									Field f = data.getClass().getDeclaredField(
//											att);
//									if (f != null) {// the field exists
//										boolean access = f.isAccessible();
//										if (!access) {
//											f.setAccessible(true);
//										}
//										// field is the parameter value
//										parameters.add(f.get(data));
//										f.setAccessible(access);
//									}
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//							} else {// the parameter is the condition data
//									// itself
//								parameters.add(match.getData());
//							}
//						} else {
//							System.err.println("found no match for condition "
//									+ condDescription);
//						}
//					} else {
//						System.err
//								.println("parameter is not correctly stated: "
//										+ param);
//					}
//				}
//				// add a parameterized action
//				result.add(new ActionInstance(action, parameters));
//			} else {
//				// add a no parameter action
//				result.add(new ActionInstance(action));
//			}
//		}
//		return result;
//	}
//
//	/**
//	 * @return information on the used algorithm
//	 */
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
//}
