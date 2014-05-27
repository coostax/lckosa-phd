/**
 * 
 */
package pt.iscte.pramc.tests.sit.remote.observer.listeners;

import java.rmi.RemoteException;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import pt.iscte.pramc.sit.ext.remote.RMIVisualSoftwareAgent;
import pt.iscte.pramc.sit.swi.SoftwareImage;
import pt.iscte.pramc.sit.swi.si.Action;
import pt.iscte.pramc.sit.swi.si.Actuator;
import pt.iscte.pramc.sit.swi.si.AgentPart;
import pt.iscte.pramc.sit.swi.si.Sensor;
import pt.iscte.pramc.tests.sit.remote.observer.holders.RemoteVisualAgentListHolder;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Event listener for the agent list
 * 
 * @version 0.1
 * @since Mar 15, 2011
 */
public class AgentListSelectionListener implements ListSelectionListener {

	private final DefaultMutableTreeNode rootNode;

	public AgentListSelectionListener(DefaultMutableTreeNode root) {
		this.rootNode = root;
	}

	/**
	 * writes the selected agent's software image on the appropriate fields
	 * 
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent event) {
		// verify that source is a JList
		if (event.getSource() instanceof JList) {
			JList list = (JList) event.getSource();
			RMIVisualSoftwareAgent agent = ((RemoteVisualAgentListHolder) list
					.getSelectedValue()).getAgent();
			try {
				// write on text area
				// area.setText(agent.show().getStaticImage().toString());
				// write on JTree
				writeJTree(agent.getSoftwareImage());
			} catch (RemoteException e) {
				System.err.println("ERROR: " + e.getMessage());
			}
		}
	}

	/**
	 * Writes this software image's static image on a JTree
	 * 
	 * @param show
	 */
	private void writeJTree(SoftwareImage swi) {
		// clean tree
		rootNode.removeAllChildren();
		// start creating the tree from the root node
		rootNode.setUserObject(swi.getAgentUUID());
		// static image node
		DefaultMutableTreeNode staticImage = new DefaultMutableTreeNode(
				"Static Image");
		// iterate parts
		int partCounter = 1;
		for (AgentPart ap : swi.getStaticImage().getAgentParts()) {
			DefaultMutableTreeNode partNode = new DefaultMutableTreeNode(
					"AgentPart(" + partCounter + "): " + ap.getComment());
			// iterate sensors
			int sensorCounter = 1;
			for (Sensor sensor : ap.getSensors()) {
				DefaultMutableTreeNode sensorNode = new DefaultMutableTreeNode(
						"Sensor(" + sensorCounter + "): "
								+ sensor.getDescriptor());
				sensorCounter++;
				partNode.add(sensorNode);
			}
			// iterate actuators
			int actuatorCounter = 1;
			for (Actuator actuator : ap.getActuators()) {
				DefaultMutableTreeNode actuatorNode = new DefaultMutableTreeNode(
						"Actuator(" + actuatorCounter + "): "
								+ actuator.getComment());
				// iterate actions
				for (Action action : actuator.getActionSet()) {
					DefaultMutableTreeNode actionNode = new DefaultMutableTreeNode(
							"Action: " + action.getDescriptor());
					actuatorNode.add(actionNode);
				}
				actuatorCounter++;
				partNode.add(actuatorNode);
			}
			partCounter++;
			staticImage.add(partNode);
		}
		rootNode.add(staticImage);
		// tree.add(root);
	}

}
