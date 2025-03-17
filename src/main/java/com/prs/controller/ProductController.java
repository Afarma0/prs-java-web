package com.prs.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.db.ProductRepo;
import com.prs.model.Product;

@CrossOrigin
@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	public ProductRepo prodRep;

	@GetMapping("/")
	public List<Product> getAll() {
		return prodRep.findAll();
	}

	@GetMapping("/{id}")
	public Optional<Product> getById(@PathVariable int id) {
		Optional<Product> p = prodRep.findById(id);
		if (p.isPresent()) {
			return p;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found by id" + id);
		}
	}

	@PostMapping("")
	public Product post(@RequestBody Product product) {
		return prodRep.save(product);
	}

	@PutMapping("/{id}")
	public void update(@PathVariable int id, @RequestBody Product product) {
		if (id != product.getId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"u dumb, check the http and make sure the id matches the is in the request body (json)");
		} else if (prodRep.existsById(id)) {
			prodRep.save(product);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found by id");
		}
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		if (prodRep.existsById(id)) {
			prodRep.deleteById(id);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found by id");
		}
	}
}
//last}
