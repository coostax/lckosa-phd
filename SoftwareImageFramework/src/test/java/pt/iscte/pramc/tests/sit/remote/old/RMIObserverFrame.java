package pt.iscte.pramc.tests.sit.remote.old;

import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

public class RMIObserverFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel jLabel = null;
	private JTable jTable = null;

	/**
	 * This is the default constructor
	 */
	public RMIObserverFrame() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(651, 268);
		this.setContentPane(getJContentPane());
		this.setTitle("Visual Software Agent observer");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(10, 11, 177, 15));
			jLabel.setText("Visual Software Agents");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(jLabel, null);
			jContentPane.add(getJTable(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private JTable getJTable() {
		if (jTable == null) {
			jTable = new JTable();
			jTable.setBounds(new Rectangle(14, 35, 375, 80));
		}
		return jTable;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
