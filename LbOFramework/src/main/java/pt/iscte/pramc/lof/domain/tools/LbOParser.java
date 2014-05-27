/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.domain.tools;

import java.lang.reflect.Type;
import java.util.List;

import pt.iscte.pramc.lof.domain.LbOInstance;
import pt.iscte.pramc.lof.ext.GSonExclusionStrategy;
import pt.iscte.pramc.sit.ext.PropertiesLoader;
import pt.iscte.pramc.sit.swi.di.ActionInstance;
import pt.iscte.pramc.sit.swi.di.Condition;
import pt.iscte.pramc.sit.swi.di.Snapshot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         A set of parsing tools for the learning method
 * 
 *         Provides the following set of tools:
 * 
 *         - xmlParser to transform objects into strings
 *         
 *         - jsonParser to transform objects into strings
 * 
 * @version 2.1 LbOParser is now focused only on parsing to and from XML and JSON
 * @see BehaviourTools
 * @since Jul 1, 2011
 */
public final class LbOParser {

	/**
	 * @author Paulo Costa (paulo.costa@iste.pt)
	 *
	 * Differentiates the types of parsing supported by this parser
	 *
	 * @version 0.1
	 * @since Nov 24, 2011
	 */
	enum ParserType{
		JSON,
		XML
	}
	
	/**
	 * Indicates the type of parsing used by this parser
	 * The supported types are JSON or XML
	 */
	private static final ParserType TYPE = ParserType.valueOf(new String(
			PropertiesLoader.loadPropertyFrom("learning.properties",
					"learning.parsing.type", "XML")));
	
	/**
	 * Indicates if behaviours are stored as actions or as action instances
	 */
	private final boolean asActions; 

	
	/**
	 * The xml parser
	 */
	private final XStream xmlParser;

	/**
	 * The JSON parser
	 */
	private final Gson jsonParser;

	/**
	 * Constructor
	 */
	public LbOParser(boolean asActions) {
		this.asActions = asActions;
		xmlParser = new XStream(new DomDriver());
		jsonParser = new GsonBuilder()
				.setExclusionStrategies(new GSonExclusionStrategy())
				.serializeNulls().create();
	}

	//---- generic methods
	
	/**
	 * Parse a generic object
	 * @param obj
	 * @return a string representation of this object
	 */
	public String parseObject(Object obj){
		if(ParserType.XML.equals(TYPE)){
			return xmlParser.toXML(obj);
		}else{//by default parse with JSON
			return toJSON(obj);
		}
	}
	
	/**
	 * Turns this string into an object
	 * @param str
	 * @return
	 */
	public <E> E fromString(String str, Class<E> clazz){
		if(ParserType.XML.equals(TYPE)){
			final Object obj = xmlParser.fromXML(str); 
			if(clazz.isInstance(obj)){
				return clazz.cast(obj);
			}else{
				return null;
			}
		}else{//default for JSON
			return fromJSON(str, clazz);
		}
	}
	
	/**
	 * Turns this string into an object
	 * @param str
	 * @return
	 */
	public <E> E fromString(String str, Type type){
		if(ParserType.XML.equals(TYPE)){
			final Object obj = xmlParser.fromXML(str); 
			return null;
		}else{//default for JSON
			return fromJSON(str, type);
		}
	}
	
	/**
	 * Special parse methods for behaviours 
	 */
	public String parseBehaviour(List<ActionInstance> behaviour){
		if(ParserType.XML.equals(TYPE)){
			return xmlParser.toXML(new BehaviourHolder(behaviour,asActions));
		}else{//default for JSON
			return toJSON(new BehaviourHolder(behaviour,asActions)); //getJSONForBehaviour(behaviour,asActions);
		}
	}
	
	/**
	 * Special build behaviour from string without condition
	 */
	public List<ActionInstance> behaviourFromString(String str){
		if(ParserType.XML.equals(TYPE)){
			return ((BehaviourHolder)xmlParser.fromXML(str)).getBehaviour();
		}else{//default for JSON
			return fromJSON(str, BehaviourHolder.class).getBehaviour(); //getBehaviourFromJSON(str).getBehaviour();
		}
	}
	
	/**
	 * Special build behaviour from string with condition
	 */
	public List<ActionInstance> behaviourFromString(String str, List<LbOInstance> conditions){
		if(ParserType.XML.equals(TYPE)){
			return ((BehaviourHolder)xmlParser.fromXML(str)).getBehaviour(conditions);
		}else{//default for JSON
			return fromJSON(str, BehaviourHolder.class).getBehaviour(conditions);//getBehaviourFromJSON(str).getBehaviour(conditions);
		}
	}
	
	// --- XML methods

	/**
	 * Provides a string representation for this condition to be stored in the
	 * learning datafile
	 * 
	 * @param cond
	 *            the condition to store in the CBR datafile
	 * @return a string representation of this condition
	 */
	@Deprecated
	private String getXMLRepForCondition(Condition cond) {
		return xmlParser.toXML(cond.getData());
	}

	/**
	 * Provides a string representation for this condition to be stored on the
	 * learning datafile
	 * 
	 * @param snap
	 * @return
	 */
	@Deprecated
	private String getXMLRepForBehaviourOn(Snapshot snap) {
		return xmlParser.toXML(snap.getBehaviour());
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	private List<ActionInstance> getActionInstancesFromXml(String xml) {
		Object obj = xmlParser.fromXML(xml);
		if (obj instanceof List<?>) {
			return (List<ActionInstance>) obj;
		} else {
			return null;
		}
	}

	// ----JSON methods
	/**
	 * @param condition
	 *            the condition from were the data needs a JSON representation
	 * @return a string representation for the condition data
	 */
	@Deprecated
	private String getJSONForConditionData(Condition condition) {
		return jsonParser.toJson(condition.getData());
	}

	/**
	 * This method uses behaviour simplification when building the JSON
	 * representation Behaviours are represented by their actions with no
	 * parameters.
	 * @param behaviour
	 *            the behaviour to build the JSON representation
	 * @return a string representation for the behaviour
	 */
//	private String getJSONForBehaviour(List<ActionInstance> behaviour, boolean asActions) {
//		return jsonParser.toJson();
//	}
	
//	private BehaviourHolder getBehaviourFromJSON(String behaviour){
//		return jsonParser.fromJson(behaviour, BehaviourHolder.class);
//	}

	/**
	 * Provides JSON representation to a generic object
	 * 
	 * @param data
	 *            the data from where to get the JSON representation
	 * @return a string representation for the data
	 */
	protected String toJSON(Object data) {
		return jsonParser.toJson(data);
	}

	protected <T> T fromJSON(String rep, Class<T> clazz) {
		return jsonParser.fromJson(rep, clazz);
	}

	protected <T> T fromJSON(String rep, Type type) {
		return jsonParser.fromJson(rep, type);
	}
	
	// --end of JSON methods

}
