package com.nicky.utils;

import java.lang.reflect.Array;
import java.util.*;

public final class ArrayUtils {
	
	public static String[] toStringArray(Object[] objects) {
		int length=objects.length;
		String[] result = new String[length];
		for (int i=0; i<length; i++) {
			result[i] = objects[i].toString();
		}
		return result;
	}
	
	public static String[] fillArray(String str, int length) {
		String[] result = new String[length];
		Arrays.fill(result, str);
		return result;
	}
	
	public static String[] toStringArray(Collection coll) {
		return toStringArray( coll.toArray() );
	}
	
	public static int[] toIntArray(Collection coll) {
		Iterator iter = coll.iterator();
		int[] arr = new int[ coll.size() ];
		int i=0;
		while( iter.hasNext() ) {
			arr[i++] = (Integer) iter.next();
		}
		return arr;
	}
	
	public static Object[] typecast(Object[] array, Object[] to) {
		return Arrays.asList(array).toArray(to);
	}
	
	//Arrays.asList doesn't do primitive arrays
	public static List toList(Object array) {
		if ( array instanceof Object[] ) return Arrays.asList( (Object[]) array ); //faster?
		int size = Array.getLength(array);
		ArrayList list = new ArrayList(size);
		for (int i=0; i<size; i++) {
			list.add( Array.get(array, i) );
		}
		return list;
	}
	
	public static String[] slice(String[] strings, int begin, int length) {
		String[] result = new String[length];
		for ( int i=0; i<length; i++ ) {
			result[i] = strings[begin+i];
		}
		return result;
	}
	
	public static Object[] slice(Object[] objects, int begin, int length) {
		Object[] result = new Object[length];
		for ( int i=0; i<length; i++ ) {
			result[i] = objects[begin+i];
		}
		return result;
	}
	
	public static List toList(Iterator iter) {
		List list = new ArrayList();
		while ( iter.hasNext() ) {
			list.add( iter.next() );
		}
		return list;
	}
	
	public static String[] join(String[] x, String[] y) {
		String[] result = new String[ x.length + y.length ];
		for ( int i=0; i<x.length; i++ ) result[i] = x[i];
		for ( int i=0; i<y.length; i++ ) result[i+x.length] = y[i];
		return result;		
	}
	
	public static int[] join(int[] x, int[] y) {
		int[] result = new int[ x.length + y.length ];
		for ( int i=0; i<x.length; i++ ) result[i] = x[i];
		for ( int i=0; i<y.length; i++ ) result[i+x.length] = y[i];
		return result;		
	}

	private ArrayUtils() {}
	
	public static String asString(Object os[]) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int i = 0; i < os.length; i++) {
			sb.append(os[i]);
			if(i<os.length-1) sb.append(",");			
		}
		sb.append("]");
		return sb.toString();
	}
	
	public static boolean isAllNegative(int[] array) {
		for ( int i=0; i<array.length; i++ ) {
			if ( array[i] >=0 ) return false;
		}
		return true;
	}
	
	public static void addAll(Collection collection, Object[] array) {
		for ( int i=0; i<array.length; i++ ) {
			collection.add( array[i] );
		}
	}
	
	public static final String[] EMPTY_STRING_ARRAY = {};
	public static final Class[] EMPTY_CLASS_ARRAY = {};
	public static final Object[] EMPTY_OBJECT_ARRAY = {};
	public static int[] EMPTY_INT_ARRAY = {};
}






