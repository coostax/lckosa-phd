/**
 * Copyright 2012 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.lof.domain.eval;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import pt.iscte.pramc.sit.ext.NoData;
import pt.iscte.pramc.sit.ext.cloning.CloneHelper;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 *
 * Holds information on an agent internal attribute.
 * 
 * Provides a preset value and a way to get access to new values
 *
 * @since Mar 29, 2012
 * @version 0.1
 */
public class InternalAttInfo {

	private final Logger logger = Logger.getLogger(InternalAttInfo.class);
	
	private Object data;
	
	private final Object executor;
	
	private final Method getter;

	/**
	 * @param data
	 * @param executor
	 * @param getter
	 */
	public InternalAttInfo(Object data, Object executor, Method getter) {
		super();
		try {
			this.data = CloneHelper.makeCopyOf(data);
		} catch (CloneNotSupportedException e) {
			logger.error("Could not clone data " + data + ". Storing NO_VALUE");
			this.data = new NoData();
		}
		this.executor = executor;
		this.getter = getter;
	}
	
	/**
	 * @return the stored data for this internal attribute
	 */
	public Object getStoredData() {
		return data;
	}
	
	/**
	 * Calls the method to get the current value for the pointed internal attribute
	 * @return the current value for the internal attribute, NoData if it was not possible to call method
	 */
	public Object getCurrentData(){
		try {
			return getter.invoke(executor);
		} catch (Exception e) {
			logger.error("Could not call " + getter.getName() + " on " + executor + ". " + e.getMessage());
			return new NoData();
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof InternalAttInfo){
			return this.data.equals(((InternalAttInfo) obj).data) && this.executor.equals(((InternalAttInfo) obj).executor) 
					&& this.getter.equals(((InternalAttInfo) obj).getter);
		}
		return super.equals(obj);
	}
	
}
