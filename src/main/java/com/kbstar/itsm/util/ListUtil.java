package com.kbstar.itsm.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ListUtil {
	
	public static <T> List<T> tranform(List<Map<String,Object>> list, Function<Map<String,Object>,T> function) {
		
		List<T> resultList = new ArrayList<T>();
		for(Map<String,Object> i : list) resultList.add(function.apply(i));
	
		return resultList;
	}

	
	public static <T> Vector<T> tranformVector(Vector<Map<String,Object>> list, Function<Map<String,Object>,T> function) {
		
		Vector<T> resultList = new Vector<T>();
		for(Map<String,Object> i : list) resultList.add(function.apply(i));
	
		return resultList;
	}
	
	
	public static <T>boolean findFirstUpdate(List<T> list,Predicate<T> where, Consumer<T> update) {
		Optional<T> opt = list.stream().filter(where).findFirst();
		opt.ifPresent(update);
		return opt.isPresent();
	}
	

}
