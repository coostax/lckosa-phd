/**
 * 
 */
package pt.iscte.pramc.tests.sit.remote.old;

import javax.swing.JTextArea;

import pt.iscte.pramc.sit.swi.di.DynamicImage;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 * @version 0.1
 * @since Mar 14, 2011
 */
public class DynamicImageObserver extends Thread {

	private final DynamicImage dynamicImage;
	private final JTextArea textArea;

	private boolean stop;

	public DynamicImageObserver(DynamicImage di, JTextArea area) {
		this.dynamicImage = di;
		this.textArea = area;
		stop = false;
	}

	/**
	 * Runs this thread. Makes recursive calls to the agent's dynamic image
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (!stop) {
			try {
				textArea.setText(dynamicImage.toString());
				wait(100);
			} catch (Exception e) {
				System.err.println("Exception: " + e.getMessage());
			}
		}
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

}
