/** Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.swi.di;

import java.io.Serializable;
import java.util.List;

import pt.iscte.pramc.sit.swi.SoftwareImage;

/**
 * The Dynamic image is a visual representation of the agent's behaviors.
 * Behavior description is encapsulated in Snapshot Objects
 * 
 * @see Snapshot
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * @version 1.0
 * @since 04/2010
 * 
 */
public class DynamicImage implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static final int CONTAINER_SIZE = 10;

    private Snapshot currentSnapshot;

    private final Snapshot[] historicalRecord;

    private int historySize;

    /**
     * The root software image of this Dynamic image
     */
    private SoftwareImage root;

    /**
     * Default constructor. Receives the root software image and starts up the
     * historical record
     * 
     * @param softwareImage
     */
    public DynamicImage(SoftwareImage root) {
	super();
	this.root = root;
	// TODO: set historical record maximum size from properties file
	this.historicalRecord = new Snapshot[CONTAINER_SIZE];
	this.historySize = 0;
    }

    /**
     * Copy Constructor Creates a new dynamic image as copy of the supplied
     * image
     * 
     * @param toCopy
     *            the dynamic image to copy
     */
    public DynamicImage(DynamicImage toCopy, SoftwareImage root) {
	this(root);
	try {
	    // update historical record
	    for (int i = 0; i != toCopy.getHistoricalRecord().length; ++i) {
		if (toCopy.getHistoricalRecord()[i] != null) {
		    this.historicalRecord[i] = new Snapshot(
			    toCopy.getHistoricalRecord()[i]);
		}
	    }
	    // update current action
	    if (toCopy.getCurrentSnapshot() != null) {
		this.currentSnapshot = new Snapshot(toCopy.getCurrentSnapshot());
	    }
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	}
    }

    /**
     * @return the last set of action instances performed by the agent
     */
    public List<ActionInstance> getCurrentActionInstances() {
	return currentSnapshot.getBehaviour();
    }

    /**
     * @return the current condition action set
     */
    public Snapshot getCurrentSnapshot() {
	return currentSnapshot;
    }

    /**
     * Updates the software image with new action
     * 
     * @param currentSnapshot
     */
    public void update(Snapshot currentAction) {
	// set the previous condition/action set has historical information
	updateHistoricalRecord();
	this.currentSnapshot = currentAction;
    }

    /**
     * updates the historical record with the current condition/action set
     */
    private void updateHistoricalRecord() {
	System.arraycopy(historicalRecord, 0, historicalRecord, 1,
		historicalRecord.length - 1);
	this.historicalRecord[0] = currentSnapshot;
	if (historySize < CONTAINER_SIZE) {
	    this.historySize++;
	}
    }

    /**
     * @return the root software image
     */
    public SoftwareImage getRoot() {
	return root;
    }

    public void setRoot(SoftwareImage root) {
	this.root = root;
    }

    /**
     * @return the historical record from this agent's software image
     */
    public Snapshot[] getHistoricalRecord() {
	if (historySize >= CONTAINER_SIZE) {
	    return historicalRecord;
	} else {
	    final Snapshot[] aux = new Snapshot[historySize];
	    System.arraycopy(historicalRecord, 0, aux, 0, historySize);
	    return aux;
	}
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	StringBuffer sb = new StringBuffer("[dynamic] \n");
	sb.append("[currentSnapshot] ");
	if (currentSnapshot != null) {
	    sb.append(currentSnapshot.toString());
	} else {
	    sb.append("NO ACTION");
	}
	sb.append(" [/currentSnapshot] \n");
	sb.append("[history] \n");
	if (historicalRecord != null && historicalRecord.length > 0) {
	    int counter = 0;
	    for (Snapshot histRec : historicalRecord) {
		sb.append(counter);
		sb.append(" : ");
		if (histRec != null) {
		    sb.append(histRec.toString());
		} else {
		    sb.append("NO ACTION");
		}
		sb.append("\n");
		counter++;
	    }
	} else {
	    sb.append(" NO HISTORY ");
	}
	sb.append("[/history] \n");
	sb.append("[/dynamic] \n");
	return sb.toString();
    }
}