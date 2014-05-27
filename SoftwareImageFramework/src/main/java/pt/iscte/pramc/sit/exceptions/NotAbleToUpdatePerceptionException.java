/**
 * 
 */
package pt.iscte.pramc.sit.exceptions;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * 
 *         Exception thrown whenever is not possible to update an agent's
 *         perception
 * 
 * @version 0.1
 * @since 02/2011
 */
public class NotAbleToUpdatePerceptionException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private final String fromAgent;
    private final String data;

    public NotAbleToUpdatePerceptionException(Object executor, Object data) {
	this.fromAgent = executor.getClass().getCanonicalName();
	this.data = data.getClass().getName() + ": " + data.toString();
    }

    @Override
    public String getMessage() {
	return "Unable to set perception on " + fromAgent
		+ "\n Sensor data to set was : " + data;
    }

}
