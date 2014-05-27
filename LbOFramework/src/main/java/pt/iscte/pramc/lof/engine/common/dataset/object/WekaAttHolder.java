/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.common.dataset.object;

//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Set;
//import java.util.SortedMap;
//import java.util.TreeMap;
//
//import pt.iscte.pramc.lof.domain.tools.BehaviourHolder;
//import pt.iscte.pramc.lof.domain.tools.LbOParser;
//import pt.iscte.pramc.lof.engine.common.Settings;
//import pt.iscte.pramc.lof.engine.common.holders.InternalAttributeHolder;
//import pt.iscte.pramc.lof.exception.LearningEngineException;
//import pt.iscte.pramc.lof.exception.AttributeNotFoundException;
//import pt.iscte.pramc.lof.exception.TypeNotSupportedException;
//import pt.iscte.pramc.sit.swi.di.ActionInstance;
//import pt.iscte.pramc.sit.swi.di.Condition;
//import pt.iscte.pramc.sit.swi.di.Snapshot;
//import weka.core.Attribute;
//import weka.core.FastVector;
//import weka.core.Instance;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Holds the attributes directly as object instances.
 * 
 *         Storing attributes like this increases the complexity of handling
 *         attributes but improves matching between different instaces of the
 *         same type.
 * 
 *         Storing attributes as objects imply each condition to be linked a
 *         numerical attribute. Attributes store all possible instances of
 *         attributes, representing them as a numberInPot that reflects the
 *         position of the attributes on their internal representations. New
 *         attributes can be compared with the attributes stored in the
 *         attribute to see if it matches. If a match is found the new
 *         attributes takes the representation of the match.
 * 
 *         V2.0 The dataset becomes a holder for the Weka attributes. Provides
 *         the methods to prepare the attribute set
 * 
 * @version 1.0 - Dataset with attributes and attribute instances, not direclty
 *          turned to WEKA
 * @version 2.0 - Dataset is build from Weka instances, this class becomes a
 *          holder for the WEKA attribute set
 * @since Jul 11, 2011
 */
