/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.domain.tools;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import pt.iscte.pramc.lof.domain.ConditionAttribute;
import pt.iscte.pramc.lof.domain.LbOInstance;
import pt.iscte.pramc.lof.engine.common.Settings;
import pt.iscte.pramc.lof.engine.common.holders.InternalAttributeHolder;
import pt.iscte.pramc.sit.swi.di.ActionInstance;
import pt.iscte.pramc.sit.swi.di.Condition;
import pt.iscte.pramc.sit.swi.si.Action;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * Provides a set of methods to deal with behaviours
 *
 * @version 0.1
 * @since Nov 24, 2011
 */
public class BehaviourTools {

	/**
	 * Builds the a unique attribute description from a string, associating the
	 * attribute name with its position in the dataset
	 * 
	 * @param internalAtt
	 * @param rowNum
	 * @return
	 * @deprecated use InternalAttributeHolder to represent internal attributes
	 */
	@Deprecated
	public static String buildUniqueAttributeDescriptionFor(String internalAtt,
			int rowNum) {
		return attachRowNumberToAttributeDescription(internalAtt, rowNum);
	}

	/**
	 * Builds the a unique attribute description from an internal attribute
	 * holder, associating the attribute's descriptor with its position in the
	 * dataset
	 * 
	 * @param internalAtt
	 * @param rowNum
	 * @return
	 */
	public static String buildUniqueAttributeDescriptionFor(
			InternalAttributeHolder internalAtt, int rowNum) {
		return attachRowNumberToAttributeDescription(internalAtt.descriptor,
				rowNum);
	}

	/**
	 * Builds a unique attribute description from a condition associated with
	 * the position of this attribute in the dataset
	 * 
	 * @param cond
	 * @return
	 */
	public static String buildUniqueAttributeDescriptionFor(Condition cond,
			int rowNum) {
		return attachRowNumberToAttributeDescription(cond.getSource()
				.getDescriptor(), rowNum);
	}

	/**
	 * Attaches the row numberInPot to the attribute descriptor, allowing unique
	 * names for the attributes Row numbers are added as (00)rowNum:descriptor
	 * 
	 * @param description
	 * @return
	 */
	public static String attachRowNumberToAttributeDescription(
			String description, int rowNum) {
		if (rowNum < 100) {
			if (rowNum < 10) {
				return "00" + rowNum + ":" + description;
			} else {
				return "0" + rowNum + ":" + description;
			}
		} else {
			return rowNum + ":" + description;
		}
	}

	/**
	 * Builds the attribute description for a behaviour
	 * 
	 * @param cond
	 * @return
	 * @deprecated
	 */
	@Deprecated
	public static String buildAttributeDescriptionForBehaviour() {
		return Settings.BEHAVIOUR_ATTRIBUTE_NAME;
	}

	/**
	 * Verifies if the two provided objects represent the similar behaviours Two
	 * behaviours are similar if both have the same actions and if both
	 * performed them in the same order In this case, action comparison does not
	 * include parameter instances, two actions are the same even if they have a
	 * different parameter instances
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static boolean isSimilarBehaviour(Object b1, Object b2) {
		if (b1 instanceof List<?> && b2 instanceof List<?>) {
			for (Object b1Obj : (List<?>) b1) {
				if (b1Obj instanceof ActionInstance) {
					for (Object b2Obj : (List<?>) b2) {
						if (b2Obj instanceof ActionInstance) {
							if (!((ActionInstance) b1Obj).getAssociatedAction()
									.isSimilar(
											((ActionInstance) b2Obj)
													.getAssociatedAction())) {
								return false;
							}
						} else {
							return false;
						}
					}
				} else {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Builds a list of action instances from a list of performed actions given
	 * a set of conditions
	 * 
	 * @param conditions
	 *            the current conditions
	 * @param behaviourFrom
	 *            the list of actions to apply the conditions
	 * @return
	 */
	public static List<ActionInstance> buildBehaviourFrom(
			List<LbOInstance> conditions, List<Action> actions) {
		List<ActionInstance> result = new ArrayList<ActionInstance>();
		for (Action action : actions) {
			if (action.hasParams()) { // is a parametrized action
				List<Object> parameters = new ArrayList<Object>();
				for (String param : action.getParams()) {
					StringTokenizer tokens = new StringTokenizer(param, ".");
					if (tokens.hasMoreElements()) {
						// find matching condition
						String condDescription = tokens.nextToken();
						LbOInstance match = null;
						INNER: for (LbOInstance candidate : conditions) {
							if (candidate.isCondition() && 
									((ConditionAttribute)candidate.getAttribute()).getSource().getDescriptor()
									.equals(condDescription)) {
								match = candidate;
								break INNER;
							}
						}
						// if not found show message
						if (match != null) {
							if (tokens.hasMoreElements()) {
								String att = tokens.nextToken();
								// find attribute in the condition data
								Object data = match.getValue();
								try {
									Field f = data.getClass().getDeclaredField(
											att);
									if (f != null) {// the field exists
										boolean access = f.isAccessible();
										if (!access) {
											f.setAccessible(true);
										}
										// field is the parameter value
										parameters.add(f.get(data));
										f.setAccessible(access);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {// the parameter is the condition data
									// itself
								parameters.add(match.getValue());
							}
						} else {
							System.err.println("found no match for condition "
									+ condDescription);
						}
					} else {
						System.err
								.println("parameter is not correctly stated: "
										+ param);
					}
				}
				// add a parameterized action
				result.add(new ActionInstance(action, parameters));
			} else {
				// add a no parameter action
				result.add(new ActionInstance(action));
			}
		}
		return result;
	}

	/**
	 * Transforms the behaviour according to the conditions. Changes action
	 * instance attributes to make them compliant with the current conditions
	 * 
	 * @param toChange
	 *            the behaviour to change
	 * @param conditions
	 *            the conditions to apply to the behaviour
	 * @return
	 */
	public static List<ActionInstance> adaptBehaviourTo(
			List<ActionInstance> toChange, List<LbOInstance> conditions) {
		List<Action> actions = new ArrayList<Action>(toChange.size());
		for (ActionInstance ai : toChange) {
			actions.add(ai.getAssociatedAction());
		}
		return buildBehaviourFrom(conditions, actions);
	}
	
}
