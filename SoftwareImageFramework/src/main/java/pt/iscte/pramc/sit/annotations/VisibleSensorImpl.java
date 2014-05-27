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
 * This annotation reflects a field in the agent's body part that represents a
 * sensor implementation. Inside sensor implementations are the information
 * extraction methods that supply the body part with the information gathered by
 * the sensor. Unlike the rest of the annotations, this one does not have a
 * correspondent software image representation. Thus there is no need to add
 * aditional information to the annotation. The value parameter can be used for
 * developer comments Sensors are identified on the annotations written on the
 * information gathering methods in the sensor implementing class
 * 
 * The automated build tools look for this tag in the body part implementation
 * class to locate the sensor implementation classes
 * 
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * @version 1.0
 * @since 10/2010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface VisibleSensorImpl {
    String value() default "";

}
