package com.prs.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.db.VendorRepo;
import com.prs.model.Vendor;

@CrossOrigin
@RestController
@RequestMapping("api/vendors")
public class VendorController {

	@Autowired
	public VendorRepo vendorRepo;

	@GetMapping("/")
	public List<Vendor> getAll() {
		return vendorRepo.findAll();
	}

	@GetMapping("/{id}")
	public Optional<Vendor> getById(@PathVariable int id) {
		Optional<Vendor> v = vendorRepo.findById(id);
		if (v.isPresent()) {
			return v;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found by id" + id);
		}
	}
	
	@PostMapping ("")
	public Vendor post(@RequestBody Vendor vendor) {
		return vendorRepo.save(vendor);
	}
	
	@PutMapping ("/{id}")
	public void update(@PathVariable int id, @RequestBody Vendor vendor)
	{
		if (id != vendor.getId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vendor not found by id" + id);
		}
		else if (vendorRepo.existsById(id)) {
			vendorRepo.save(vendor);
		}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found by id" + id);
		}
	}
	
	@DeleteMapping ("/{id}")
	public void delete(@PathVariable int id) {
		if (vendorRepo.existsById(id)) {
			vendorRepo.deleteById(id);
		}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found by id" + id);
		}
	}
	
	
	
	
}
//final }
