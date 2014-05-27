/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.engine;

import org.apache.log4j.Logger;

import pt.iscte.pramc.lof.agent.Apprentice;
import pt.iscte.pramc.sit.agent.VisualSoftwareAgent;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 *
 * Generic class for all apprentice engines
 *
 * @version 0.1
 * @since Nov 2, 2011
 */
public abstract class LTEngine extends Thread {

	protected final Logger logger = Logger.getLogger(this.getClass());
	
	private boolean go;
	
	/**
	 * Association to the apprentice agent
	 */
	protected final Apprentice apprentice;
	
	public LTEngine(final Apprentice apprentice) {
		super();
		this.apprentice = apprentice;
		this.go = true;
	}
	
	public synchronized void stopEngine() {
		this.go = false;
	}
	
	@Override
	public void run() {
		//delay the method's start
		while(((VisualSoftwareAgent)apprentice).getSoftwareImage() == null){
			try {
				sleep(100);
			} catch (InterruptedException e) {
			}
		}
		mechanismCycle();
	}
	
	/**
	 * This method is called on each cycle
	 */
	protected  abstract void mechanismCycle();
	
	/**
	 * @return the agent associated to this method
	 */
	public Apprentice getAgent() {
		return apprentice;
	}
}
