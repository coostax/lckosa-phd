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
 * This annotation reflects a implementation class of an agent part actuator.
 * The actuator implementation class contains the methods that represent the
 * agent's visible actions. Actuators are characterized by the set of actions
 * they enable to the agent.
 * 
 * The automated build tools look for this tag in the body part implementation
 * class to locate the actuator implementation classes. Once on the actuator
 * implementation class the build tools detect all actions related to this
 * actuator and builds the set of actions for this actuator
 * 
 * The value element can be used for developer comments on the actuator itself
 * 
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * @version 1.1
 * @since 04/2010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface VisibleActuator {
    /**
     * Reserved for developer comments
     * 
     * @return
     */
    String value() default "";
}
