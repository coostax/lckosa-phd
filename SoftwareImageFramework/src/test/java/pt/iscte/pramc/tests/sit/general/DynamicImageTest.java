/**
 * Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.tests.sit.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import pt.iscte.pramc.sit.swi.di.DynamicImage;
import pt.iscte.pramc.tests.sit.general.agent.SimpleExpert;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * 
 *         Tests the dynamic image capabilities
 * 
 * @since 10 de Nov de 2011
 * @version 0.1
 */
public class DynamicImageTest {

    /**
     * Sees if the dynamic image is created
     */
    @Test
    public void testCreation() {
	final SimpleExpert sa = new SimpleExpert();
	final DynamicImage di = sa.getSoftwareImage().getDynamicImage();
	assertNotNull(di);
	assertNull(di.getCurrentSnapshot());
	sa.run();
	assertNotNull(di.getCurrentSnapshot());
    }

    /**
     * Sees if the history record is working
     */
    @Test
    public void testHistoryRecord() {
	final SimpleExpert sa = new SimpleExpert();
	final DynamicImage di = sa.getSoftwareImage().getDynamicImage();
	assertNotNull(di);
	assertNotNull(di.getHistoricalRecord());
	assertEquals(0, di.getHistoricalRecord().length);
	sa.run();
	assertEquals(1, di.getHistoricalRecord().length);
	sa.run();
	assertEquals(2, di.getHistoricalRecord().length);
    }

}
