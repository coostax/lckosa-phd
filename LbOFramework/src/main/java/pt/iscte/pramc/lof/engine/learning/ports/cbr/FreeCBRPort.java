/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.learning.ports.cbr;

import java.util.List;

import pt.iscte.pramc.lof.domain.BehaviourAttribute;
import pt.iscte.pramc.lof.domain.LbOAttribute;
import pt.iscte.pramc.lof.domain.LbOInstance;
import pt.iscte.pramc.lof.domain.Step;
import pt.iscte.pramc.lof.engine.common.holders.PossibleBehaviour;
import pt.iscte.pramc.lof.engine.learning.ports.AlgorithmConnector;
import pt.iscte.pramc.lof.exception.CannotFindProperSolutionException;

/**
 * @author Paulo Costa (coostax@gmail.com)
 * 
 *         Implementation of the learning method with a case based reasoning
 *         vision, using the FreeCBR tool
 * 
 *         FreeCBR uses a case based algorithm to discover solutions for new
 *         problems. It finds the closest match among cases in a case set. Each
 *         case consists of a predefined set of features. The features are
 *         defined by a name and a datatype where the datatype may be any of
 *         String, MultiString, Float, Int and Bool.
 *         
 *         TODO: make it compatible with memory method
 * 
 * @see http://freecbr.sourceforge.net/ for more information on this tool
 */
@SuppressWarnings({ "unused", "serial" })
@Deprecated
public class FreeCBRPort implements AlgorithmConnector {

	@Override
	public List<PossibleBehaviour> provideSolutionsFor(
			List<LbOInstance<?>> conditions, BehaviourAttribute att)
			throws CannotFindProperSolutionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean train(List<LbOAttribute<?>> attributes, List<Step> steps) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return null;
	}
