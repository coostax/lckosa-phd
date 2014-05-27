/**
 * Copyright 2011 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.tools;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

/**
 * @author Paulo Costa (paulo.costa@iste.pt)
 * 
 *         Contains time extraction methods that measure time more accurately
 * 
 * @version 0.1
 * @since Jan 23, 2013
 */
public class TimeMeasurement {

    /**
     * Get CPU time in nanoseconds. "CPU time" is user time plus system time.
     * It's the total time spent using a CPU for your application
     */
    public static long getCpuTime() {
	ThreadMXBean bean = ManagementFactory.getThreadMXBean();
	return bean.isCurrentThreadCpuTimeSupported() ? bean
		.getCurrentThreadCpuTime() : 0L;
    }

    /**
     * Get user time in nanoseconds. The user time is the time spent running
     * your application's own code
     */
    public static long getUserTime() {
	ThreadMXBean bean = ManagementFactory.getThreadMXBean();
	return bean.isCurrentThreadCpuTimeSupported() ? bean
		.getCurrentThreadUserTime() : 0L;
    }

    /**
     * Get system time in nanoseconds. "System time" is the time spent running
     * OS code on behalf of your application (such as for I/O).
     */
    public static long getSystemTime() {
	ThreadMXBean bean = ManagementFactory.getThreadMXBean();
	return bean.isCurrentThreadCpuTimeSupported() ? (bean
		.getCurrentThreadCpuTime() - bean.getCurrentThreadUserTime())
		: 0L;
    }

}
