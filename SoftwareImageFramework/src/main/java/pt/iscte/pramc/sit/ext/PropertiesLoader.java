/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.ext;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Loads properties from a properties file
 * 
 * @version 0.1
 * @since Aug 01, 2011
 */
public class PropertiesLoader {

    private static final Logger logger = Logger
	    .getLogger(PropertiesLoader.class);

    /**
     * Reads a property from a file
     * 
     * @param file
     * @param propName
     * @param deflt
     * @return the property or the default value in case of error
     */
    public static String loadPropertyFrom(String file, String propName,
	    String deflt) {
	Properties prop = new Properties();
	try {
	    prop.load(new FileInputStream(file));
	    return prop.getProperty(propName, deflt);
	} catch (Exception e) {
	    logger.error("Could not load property from file: " + file
		    + e.getMessage());
	    return deflt;
	}
    }

    public static Double loadPropertyFrom(String file, String propName,
	    Double deflt) {
	Properties prop = new Properties();
	try {
	    prop.load(new FileInputStream(file));
	    return Double.valueOf(prop.getProperty(propName, deflt.toString()));
	} catch (Exception e) {
	    logger.error("Could not load property from file: " + file
		    + e.getMessage());
	    return deflt;
	}
    }

    public static Boolean loadPropertyFrom(String file, String propName,
	    Boolean deflt) {
	Properties prop = new Properties();
	try {
	    prop.load(new FileInputStream(file));
	    return Boolean
		    .valueOf(prop.getProperty(propName, deflt.toString()));
	} catch (Exception e) {
	    logger.error("Could not load property from file: " + file
		    + e.getMessage());
	    return deflt;
	}
    }

    public static Integer loadPropertyFrom(String file, String propName,
	    Integer deflt) {
	Properties prop = new Properties();
	try {
	    prop.load(new FileInputStream(file));
	    return Integer
		    .valueOf(prop.getProperty(propName, deflt.toString()));
	} catch (Exception e) {
	    logger.error("Could not load property from file: " + file
		    + e.getMessage());
	    return deflt;
	}
    }
}
