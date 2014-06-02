/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.tests.sit.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

import pt.iscte.pramc.sit.engines.perception.AgentPerception;
import pt.iscte.pramc.sit.ext.NoData;
import pt.iscte.pramc.sit.swi.di.ActionInstance;
import pt.iscte.pramc.sit.swi.di.Condition;
import pt.iscte.pramc.sit.swi.di.DynamicImage;
import pt.iscte.pramc.sit.swi.di.Snapshot;
import pt.iscte.pramc.sit.swi.si.AgentPart;
import pt.iscte.pramc.sit.swi.si.Sensor;
import pt.iscte.pramc.sit.swi.si.StaticImage;
import pt.iscte.pramc.tests.sit.general.agent.SimpleExpert;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         JUnit tests for the software image
 * 
 * @version 0.1
 * @since Oct 21, 2011
 */
public class SoftwareImageTest {

    /**
     * Tests the creation of a simple agent
     */
    @Test
    public void testSimpleAgentCreation() {
	final SimpleExpert sa = new SimpleExpert();
	assertNotNull(sa);
	assertNotNull(sa.getSoftwareImage());
    }

    /**
     * Tests if the created static image has the desired organization
     */
    @Test
    public void testStaticImage() {
	final SimpleExpert sa = new SimpleExpert();
	final StaticImage si = sa.getSoftwareImage().getStaticImage();
	assertNotNull(si);
	// has to have two agent parts
	assertEquals(2, si.getAgentParts().size());
	final AgentPart ap1 = si.getAgentParts().get(0);
	final AgentPart ap2 = si.getAgentParts().get(1);
	assertNotNull(ap1);
	assertNotNull(ap2);
	// sensors on part one
	final List<Sensor> sensors = ap1.getSensors();
	assertEquals(2, sensors.size());
	boolean found = false;
	for (final Sensor s : sensors) {
	    if ("obtainData.part1".equals(s.getDescriptor())) {
		found = true;
		break;
	    }
	}
	assert found;
	// actions on part two
	assertEquals(1, ap2.getActuators().size());
	assertEquals(1, ap2.getActuators().get(0).getActionSet().size());
	assertEquals("action.do",
		ap2.getActuators().get(0).getActionSet().get(0).getDescriptor());
    }

    /**
     * Tests the agent's capability of updating its dynamic image
     */
    @Test
    public void testDynamicImage() {
	final SimpleExpert sa = new SimpleExpert();
	// the initial value of the current snapshot must be null
	assertNull(sa.getSoftwareImage().getDynamicImage().getCurrentSnapshot());
	sa.run();
	final Snapshot current = sa.getSoftwareImage().getDynamicImage()
		.getCurrentSnapshot();
	// the snapshot cannot be null
	assertNotNull(current);
	// validate conditions
	Object data = null;
	for (final Condition cond : current.getConditions()) {
	    if ("obtainData.part1".equals(cond.getSource().getDescriptor())) {
		data = cond.getData();
	    }
	}
	assertNotNull(data);
	System.out.println("Data: " + data);
	assertEquals(sa.getPartOne().toString(), data);
	// validate actions
	ActionInstance act = null;
	for (final ActionInstance ai : current.getBehaviour()) {
	    if ("object.move.DOWN".equals(ai.getAssociatedAction()
		    .getDescriptor())) {
		act = ai;
	    }
	}
	assertNotNull(act);
	assertArrayEquals(new Object[0], act.getParameters().toArray());
    }

    /**
     * Tests the agent's capability of updating its history
     */
    @Test
    public void testDynamicImageHistory() {
	final SimpleExpert sa = new SimpleExpert();
	// history values must be empty
	final DynamicImage di = sa.getSoftwareImage().getDynamicImage();
	assertArrayEquals(new Snapshot[0], di.getHistoricalRecord());
	sa.run();
	// the first history element must be null
	assertNull(di.getHistoricalRecord()[0]);
	final Snapshot current = di.getCurrentSnapshot();
	sa.run();
	// history now has one element that matches the previously current
	// snapshot
	assertEquals(current, di.getHistoricalRecord()[0]);
    }

    /**
     * Tests the agent's perception storage mechanism
     */
    @Test
    public void testAgentPerception() {
	final SimpleExpert sa = new SimpleExpert();
	final AgentPerception perception = sa.getAgentPerception();
	assertNotNull(perception);
	assertNotNull(perception.getCurrentConditions());
	assertEquals(6, perception.getCurrentConditions().size());
	assertEquals(NoData.class, perception.getCurrentConditions().get(0)
		.getData().getClass());
	sa.run();
	assertEquals(String.class, perception.getCurrentConditions().get(0)
		.getData().getClass());
    }
}
