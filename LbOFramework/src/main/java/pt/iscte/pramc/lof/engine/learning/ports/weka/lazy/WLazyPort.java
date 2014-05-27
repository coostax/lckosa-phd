/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.learning.ports.weka.lazy;

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
//import weka.classifiers.lazy.IBk;
//import weka.core.FastVector;
//import weka.core.Instance;
//import weka.core.Instances;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Connects the learning algorithm to a weka lazy algorithm (IbK) using
 *         the Weka library
 * 
 *         Builds its own attributes structure, weka facilities are only built
 *         when providing a solution
 * 
 * @version 0.1
 * @since Jul 1, 2011
 * @deprecated use WekaConnector instead
 */
@Deprecated
public class WLazyPort{ // implements AlgorithmConnector {
	
}
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	// the attributes
//	private final WekaAttHolder attributes;
//	// the dataset where instances are stored
//	private final List<List<DSCell>> dataset;
//
//	private final IBk algorithm;
//
//	/**
//	 * Default constructor. Associates a IbK classifier with k=0
//	 */
//	public WLazyPort() {
//		attributes = new WekaAttHolder();
//		dataset = new ArrayList<List<DSCell>>();
//		algorithm = new IBk();
//	}
//
//	/**
//	 * Associates a IbK classifier with the provided k assures k > 1
//	 */
//	public WLazyPort(int k) {
//		attributes = new WekaAttHolder();
//		dataset = new ArrayList<List<DSCell>>();
//		if (k < 1) {
//			k = 1;
//		}
//		algorithm = new IBk(k);
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
//
//	/**
//	 * 
//	 * @return information on the algorithm
//	 */
//	public String getInfo() {
//		return algorithm.getNearestNeighbourSearchAlgorithm().toString() + "/n"
//				+ algorithm.toString();
//	}
//
//	@Override
//	public boolean loadAttributes(SequentialStorage storage) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//}
