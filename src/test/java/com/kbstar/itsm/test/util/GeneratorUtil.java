package com.kbstar.itsm.test.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntFunction;

public class GeneratorUtil {
	
	public static<T> Vector<T> toVector(int to,IntFunction<T> forin) {
		
		Vector<T> v = new Vector<>();
		for(int i=1 ; i<= to; i++ ) v.add(forin.apply(i));
		
		return v;
	}

	
	public static<T> List<T> toList(int to,IntFunction<T> forin) {
		List<T> v = new ArrayList<>();
		for(int i=1 ; i<= to; i++ ) v.add(forin.apply(i));
	
		return v;
	}

}
