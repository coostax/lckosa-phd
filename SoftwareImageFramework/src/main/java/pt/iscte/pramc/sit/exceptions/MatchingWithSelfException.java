/**
 * 
 */
package pt.iscte.pramc.sit.exceptions;

import pt.iscte.pramc.sit.swi.SoftwareImage;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Exception thrown when matching a software image with itself
 * 
 * @version 0.1
 * @since Apr 6, 2011
 */
public class MatchingWithSelfException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private final SoftwareImage image;

    public MatchingWithSelfException(SoftwareImage img) {
	this.image = img;
    }

    @Override
    public String getMessage() {
	return "Matching software image with itself : " + image.getAgentUUID();
    }

}
