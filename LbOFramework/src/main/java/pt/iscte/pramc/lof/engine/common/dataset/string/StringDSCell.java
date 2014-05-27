/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.common.dataset.string;

//import java.util.List;
//
//import pt.iscte.pramc.lof.domain.tools.BehaviourHolder;
//import pt.iscte.pramc.lof.domain.tools.LbOParser;
//import pt.iscte.pramc.lof.engine.common.Settings;
//import pt.iscte.pramc.sit.ext.NoData;
//import pt.iscte.pramc.sit.swi.di.ActionInstance;
//import pt.iscte.pramc.sit.swi.di.Condition;
//import pt.iscte.pramc.sit.swi.si.Action;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Representation of cell in the string data set. Each cell holds a
 *         condition or a behaviour from a dynamic image snapshot. The cell has
 *         an attribute name (the source of the data) and a JSON string
 *         representation of the data
 * 
 *         This allows a simpler storage method but may cause difficulties when
 *         comparing instances of the same type
 * 
 * @version 0.1
 * @since Jul 11, 2011
 */
@Deprecated
public class StringDSCell {
//	private final LbOParser tools = LbOParser.getParser();
//	// private final Gson json = new GsonBuilder().setExclusionStrategies(new
//	// GSonExclusionStrategy()).serializeNulls().create();
//	private final Class<?> holder;
//	public final String attribute;
//	public final String value;
//	public final int row;
//
//	/**
//	 * Condition dataholder
//	 * 
//	 * @param source
//	 */
//	public StringDSCell(Condition condition, int rowNum) {
//		attribute = LbOParser.buildUniqueAttributeDescriptionFor(condition, rowNum);
//		value = tools.getJSONForConditionData(condition);
//		holder = condition.getData().getClass();
//		this.row = rowNum;
//	}
//
//	/**
//	 * behaviour dataholder
//	 * 
//	 * @param source
//	 */
//	public StringDSCell(List<ActionInstance> behaviour, int rowNum) {
//		attribute = Settings.BEHAVIOUR_ATTRIBUTE_NAME;
//		value = tools.getJSONForBehaviour(behaviour);// json.toJson(new
//															// BehaviourHolder(behaviour));
//		holder = BehaviourHolder.class;
//		this.row = rowNum;
//	}
//
//	/**
//	 * Creates a dataholder with no data, only the attribute description
//	 * 
//	 * @param attName
//	 *            the name for this attribute
//	 */
//	public StringDSCell(String attName, int rowNum) {
//		attribute = attName;
//		value = tools.getJSONFor(new NoData());
//		holder = NoData.class;
//		this.row = rowNum;
//	}
//
//	@Override
//	public String toString() {
//		return attribute + ">>" + value;
//	}
//
//	/**
//	 * @return the condition value instance stored in this cell, if a condition
//	 *         is stored in this cell, null otherwise
//	 */
//	public Object getConditionValue() {
//		if (!holder.equals(BehaviourHolder.class)) {
//			Object val = tools.getFromJSON(value, holder);// json.fromJson(value,
//															// holder);
//			return val;
//		} else {
//			System.err.println("Value " + value + " is not from a condition");
//			return null;
//		}
//	}
//
//	/**
//	 * @return the behaviour stored in this cell if a behaviour is stored in the
//	 *         cell, null otherwise
//	 */
//	public List<Action> getBehaviour() {
//		if (holder.equals(BehaviourHolder.class)) {
//			BehaviourHolder holder = tools.getFromJSON(value,
//					BehaviourHolder.class); // json.fromJson(value,
//											// BehaviourHolder.class);
//			return holder.getPerformedActions();
//		} else {
//			System.err.println("Value " + value + " is not a behaviour");
//			return null;
//		}
//	}
}
