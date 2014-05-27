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
 *         Annotation for the agent loop method. Visual software agents are
 *         dynamic instances with a main loop method. The identification of this
 *         main loop method allows the software image toolkit engines to be
 *         called.
 * 
 * @version 0.1
 * @since Jun 16, 2011
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AgentLoop {

    String value() default "";
}
