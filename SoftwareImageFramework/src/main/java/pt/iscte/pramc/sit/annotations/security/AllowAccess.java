/**
 * Copyright 2012 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.annotations.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * 
 *         Provides a security feature for the specific methods in visual
 *         software agents. The methods annotated with this can only be accessed
 *         by the authorized types. All other accesses are blocked, throwing an
 *         exception
 * 
 * @since Jun 12, 2012
 * @version 0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AllowAccess {

    /**
     * @return the types authorized to access this method
     */
    Class<?>[] value();

}
