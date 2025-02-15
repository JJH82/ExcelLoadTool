package com.kbstar.itsm.exload.dao;

import java.util.Vector;

public interface GenericVectorDAO<T> {
	
	
	public boolean saveAll(Vector<T> info) ;
	
	public Vector<T> loadAll() ;
	
}
