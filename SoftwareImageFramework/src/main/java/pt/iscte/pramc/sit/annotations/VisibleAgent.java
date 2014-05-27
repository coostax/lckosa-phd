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
 * 
 * This annotation reflects the agent's main implementation class.
 * 
 * The value element can be used for developer comments
 * 
 * The software image automated build tools use this annotation to identify the
 * agent main implementation class and the agent elements.
 * 
 * @author Paulo Costa (coostax@gmail.com)
 * @since 12/2010
 * @version 0.1
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface VisibleAgent {

    public String value() default "";
}
