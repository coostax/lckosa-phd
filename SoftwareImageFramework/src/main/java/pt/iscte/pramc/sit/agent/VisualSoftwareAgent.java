/**
 * Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.agent;

import pt.iscte.pramc.sit.engines.builder.AgentActions;
import pt.iscte.pramc.sit.engines.match.TranslationMatrix;
import pt.iscte.pramc.sit.engines.perception.AgentPerception;
import pt.iscte.pramc.sit.engines.update.Observer;
import pt.iscte.pramc.sit.exceptions.MatchingWithSelfException;
import pt.iscte.pramc.sit.exceptions.ObserverException;
import pt.iscte.pramc.sit.swi.SoftwareImage;
import pt.iscte.pramc.sit.swi.di.Snapshot;

/**
 * Interface representation of an agent with a software image
 * 
 * Visual software agents represent an visible agent, meaning they have a
 * software image
 * 
 * Since version 2.0 the visual software agent interface has the following
 * methods:
 * 
 * - getSoftwareImage() : provides access to this agent's software image -
 * 
 * - getAgentPerception() : provides access to this agent's perception -
 * 
 * - match() : builds a matching matrix between this agent and another one,
 * allowing the interpretation of this agent's perception and actions by another
 * agent
 * 
 * - getSimilarity() : calculates this and another one share the same structure
 * and constituents (from 0.0 to 1.0)
 * 
 * - canObserve() : shows if an apprentice can observe this agent
 * 
 * - observeCurrent() : get the current snapshot from this agent
 * 
 * - observeHistory() : get the historical record from this agent
 * 
 * The VisualSoftwareAgent interface is used to distinguish the agent instance
 * from other instances.
 * 
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * @version 2.0
 * @since 04/2010
 */
public interface VisualSoftwareAgent {

    /**
     * @return this agent's software image
     */
    public SoftwareImage getSoftwareImage();

    /**
     * @return this agent's perception
     */
    public AgentPerception getAgentPerception();

    /**
     * @return the association between the agent's actions and its
     *         representation in the software image
     */
    public AgentActions getAgentActions();

    /**
     * @return the actions currently performed by the agent
     */
    // public List<ActionInstance> getPerformedActions();

    /**
     * @return this agent's unique identifier stamped in the the software image
     */
    public String getAgentUUID();

    /**
     * Matches two software images and builds a matching matrix. Takes into
     * consideration the fact that for learning to be effective the expert's
     * image must be contained in the apprentice's image The apprentice's image
     * can have more elements than the expert's as long as all expert's elements
     * match with one of the apprentice's
     * 
     * @param apprenticeImage
     * @param allowGeneralization
     * @return
     */
    public TranslationMatrix match(VisualSoftwareAgent apprentice,
	    boolean allowGeneralization) throws MatchingWithSelfException;

    /**
     * Matches this agent with another one
     * 
     * @param another
     *            the agent to match with
     * @return 1.0 if expert matches, less if not
     */
    public double getSimilarity(VisualSoftwareAgent another);

    /**
     * decides if this agent can observe the designated agent
     * 
     * @param expert
     *            the agent to observe
     * @return true if this agent can observe the expert, false if not
     */
    public boolean canObserve(VisualSoftwareAgent apprentice);

    /**
     * @return the current snapshot from the agent's dynamic image
     */
    public Snapshot observeCurrent();

    /**
     * @return the historical record of snapshots from this agent's dynamic
     *         image
     */
    public Snapshot[] observeHistory();

    // Observer pattern

    /**
     * Attaches an observer to this expert agent Observers are informed each
     * time the expert does an action
     * 
     * @param observer
     *            the observer to attach
     * @throws ObserverException
     *             whenever it is not possible to attach an observer
     */
    public void attach(Observer observer) throws ObserverException;

    /**
     * Detaches an observer to this expert agent
     * 
     * @param observer
     *            the observer to the detach
     * @throws ObserverException
     *             whenever it is not possible to detach an observer
     */
    public void detach(Observer observer) throws ObserverException;

    /**
     * Calls the notifyDIUpdate on the registered observers.
     */
    public void recallObservers();

    /**
     * Makes the proper arrangements to shut this agent down
     */
    public void shutdown();
}
