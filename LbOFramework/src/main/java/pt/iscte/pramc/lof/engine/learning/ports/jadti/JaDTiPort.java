/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.learning.ports.jadti;

import java.util.List;

import pt.iscte.pramc.lof.domain.BehaviourAttribute;
import pt.iscte.pramc.lof.domain.LbOAttribute;
import pt.iscte.pramc.lof.domain.LbOInstance;
import pt.iscte.pramc.lof.domain.Step;
import pt.iscte.pramc.lof.engine.common.holders.PossibleBehaviour;
import pt.iscte.pramc.lof.engine.learning.ports.AlgorithmConnector;
import pt.iscte.pramc.lof.exception.CannotFindProperSolutionException;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         A DecisionTree algorithm based learning by observation method
 * 
 * TODO: make it compatible with memory method
 * 
 * @version 0.1 using only the visual information to build the tree
 * @since Jul 13, 2011
 */
@SuppressWarnings({ "serial", "unused" })
@Deprecated
public class JaDTiPort implements AlgorithmConnector {

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
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 * Place where observations are stored
//	 */
//	private ItemSet dataset;
//
//	/**
//	 * The attributes used for this learning method
//	 */
//	private AttributeSet attributes;
//
//	/**
//	 * Default constructor
//	 */
//	public JaDTiPort() {
//		this.dataset = null;
//		this.attributes = null;
//	}
//
//	@Deprecated
//	public boolean load(Step toLoad) {
//		if (attributes == null) {
//			// first time loading, prepare attributes and dataset
//			buildAttributeSet(toLoad);
//			if (attributes != null) {
//				dataset = new ItemSet(attributes);
//			} else {
//				System.err.println("JaDTiPort: attributes not correctly built");
//				return false;
//			}
//		}
//		// add snapshot to dataset as an array of attribute values
//		AttributeValue[] values = new AttributeValue[attributes.size()];
//		// start with conditions
//		int num = 0;
//		for (Condition condition : toLoad.getConditions()) {
//			// find the attribute associated to this condition
//			ConditionAttribute att = (ConditionAttribute) attributes
//					.findByName(LbOParser.buildUniqueAttributeDescriptionFor(
//							condition, num));
//			if (att != null) {
//				// get the position for this condition value
//				int position = att.getPositionOf(condition);
//				if (position != -1) {
//					// build a new instance value
//					values[attributes.indexOf(att)] = new KnownSymbolicValue(
//							position);
//				} else {
//					System.err
//							.println("JaDTiPort: could not find position for "
//									+ condition.getData());
//					return false;
//				}
//			} else {
//				System.err.println("JaDTiPort: could not find attribute "
//						+ LbOParser.buildUniqueAttributeDescriptionFor(condition,
//								num));
//				return false;
//			}
//			++num;
//		}
//		// get behaviour attribute
//		BehaviourAttribute att = (BehaviourAttribute) attributes
//				.findByName(Settings.BEHAVIOUR_ATTRIBUTE_NAME);
//		int position = att.getPositionOf(toLoad.getBehaviour());
//		if (position != -1) {
//			// add behaviour in last position
//			values[attributes.size() - 1] = new KnownSymbolicValue(position);
//		} else {
//			System.err.println("JaDTiPort: behaviour not loaded correcly "
//					+ toLoad.getBehaviour());
//			return false;
//		}
//		dataset.add(new Item(values));
//		return true;
//	}
//
//	/**
//	 * Builds the attribute list from the provided conditions Stores the
//	 * attribute list in the attributes attribute
//	 * 
//	 * @param conditions
//	 */
//	private void buildAttributeSet(Snapshot snapshot) {
//		Vector<LbOAttribute<?>> vec = new Vector<LbOAttribute<?>>();
//		int rowNum = 0;
//		for (Condition cond : snapshot.getConditions()) {
//			// get
//			vec.add(new ConditionAttribute(cond, rowNum));
//			++rowNum;
//		}
//		// add the final behaviour attribute
//		vec.add(new BehaviourAttribute(snapshot.getBehaviour()));
//		attributes = new AttributeSet(vec);
//	}
//
//	/**
//	 * @see pt.iscte.pramc.lof.engine.common.AlgorithmConnector#loadAll(pt.iscte.pramc.sit.swi.di.Snapshot[])
//	 */
//	// @Override
//	// public void loadAll(Snapshot[] cases) {
//	// for (Snapshot snap : cases) {
//	// if (snap == null) {
//	// break;
//	// }
//	// load(snap);
//	// }
//	// }
//
//	/**
//	 * @see pt.iscte.pramc.lof.engine.learning.ports.AlgorithmConnector#provideSolutionsFor(java.util.List)
//	 */
//	@Override
//	public List<PossibleBehaviour> provideSolutionsFor(
//			List<Condition> conditions) {
//		// item set for the search
//		List<AttributeValue> search = new ArrayList<AttributeValue>();
//		// building the test attribute vector from the conditions
//		Vector<Attribute> testAttributesVec = new Vector<Attribute>();
//		for (int i = 0; i != conditions.size(); ++i) {
//			ConditionAttribute att = (ConditionAttribute) attributes
//					.attributes().elementAt(i);
//			String condName = LbOParser.buildUniqueAttributeDescriptionFor(
//					conditions.get(i), i);
//			if (att.name().equals(condName)) {
//				// see if conditions already exist in attribute set while
//				int itemNo = att.getPositionOf(conditions.get(i));
//				if (itemNo != -1) {
//					search.add(new KnownSymbolicValue(itemNo));
//				} else {
//					System.err
//							.println("JaDTiPort: something went wrong when getting position");
//					return null;
//				}
//				testAttributesVec.add(att);
//			} else {
//				System.err
//						.println("JaDTiPort: conditions with different name for attribute. Condition name is "
//								+ condName
//								+ "; attribute name is "
//								+ att.name());
//				return null;
//			}
//		}
//		// build test att set
//		AttributeSet testAtts = new AttributeSet(testAttributesVec);
//		// get the goal attribute
//		BehaviourAttribute goalAtt = (BehaviourAttribute) attributes
//				.findByName(Settings.BEHAVIOUR_ATTRIBUTE_NAME);
//
//		// build decision tree
//		DecisionTreeBuilder builder = new DecisionTreeBuilder(dataset,
//				testAtts, goalAtt);
//		DecisionTree tree = builder.build().decisionTree();
//
//		// build the search item
//		double[] distribution = tree.goalValueDistribution(new Item(search
//				.toArray(new AttributeValue[0])));
//
//		// build list of possible values from distribution
//		List<PossibleBehaviour> behaviours = new ArrayList<PossibleBehaviour>();
//		for (int i = 0; i != distribution.length; ++i) {
//			behaviours.add(new PossibleBehaviour(goalAtt.getCurrentBehaviourAt(
//					i, conditions), distribution[i]));
//		}
//		return behaviours;
//	}
//
//	@Override
//	public String getInfo() {
//		return "JaDTI Engine";
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
