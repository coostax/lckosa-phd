/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine.memory;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import pt.iscte.pramc.lof.domain.ConditionAttribute;
import pt.iscte.pramc.lof.domain.LbOInstance;
import pt.iscte.pramc.lof.domain.Step;
import pt.iscte.pramc.lof.exception.MatchException;
import pt.iscte.pramc.sit.swi.di.ActionInstance;
import pt.iscte.pramc.sit.swi.di.Condition;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * Includes methods to compare and match steps and its internal constituents.
 * 
 * The rules for matching two steps are:
 * 
 * 	- When  the excludeConditions flag is active the two steps match when they have the same behaviour
 * 
 *     - When  the excludeConditions flag is inactive the two steps match when they have the same behaviour and same conditions
 *     
 * Matching returns a double value between 1.0 and 0.0 that indicates the level of similarity between the matched steps 
 *
 * @version 0.1
 * @since Nov 7, 2011
 */
public class StepMatcher {
	
	static Logger logger = Logger.getLogger(StepMatcher.class);
	
	/**
	 * By default the excludeConditions flag is set to false
	 * @param s1 the step to compare
	 * @param s2 the other step to compare
	 * @return 1.0 if the two steps match each other, less than 1.0 if they are different
	 * @throws MatchException 
	 */
	public static double match(Step s1, Step s2) throws MatchException {
		return match(s1,s2,false);
	}
	
	/**
	 * Compares two steps
	 * @param s1 the step to compare
	 * @param s2 the other step to compare
	 * @param excludeConditions flag that excludes (true) or includes (false) the conditions in the match operation
	 * @return 1.0 if the two steps match each other, less than 1.0 if they are different
	 * @throws MatchException 
	 */
	public static double match(Step s1, Step s2, boolean excludeConditions) throws MatchException {
//		try{
			if(excludeConditions){
				return match(s1.getBehaviour(),s2.getBehaviour());
			}
			return (match(s1.getConditions(),s2.getConditions()) + match(s1.getBehaviour(),s2.getBehaviour()))/2.0;
	}
	
	/**
	 * Matches the behaviour instance of a step
	 * @param e1 the first instance to compare
	 * @param e2 the second instance to compare
	 * @throws MatchException 
	 */
	@SuppressWarnings("unchecked")
	public static double match(LbOInstance<?> e1, LbOInstance<?> e2) throws MatchException{
		if(e1.isBehaviour() && e2.isBehaviour()){
			//get the list of action instances that makes up both behaviours and match them
			try{
				List<ActionInstance> list1 = (List<ActionInstance>)e1.getValue();
				List<ActionInstance> list2 = (List<ActionInstance>)e2.getValue();
				if(list1.size() > list2.size()){
					return runMatchOnList(list1,list2,true);
				}
				return runMatchOnList(list2,list1,true);
			}catch(ClassCastException e){
				throw new MatchException("Behaviour not holding a list of action instances: " + e.getMessage());
			}
		}else if(!e1.isBehaviour()){
			throw new MatchException(e1 + " is not a behaviour");
		}else{
			throw new MatchException(e2 + " is not a behaviour");
		}
	}
	
	/**
	 * Matches the condition instances of a step
	 * @param list1
	 * @param list2
	 * @return 1.0 if the conditions/behaviour on both sets match. Less than 1.0 otherwise
	 * @throws MatchException if matching is between different types or if lists are empty
	 */
	public static double match(List<LbOInstance<?>> list1, List<LbOInstance<?>> list2) throws MatchException{
		if(list1.isEmpty() && list2.isEmpty()){
			return 0.0;
		}
		if(list1.size() > list2.size()){
			return runMatchOnList(list1,list2,false);
		}
		return runMatchOnList(list2,list1,false);
	}
	
