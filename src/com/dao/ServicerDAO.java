package com.dao;
import com.entity.*;

import java.util.*;
public interface ServicerDAO {
	void add(Servicer servicer);
	List<Servicer> selectAll(HashMap map);
	Servicer findById(int id);
	void delete(int id);
	void update(Servicer servicer);
}