//
//	/**
//	 * The case based reasoning method
//	 */
//	private final CBR method;
//
//	private static final long serialVersionUID = 1L;
//
//	private StringDataSet dataset;
//
//	/**
//	 * Indicator for loaded features;
//	 */
//	private boolean loadedFeatures;
//
//	/**
//	 * Default constructor, initializes the CBR method. Initializes the dataset
//	 * with the provided internal attributes
//	 * 
//	 * @param internalAtts
//	 *            the provided internal attributes
//	 */
//	public FreeCBRPort(String... internalAtts) {
//		method = new CBR();
//		loadedFeatures = false;
//		dataset = new StringDataSet(internalAtts);
//	}
//
//
//	@Deprecated
//	public boolean load(Step toLoad) {
//		// add to dataset
//		StringDSCell[] line = dataset.addData(toLoad);
//		if (line != null) {
//			if (!loadedFeatures) {
//				// load features
//				for (String attribute : dataset.getAttributes()) {
//					method.addFeature(attribute, Feature.FEATURE_TYPE_STRING);
//				}
//				loadedFeatures = true;
//			}
//			// add to method
//			method.addCase(getFeaturesFromDataSetLine(line));
//			return true;
//		} else {
//			return false;
//		}
//
//		// if(toLoad != null){
//		// //if no features loaded load from the conditions
//		// if(!loadedFeatures){
//		// loadedFeatures = loadFeatures(toLoad.getConditions());
//		// }
//		// //load the conditions if they are in the same numberInPot as the
//		// method's
//		// features
//		// if(toLoad.getConditions().size() == (method.getNumFeatures() - 1)){
//		// List<Feature> cbrCase = new ArrayList<Feature>();
//		// //load conditions to case
//		// for(Condition cond : toLoad.getConditions()){
//		// cbrCase.add(new
//		// Feature(LbOParser.getTools().getStringRepForCondition(cond)));
//		// }
//		// //load actions
//		// cbrCase.add(new Feature(xmlParser.toXML(toLoad.getBehaviour())));
//		// //load the case into method
//		// method.addCase(cbrCase.toArray(new Feature[0]));
//		// return true;
//		// }else{
//		// printError("Numeral of conditions ("+toLoad.getConditions().size()+") does not match with numberInPot of features ("+(method.getNumFeatures()
//		// - 1)+")");
//		// }
//		// }else{
//		// printError("Snapshot to load is null");
//		// }
//	}
//
//	private Feature[] getFeaturesFromDataSetLine(StringDSCell[] line) {
//		List<Feature> features = new ArrayList<Feature>();
//		for (StringDSCell holder : line) {
//			features.add(new Feature(holder.value));
//		}
//		return features.toArray(new Feature[0]);
//	}
//
//	// /**
//	// * CBR feature names represent the sensor type that provides this
//	// condition
//	// * @param cond the condition to get the CBR feature name
//	// * @return the feature name for the specific condition.
//	// */
//	// private String getFeatureNameFromCondition(Condition cond) {
//	// return cond.getSource().getClass().getCanonicalName();
//	// }
//	//
//	// /**
//	// * Provides a string representation for this condition to be stored in the
//	// CBR datafile
//	// * @param cond the condition to store in the CBR datafile
//	// * @return a string representation of this condition
//	// */
//	// private String getStringRepForCondition(Condition cond) {
//	// return xmlParser.toXML(cond.getData());
//	// }
//	//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see pt.iscte.pramc.lof.LearningEngine#loadAll(java.util.List)
//	 */
//	// @Override
//	// public void loadAll(Snapshot[] cases) {
//	// for (Snapshot c : cases) {
//	// load(c);
//	// }
//	// }
//
//	/**
//	 * Provides a solution to a specific set of conditions
//	 * 
//	 * @see pt.iscte.pramc.lof.engine.learning.ports.AlgorithmConnector#provideSolutionsFor(java.util.List)
//	 */
//	@Override
//	public List<PossibleBehaviour> provideSolutionsFor(
//			List<Condition> conditions) {
//		final int listSize = conditions.size();
//		if (method != null && method.getNumCases() > 0) {
//			// prepare string array with features from condition
//			String[] fields = dataset.getAttributes();
//			// build search features array
//			String[] search = new String[fields.length];
//			int ind = 0;
//			for (StringDSCell dh : dataset.buildDataSetLineFrom(conditions)) {
//				if (dh != null) {
//					search[ind] = dh.value;
//					++ind;
//				}
//			}
//			// search
//			CBRResult[] res = method.search(fields, search, new int[listSize],
//					new int[listSize], new int[listSize], new int[listSize]);
//			System.out.println("CBR: search results: " + res.length);
//			for (int i = 0; i != res.length; ++i) {
//				System.out.println("Snapshot " + res[i].caseNum + " match "
//						+ res[i].matchPercent);
//			}
//		} else {
//			printError("The case base is empty");
//		}
//		return null;
//	}
//
//	/**
//	 * Prints an error message
//	 * 
//	 * @param string
//	 */
//	private void printError(String string) {
//		System.err.println("CBR Engine ERROR: " + string);
//	}
//
//	/**
//	 * Present the Dataset stored on this method as a table whose columns are
//	 * the Features
//	 * 
//	 * @see java.lang.Object#toString()
//	 */
//	@Override
//	public String toString() {
//		StringBuffer sb = new StringBuffer();
//		sb.append("CBR data array with " + method.getNumCases() + " cases: \n");
//		sb.append("Snapshot No\t");
//		for (int i = 0; i != method.getNumFeatures(); ++i) {
//			sb.append(method.getFeatureName(i));
//			sb.append("\t");
//		}
//		sb.append("\n");
//		for (int i = 0; i != method.getNumCases(); ++i) {
//			sb.append(i);
//			sb.append("\t");
//			for (Feature f : method.getCase(i)) {
//				sb.append(f.getStringValue());
//				sb.append("\t");
//			}
//			sb.append("\n");
//		}
//		return sb.toString();
//	}
//
//	@Override
//	public String getInfo() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean loadAttributes(SequentialStorage storage) {
//		// TODO Auto-generated method stub
//		return false;
//	}

	@Override
	public void setProperties(String propsFile) {
		// TODO Auto-generated method stub
		
	}
}
