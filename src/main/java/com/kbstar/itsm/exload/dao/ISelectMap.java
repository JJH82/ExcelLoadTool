package com.kbstar.itsm.exload.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ISelectMap<T> {

	List<Map<String, String>> selectMap(T param) throws SQLException;

}