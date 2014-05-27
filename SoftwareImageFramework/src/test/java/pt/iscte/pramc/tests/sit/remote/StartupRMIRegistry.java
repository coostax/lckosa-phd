/**
 * 
 */
package pt.iscte.pramc.tests.sit.remote;

import java.rmi.RemoteException;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 * @version 0.1
 * @since Mar 14, 2011
 */
public class StartupRMIRegistry {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			java.rmi.registry.LocateRegistry.createRegistry(1099);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
