/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.ontology;

import org.apache.log4j.Logger;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Generic interface for the translator classes that translate
 *         sensor/visual attribute outputs from one type to another
 * 
 * @version 0.1
 * @since Oct 26, 2011
 */
public interface Translator {

    static Logger logger = Logger.getLogger(Translator.class);

    public Object translate(Object data);

}