	/**
	 * Matches the elements in the two lists
	 * list1 must be bigger or equal than list2
	 * @param <E> the type of list that is being matched
	 * @param list1
	 * @param list2
	 * @param withSameOrder flag indicating if the order of the elements in the list is important for matching
	 * @return
	 * @throws MatchException 
	 */
	private static <E> double  runMatchOnList(List<E> list1,
			List<E> list2, boolean withSameOrder) throws MatchException {
		double result = 0;
		if(withSameOrder){
			for(int i = 0; i != list2.size(); ++i){
				result += matchElement(list1.get(i),list2.get(i));
			}
		}else{
			//compare independently of the order of the list
			for(E element1 : list1){
				double max = 0;
				INNER:for(E element2 : list2){
					double val = matchElement(element1,element2);
					if(val > max){
						max = val;
					}
					if(max == 1.0){
						//break cycle if found 100% match
						break INNER;
					}
				}
				//add the maximum value for this comparison
				result += max;
			}
		}
		return result / list1.size();
	}

	/**
	 * Matches two elements that can be action instances,  LbOInstance representing a condition, or the data for a condition
	 * @param e1 
	 * @param e2
	 * @return 1.0 if the Conditions / ActionInstances match, less than 1.0 otherwise
	 * @throws MatchException 
	 */
	private static <E> double matchElement(E e1,E e2) throws MatchException{
		//instance of LbOInstance
		if(e1 instanceof LbOInstance && e2 instanceof LbOInstance){
			//both are conditions
			if(((LbOInstance<?>)e1).isCondition() && ((LbOInstance<?>)e2).isCondition()){
				//two conditions match if their descriptors are the same and the data they contain is the same
				final ConditionAttribute att1 = (ConditionAttribute)((LbOInstance<?>)e1).getAttribute();
				final ConditionAttribute att2 = (ConditionAttribute)((LbOInstance<?>)e2).getAttribute();
				if(att1.getSource().getDescriptor().equals(
						att2.getSource().getDescriptor()) &&
						matchData(((LbOInstance<?>)e1).getValue(),((LbOInstance<?>)e2).getValue())){
					return 1.0;
				}
				return 0;
			}else if(!((LbOInstance<?>)e1).isCondition()){
				throw new MatchException(e1 + " is not a condition instance");
			}else{
				throw new MatchException(e2 + " is not a condition instance");
			}
			//instance of ActionInstance
		}if(e1 instanceof ActionInstance && e2 instanceof ActionInstance){
			return match((ActionInstance)e1,(ActionInstance)e2);
		 //data from condition
		}
		//match the elements direclty
		if(matchData(e1, e2)){
			return 1.0;
		}
		return 0;
	}
			

	/**
	 * Matches two conditions
	 * Two conditions match if they make reference to the same source and have the same data
	 * @param c1
	 * @param c2
	 * @return 1.0 if conditions match, 0.0 otherwise
	 */
	@Deprecated
	public static double match(Condition c1, Condition c2) {
		if(c1.getSource().getDescriptor().equals(
				c2.getSource().getDescriptor()) &&
				matchData(c1.getData(),c2.getData())){
			return 1.0;
		}
		return 0;
	}
	
	/**
	 * Matches two objects, pays attention to arrays of objects
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public static boolean matchData(Object obj1, Object obj2){
		if(obj1.getClass().isArray() && obj2.getClass().isArray()){
			return Arrays.equals((Object[])obj1, (Object[])obj2);
		}
		return obj1.equals(obj2);
	}
	
	/**
	 * Matches two ActionInstances
	 * Two ActionInstances match if they make reference to the same action and have the same instances
	 * @param e1
	 * @param e2
	 * @return 1.0 if ActionInstances match, 0.0 otherwise
	 * @throws MatchException 
	 */
	public static double match(ActionInstance ai1, ActionInstance ai2) throws MatchException {
		if(ai1.getAssociatedAction().getDescriptor().equals(
				ai2.getAssociatedAction().getDescriptor())){
			//both have the same action
			if(ai1.hasParameters()){
				if(ai2.hasParameters() && ai1.getParameters().size() == ai2.getParameters().size()){
					//ai1 has parameters and ai2 has parameters and both have the same number of parameters
					return runMatchOnList(ai1.getParameters(), ai2.getParameters(),true);
				}
				//ai1 has parameters and ai2 has no parameters
				return 0;
			}
			if(ai2.hasParameters()){
				//ai1 has no parameters but ai2 has parameters
				return 0;  
			}
			//ai1 has no parameters and ai2 has no parameters
			return 1.0;
		}
		return 0;
	}
	
}
