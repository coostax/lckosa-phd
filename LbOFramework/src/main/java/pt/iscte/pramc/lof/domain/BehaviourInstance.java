/**
 * Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.domain;

import java.util.List;

import pt.iscte.pramc.sit.swi.di.ActionInstance;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 *
 * Represents an instance that holds a behaviour
 *
 * @since 30 de Jan de 2012
 * @version 0.1 
 */
public class BehaviourInstance extends LbOInstance<List<ActionInstance>>{

	public BehaviourInstance(LbOAttribute<List<ActionInstance>> att,
			List<ActionInstance> data) {
		super(att, data);
	}
	
}
