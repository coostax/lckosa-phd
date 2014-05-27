/**
 * 
 */
package pt.iscte.pramc.tests.sit.remote.old;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import pt.iscte.pramc.sit.ext.remote.RMIVisualSoftwareAgent;
import pt.iscte.pramc.sit.swi.SoftwareImage;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         A minimal observer agent that observes another software agent's
 *         visual image
 * 
 *         Accesses the agent's software image through RMI
 * 
 *         Shows observation results on a window
 * 
 * @version 0.1
 * @since Mar 8, 2011
 */
public class RemoteAgent implements ActionListener {

	private final JFrame window;
	private final Container contentPane;
	private final JTextField agentNameField;
	private final JButton searchButton;
	private final JTextArea staticImageTa;
	private final JTextArea dynamicImageTa;
	private DynamicImageObserver observer;

	private SoftwareImage swi;

	private final Registry registry;

	/**
	 * Default constructor Initializes this agent
	 * 
	 * @throws RemoteException
	 */
	public RemoteAgent(String host) throws RemoteException {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		registry = LocateRegistry.getRegistry(host);
		// initialize graphical components
		this.window = new JFrame("Minimal observer agent");
		this.contentPane = window.getContentPane();
		this.agentNameField = new JTextField(30);
		this.searchButton = new JButton("Search");
		this.staticImageTa = new JTextArea("Static...", 10, 30);
		this.dynamicImageTa = new JTextArea("Dynamic...", 10, 30);

		// setup window and container
		this.staticImageTa.setEditable(false);
		this.staticImageTa.setAutoscrolls(true);
		this.dynamicImageTa.setEditable(false);
		this.dynamicImageTa.setAutoscrolls(true);
		searchButton.addActionListener(this);
		window.setSize(100, 100);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane.setBackground(Color.WHITE);
		contentPane.setLayout(new FlowLayout());

		// add contents to JFrame
		contentPane.add(agentNameField);
		contentPane.add(searchButton);
		// contentPane.add(new JLabel("Static image"));
		contentPane.add(staticImageTa);
		// contentPane.add(new JLabel("Dynamic image"));
		contentPane.add(dynamicImageTa);

		// finalize JFrame
		window.pack();
		window.setVisible(true);
	}

	/**
	 * Searches for an agent with the provided name
	 * 
	 * @param arg0
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// clean text fields
		staticImageTa.setText("");
		dynamicImageTa.setText("");
		// get agent UUID from text field
		String uuid = agentNameField.getText();
		if (uuid != null && !"".equals(uuid)) {
			// get the remote software agent interface
			try {
				RMIVisualSoftwareAgent otherAgent = (RMIVisualSoftwareAgent) registry
						.lookup(uuid);
				if (otherAgent != null) {
					// store link to software image
					swi = otherAgent.getSoftwareImage();
					// show static image on textbox
					staticImageTa.setText(swi.getStaticImage().toString());
					if (observer != null) {
						observer.setStop(true);
						observer = null;
					}
					observe();
				}
			} catch (AccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.err.println("ERROR, name is empty");
		}
	}

	/**
	 * Observe an agent's actions
	 */
	public void observe() {
		if (swi != null) {
			dynamicImageTa.setText(swi.getDynamicImage().toString());
		}
	}

	/**
	 * initializes this agent
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			RemoteAgent ra = new RemoteAgent(args[0]);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