@Deprecated
public class WekaAttHolder {
}
//	/**
//	 * Map that holds the attributes and their possible values
//	 */
//	private final SortedMap<String, List<PossibleValue>> attributes;
//
//	private final Comparator<String> ATT_COMPARATOR_BY_NAME = new Comparator<String>() {
//
//		@Override
//		public int compare(String o1, String o2) {
//			if (Settings.BEHAVIOUR_ATTRIBUTE_NAME.equals(o1)
//					&& !Settings.BEHAVIOUR_ATTRIBUTE_NAME.equals(o2)) {
//				return +1;
//			}
//			if (Settings.BEHAVIOUR_ATTRIBUTE_NAME.equals(o2)
//					&& !Settings.BEHAVIOUR_ATTRIBUTE_NAME.equals(o1)) {
//				return -1;
//			}
//			return o1.compareTo(o2);
//		}
//	};
//
//	/**
//	 * Default constructor. Initializes the dataset
//	 * 
//	 * @throws TypeNotSupportedException
//	 */
//	public WekaAttHolder() {
//		this.attributes = new TreeMap<String, List<PossibleValue>>(
//				ATT_COMPARATOR_BY_NAME);
//		// this.bAtt = LbOAttribute.getBehaviourAttribute();
//	}
//
//	/**
//	 * Adds attributes from an observed snapshot and an array of internal
//	 * attributes
//	 * 
//	 * @param snp
//	 *            the observed snapshot
//	 * @param internalAtts
//	 *            the array of agent internal attributes
//	 * @return a dataset instance built from the snapshot
//	 * @throws TypeNotSupportedException
//	 * @throws LearningEngineException
//	 * @throws AttributeNotFoundException
//	 */
//	public List<DSCell> update(final Snapshot snp,
//			InternalAttributeHolder[] internalAtts) {
//		// start with conditions and internal attributes
//		List<DSCell> values = update(snp.getConditions(), internalAtts);
//		// get behaviour
//		BehaviourHolder currentBh = new BehaviourHolder(snp.getBehaviour());
//		try {
//			values.add(buildDSCellFrom(currentBh,
//					Settings.BEHAVIOUR_ATTRIBUTE_NAME));
//		} catch (AttributeNotFoundException e) {
//			e.printStackTrace();
//		}
//		return values;
//	}
//
//	/**
//	 * @param values
//	 * @param toUpdate
//	 * @param attName
//	 * @throws AttributeNotFoundException
//	 */
//	private DSCell buildDSCellFrom(Object toUpdate, final String attName)
//			throws AttributeNotFoundException {
//		List<PossibleValue> possibilities;
//		// see if behaviour attribute is registered
//		if (!attributes.containsKey(attName)) {
//			possibilities = new ArrayList<PossibleValue>();
//			attributes.put(attName, possibilities);
//		} else {
//			possibilities = attributes.get(attName);
//		}
//		// see if current object exists in the list of possible values
//		PossibleValue newVal = new PossibleValue(toUpdate);
//		if (!possibilities.contains(newVal)) {
//			possibilities.add(newVal);
//		}
//		// return instance
//		return new DSCell(attName, possibilities.indexOf(newVal));
//	}
//
//	/**
//	 * Adds attributes from the provided conditions and internal attributes
//	 * 
//	 * @param snp
//	 *            the observed snapshot
//	 * @param internalAtts
//	 *            the array of agent internal attributes
//	 * @return a dataset instance built from the snapshot
//	 * @throws LearningEngineException
//	 * @throws AttributeNotFoundException
//	 */
//	public List<DSCell> update(final List<Condition> conditions,
//			InternalAttributeHolder[] internalAtts) {
//		int rowNum = 0;
//		// start with conditions
//		List<DSCell> values = new ArrayList<DSCell>();
//		for (Condition cond : conditions) {
//			try {
//				values.add(buildDSCellFrom(cond.getData(),
//						LbOParser.buildUniqueAttributeDescriptionFor(cond, rowNum)));
//				rowNum++;
//				// LbOAttribute<Condition> condAtt = new
//				// LbOAttribute<Condition>(cond);
//				// List<Object> possibleVals;
//				// //verify if condition is registered
//				// if(!attributes.containsKey(condAtt)){
//				// possibleVals = new ArrayList<Object>();
//				// attributes.put(condAtt, possibleVals);
//				// }else{
//				// possibleVals = attributes.get(condAtt);
//				// }
//				// //verify if condition attributes is already registered
//				// if(!possibleVals.contains(cond.getData())){
//				// possibleVals.add(cond.getData());
//				// }
//				// //buildAttribute value
//				// values.add(new DSCell(condAtt,
//				// possibleVals.indexOf(cond.getData())));
//				// } catch (TypeNotSupportedException e) {
//				// e.printStackTrace();
//			} catch (AttributeNotFoundException e) {
//				e.printStackTrace();
//			}
//		}
//		// move to internal attributes
//		for (InternalAttributeHolder internal : internalAtts) {
//			try {
//				values.add(buildDSCellFrom(internal.value, LbOParser
//						.buildUniqueAttributeDescriptionFor(internal, rowNum)));
//				// LbOAttribute<InternalAttributeHolder> iAtt = new
//				// LbOAttribute<InternalAttributeHolder>(internal);
//				// List<Object> possibleVals;
//				// if(!attributes.containsKey(iAtt)){
//				// possibleVals = new ArrayList<Object>();
//				// attributes.put(iAtt, possibleVals);
//				// }else{
//				// possibleVals = attributes.get(iAtt);
//				// }
//				// //verify if internal attribute attributes is already
//				// registered
//				// if(!possibleVals.contains(internal.value)){
//				// possibleVals.add(internal.value);
//				// }
//				// //buildAttribute value
//				// values.add(new DSCell(iAtt,
//				// possibleVals.indexOf(internal.value)));
//				// } catch (TypeNotSupportedException e) {
//				// e.printStackTrace();
//			} catch (AttributeNotFoundException e) {
//				e.printStackTrace();
//			}
//		}
//		return values;
//	}
//
//	/**
//	 * Adds attributes from the provided conditions and internal attributes
//	 * 
//	 * @param snp
//	 *            the observed snapshot
//	 * @param internalAtts
//	 *            the array of agent internal attributes
//	 * @return a dataset instance built from the snapshot
//	 * @throws LearningEngineException
//	 * @throws AttributeNotFoundException
//	 */
//	public List<DSCell> update(final List<Condition> conditions) {
//		return update(conditions, new InternalAttributeHolder[0]);
//	}
//
//	/**
//	 * Adds attributes from an observed snapshot
//	 * 
//	 * @param snp
//	 *            the observed snapshot
//	 * @return a dataset instance built from the snapshot
//	 * @throws LearningEngineException
//	 * @throws AttributeNotFoundException
//	 */
//	public List<DSCell> update(final Snapshot snp) {
//		return update(snp, new InternalAttributeHolder[0]);
//	}
//
//	/**
//	 * 
//	 * @return A vector of weka attributes built from the attributes
//	 * @throws LearningEngineException
//	 */
//	public FastVector getWekaAttributes() throws LearningEngineException {
//		Set<String> attSet = attributes.keySet();
//		FastVector result = new FastVector(attSet.size());
//		int pos = 0;
//		for (String att : attSet) {
//			if (Settings.BEHAVIOUR_ATTRIBUTE_NAME.equals(att)) {
//				// certify that behaviour has to be the last element
//				if (pos != attSet.size() - 1) {
//					throw new LearningEngineException(
//							"Behaviour is not the last attribute");
//				}
//			}
//			Attribute wAtt = new Attribute(att,
//					buildListOfPossibleValues(attributes.get(att)));
//			result.addElement(wAtt);
//			++pos;
//		}
//		return result;
//	}
//
//	/**
//	 * Build the WEKA possible behaviours from a list of objects
//	 * 
//	 * @param list
//	 * @return
//	 * @throws LearningEngineException
//	 */
//	public FastVector buildListOfPossibleValues(List<PossibleValue> list)
//			throws LearningEngineException {
//		FastVector result = new FastVector(list.size());
//		for (PossibleValue pVal : list) {
//			result.addElement(pVal.getRepresentation());
//		}
//		return result;
//	}
//
//	/**
//	 * Builds a WEKA instance from an instance of the dataset
//	 * 
//	 * @param wInst
//	 * @return
//	 * @throws LearningEngineException
//	 */
//	public Instance getWekaInstance(List<DSCell> wInst)
//			throws LearningEngineException {
//		double[] values = new double[attributes.size()];
//		for (int i = 0; i != wInst.size(); ++i) {
//			final DSCell cell = wInst.get(i);
//			List<PossibleValue> vals = attributes.get(cell.attribute);
//			if (Settings.BEHAVIOUR_ATTRIBUTE_NAME.equals(cell.attribute)
//					&& i != wInst.size() - 1) {
//				// verify if behaviour attribute is the last attribute
//				throw new LearningEngineException(
//						"Behaviour attribute is not the last attribute");
//			}
//			if (vals != null) {
//				values[i] = cell.intValue;
//			} else {
//				throw new LearningEngineException("Attribute not found: "
//						+ cell.attribute);
//			}
//		}
//		return new Instance(1.0, values);
//	}
//
//	/**
//	 * 
//	 * @return the index of the behaviour attribute
//	 * @throws LearningEngineException
//	 */
//	public int getBehaviourIndex() throws LearningEngineException {
//		String[] atts = attributes.keySet().toArray(new String[0]);
//		if (Settings.BEHAVIOUR_ATTRIBUTE_NAME.equals(atts[atts.length - 1])) {
//			return atts.length - 1;
//		} else {
//			throw new LearningEngineException(
//					"Behaviour attribute is not the last attribute");
//		}
//	}
//
//	/**
//	 * Builds a behaviour stored at the supplied index from the supplied
//	 * conditions
//	 * 
//	 * @param i
//	 * @param conditions
//	 * @return a behaviour
//	 */
//	public List<ActionInstance> getBehaviourAt(int i, List<Condition> conditions) {
//		return ((BehaviourHolder) attributes.get(
//				Settings.BEHAVIOUR_ATTRIBUTE_NAME).get(i).value)
//				.buildBehaviourFrom(conditions);
//	}
//
//}
