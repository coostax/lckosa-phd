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
 * This annotation reflects the method call for obtaining sensor data. Sensors
 * are implemented in a sensor class defined by the VisibleSensorImpl class.
 * Sensors supply information on domain dataTypes. These datatypes must be
 * specified on the annotation.
 * 
 * The software image automated build tools use this annotation to identify
 * sensors in the software image
 * 
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * @version 1.0
 * @since 04/2010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface VisibleSensor {
    /**
     * A universal description for this sensor update method
     * 
     * @return
     */
    String descriptor() default "";

    /**
     * The name of the field where sensor information is placed
     * 
     * @return
     */
    String fieldName();

    /**
     * A comment for this sensor update method
     */
    String comment() default "";
}
