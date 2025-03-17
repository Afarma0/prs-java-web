package com.prs.db;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.model.Request;

public interface RequestRepo extends JpaRepository<Request, Integer> {
	Optional<Request> findTopByRequestNumberStartingWithOrderByRequestNumberDesc(String datePrefix);
	
	default Optional<String> findTopRequestNumberCustomMethod() {
        return findAll().stream()
          .map(Request::getRequestNumber)
          .max(Comparator.naturalOrder());
	}

	List<Request> findByStatusAndUserIdNot(String string, int userId);
}
