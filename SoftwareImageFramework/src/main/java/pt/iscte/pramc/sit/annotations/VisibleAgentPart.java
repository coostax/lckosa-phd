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
 * This annotation reflects the agent part main implementation class. If this
 * agent part uses other implementation classes they are taken as internal body
 * parts.
 * 
 * agent parts belong to one agent only and are distinguished by their
 * constitution.
 * 
 * The value element can be used for developer comments
 * 
 * The software image automated build tools use this annotation to identify
 * agent part implementation classes and elements inside an agent part
 * implementation class that represent the agent parts.
 * 
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * @version 1.0
 * @since 04/2010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface VisibleAgentPart {

    String value() default "";

}
