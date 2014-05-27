/**
 * Copyright 2012 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.engines.security;

import pt.iscte.pramc.sit.annotations.security.AllowAccess;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 *
 * Provides visual software agents and all extensions of this project with simple access control measures
 * 
 * It uses the AllowAccess annotation to limit the access of a method to specific types of classes 
 *
 * @since Jun 12, 2012
 * @version 0.1 - security deals only with methods annotated with AllowAccess
 */
public aspect AccessControl {

	
	/**
	 * Only allows the types specified in the annotation to run the method
	 */
	before(Object executor,AllowAccess tag) : execution ( @AllowAccess * * (..) ) && @annotation(tag) && this(executor){
		boolean secure = false;
		for(Class<?> type : tag.value()){
			if(type.equals(executor.getClass())){
				secure = true;
				break;
			}
		}
		if(!secure){
			throw new SecurityException("Not allowed to access this method");
		}
	}
}
