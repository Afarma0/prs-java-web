package com.prs.db;
import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.model.User;

	public interface UserRepo extends JpaRepository<User, Integer>{
	//talks to movie table
		User findByUsernameAndPassword(String username, String password);
	}


