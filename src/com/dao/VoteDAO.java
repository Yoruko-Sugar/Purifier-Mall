package com.dao;
import com.entity.*;

import java.util.*;
public interface VoteDAO {
	void add(Vote vote);
	List<Vote> selectAll(HashMap map);
}
