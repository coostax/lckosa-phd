/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Represents an agent's internal attribute. Used by the internal
 *         attribute update engine to supply information to the decision engine
 *         Internal attributes can only be associated to fields
 * 
 * @version 0.1
 * @since Jun 14, 2011
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InternalAttribute {

    /**
     * @return developer comments
     */
    public String value() default "";
}
