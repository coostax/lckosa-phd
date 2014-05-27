/**
 * Copyright 2010 by Paulo R. Costa 
 * Distributed under the Artistic License.
 * This license appears at LICENSE file on the root of source folder. 
 */
package pt.iscte.pramc.sit.ext.cloning;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Paulo Costa (paulo.costa@iscte.pt)
 * 
 *         Helper class for creating clones
 * 
 * @since 10 de Nov de 2011
 * @version 0.1
 */
public class CloneHelper {

    /**
     * Creates a new list with a copy (clone) of the elements of the first list
     * 
     * @param from
     *            the list to copy
     * @return a copy of the supplied list
     * @throws CloneNotSupportedException
     */
    public static <E> Collection<E> copyElementsFrom(Collection<E> from)
	    throws CloneNotSupportedException {
	List<E> result = new ArrayList<E>();
	for (E obj : from) {
	    result.add(makeCopyOf(obj));
	}
	return result;
    }

    /**
     * Makes a copy of the supplied element
     * 
     * @param <E>
     * @param original
     *            the element to copy
     * @return a copy of the element
     * @throws CloneNotSupportedException
     */
    @SuppressWarnings("unchecked")
    public static <E> E makeCopyOf(E original)
	    throws CloneNotSupportedException {
	if (original == null) {
	    throw new CloneNotSupportedException("Argument is null");
	}
	// try to clone the data using the clone() method
	if (original instanceof Copiable) {
	    return ((Copiable<E>) original).copy();
	} else if (original instanceof Integer) {
	    return (E) new Integer(((Integer) original).intValue());
	} else if (original instanceof Double) {
	    return (E) new Double((Double) original);
	} else if (original instanceof Long) {
	    return (E) new Long((Long) original);
	} else if (original instanceof Float) {
	    return (E) new Float((Float) original);
	} else if (original instanceof Boolean) {
	    return (E) new Boolean((Boolean) original);
	} else if (original instanceof Character) {
	    return (E) new Character((Character) original);
	} else if (original.getClass().isPrimitive()) {
	    // when faced with primitive data we can make a direct copy
	    return original;
	} else if (original.getClass().isEnum()) {
	    // if is enumeration the original can also be returned
	    return original;
	} else if (original.getClass().isArray()) {
	    // for arrays use the array utilities
	    return (E) Arrays.copyOf((Object[]) original,
		    ((Object[]) original).length);
	} else if (original instanceof Collection) {
	    // for arrays use the array utilities
	    return (E) copyElementsFrom((Collection<?>) original);
	} else { // see if there is a copy constructor
	    Class<E> clazz = (Class<E>) original.getClass();
	    Constructor<E> constructor;
	    try {
		// see if there is any copy constructor
		constructor = clazz.getConstructor(clazz);
		if (constructor != null) {
		    E instance = constructor.newInstance(original);
		    if (instance != null) {
			return instance;
		    } else {
			throw new CloneNotSupportedException(
				"Could not create a new instance of "
					+ clazz.getName());
		    }
		} else {
		    throw new CloneNotSupportedException(clazz.getName()
			    + " is not clonable");
		}
	    } catch (Exception e) {
		throw new CloneNotSupportedException(e.getMessage());
	    }
	}
    }

    // /**
    // * Clone method for the data holded by this condition
    // * @return a clone of the data holded by this condition
    // * @throws CloneNotSupportedException
    // */
    // public Object cloneData() throws CloneNotSupportedException{
    // Object obj = null;
    // //try to clone the data using the clone() method
    // if(data instanceof Copiable){
    // obj = ((Copiable)data).copy();
    // }else if(data instanceof Integer){
    // obj = new Integer(((Integer)data).intValue());
    // }else if(data instanceof Double){
    // obj = new Double((Double)data);
    // }else if(data instanceof Long){
    // obj = new Long((Long)data);
    // }else if(data instanceof Float){
    // obj = new Float((Float)data);
    // }else if(data.getClass().isPrimitive()){
    // //when faced with primitive data we can make a direct copy
    // obj = data;
    // }else if(data.getClass().isArray()){
    // //for arrays use the array utilities
    // obj = Arrays.copyOf((Object[])data, ((Object[])data).length);
    // }else{ //see if there is a copy constructor
    // Class<?> clazz = data.getClass();
    // Constructor<?> constructor;
    // try {
    // //see if there is any copy constructor
    // constructor = clazz.getConstructor(clazz);
    // if(constructor != null){
    // Object instance = constructor.newInstance(data);
    // if(instance != null){
    // obj = instance;
    // }else{
    // //logger.error("cannot clone " + clazz.getCanonicalName());
    // throw new
    // CloneNotSupportedException("Could not create a new instance of " +
    // clazz.getName());
    // }
    // }else{
    // //logger.error("cannot clone " + clazz.getCanonicalName());
    // throw new CloneNotSupportedException(clazz.getName() +
    // " is not clonable");
    // }
    // } catch (Exception e) {
    // //logger.error("cannot clone " + clazz.getCanonicalName());
    // throw new CloneNotSupportedException(e.getMessage());
    // }
    // }
    // return obj;
    // }
}
