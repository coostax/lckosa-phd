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
 *         Annotations for visual attributes. Visual attributes are visual
 *         representations of agent attributes. Visual attribute annotations can
 *         only be associated to fields
 * 
 * @version 0.1
 * @since Jun 16, 2011
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface VisibleAttribute {

    /**
     * @return developer comments
     */
    public String comment() default "";

    /**
     * @return the universal descriptor for this visual attribute
     */
    public String descriptor();
}
