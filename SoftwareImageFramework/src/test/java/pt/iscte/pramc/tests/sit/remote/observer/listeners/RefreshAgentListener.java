/**
 * 
 */
package pt.iscte.pramc.tests.sit.remote.observer.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.Vector;

import javax.swing.JList;

import pt.iscte.pramc.sit.ext.remote.RMIVisualSoftwareAgent;
import pt.iscte.pramc.tests.sit.remote.observer.holders.RemoteVisualAgentListHolder;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         The listener class for refreshing the agent list
 * 
 *         Refreshes the JTable with the list of software agents registered on
 *         the RMI registry
 * 
 * @version 0.1
 * @since Mar 15, 2011
 */
public class RefreshAgentListener implements ActionListener {

	private final Registry registry;

	private final JList list;

	/**
	 * Default constructor. receives the RMI host and the JTable where the agent
	 * list is written
	 * 
	 * @throws RemoteException
	 */
	public RefreshAgentListener(Registry registry, JList list) {
		this.list = list;
		this.registry = registry;
	}

	/**
	 * Refreshes the JTable with the list of software agents registered on the
	 * RMI registry
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// remove previous elements from list
		list.removeAll();
		Vector<RemoteVisualAgentListHolder> agents = new Vector<RemoteVisualAgentListHolder>();
		// add new elements to list
		try {
			for (String name : registry.list()) {
				Remote rInst = registry.lookup(name);
				if (rInst instanceof RMIVisualSoftwareAgent) {
					System.out.println("agent " + name);
					// add instances to the list
					agents.addElement(new RemoteVisualAgentListHolder(
							(RMIVisualSoftwareAgent) rInst, name));
				}
			}
		} catch (AccessException e1) {
			e1.printStackTrace();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (NotBoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		list.setListData(agents);
	}

}
