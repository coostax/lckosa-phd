/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.ext;

import pt.iscte.pramc.sit.swi.si.Action;
import pt.iscte.pramc.sit.swi.si.Actuator;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * An exclusion strategy to prevent circular reference errors when generating
 * JSON data from behaviours
 * 
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 * @version 0.1
 * @since Jul 12, 2011
 */
public class GSonExclusionStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

	@Override
	/**
	 * prevent the actuator field on action to be serialized
	 */
	public boolean shouldSkipField(FieldAttributes f) {
		if (f.getDeclaringClass() == Action.class
				&& f.getDeclaredClass() == Actuator.class) {
			return true;
		}
		return false;
	}

}
