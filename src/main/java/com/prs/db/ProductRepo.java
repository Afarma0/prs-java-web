package com.prs.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.model.Product;

public interface ProductRepo extends JpaRepository<Product, Integer>{} 

	

