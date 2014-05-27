/**
 * Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation reflects the method call for executing an agent's action
 * Agent action methods exist in the actuator implementation classes.
 * 
 * Action descriptions must be universal for all agents that use the software
 * image to learn by observation. The value element contains the action
 * identifier, which is a unique string representation for the action
 * 
 * The software image automated build tools use this annotation to identify
 * actions in the software image
 * 
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * @version 1.0
 * @since 04/2010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface VisibleAction {
    /**
     * A universal description for this action
     * 
     * @return
     */
    String descriptor();

    /**
     * A developer comment for this action
     * 
     * @return
     */
    String comment() default "";

    /**
     * A list of values representing the parameters used for this action
     */
    String[] params() default {};
}
