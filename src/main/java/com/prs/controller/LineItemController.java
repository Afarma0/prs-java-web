package com.prs.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.db.LineItemRepo;
import com.prs.db.RequestRepo;
import com.prs.model.LineItem;
import com.prs.model.Request;

import jakarta.transaction.Transactional;

@CrossOrigin
@RestController
@RequestMapping("/api/lineitems")
public class LineItemController {

	@Autowired
	private LineItemRepo lineItemRepo;
	@Autowired
	private RequestRepo reqRepo;

	@GetMapping("/")
	public List<LineItem> getAll() {
		return lineItemRepo.findAll();
	}

	@GetMapping("/{id}")
	public Optional<LineItem> getById(@PathVariable int id) {
		Optional<LineItem> li = lineItemRepo.findById(id);
		if (li.isPresent()) {
			return li;
			
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "LineItem not found by id" + id);
		}
	}

	@PostMapping("")
	@Transactional
	public LineItem add(@RequestBody LineItem li) {
		LineItem savedLi = lineItemRepo.save(li);
		recalculateTotal(li.getRequest().getId());
		return savedLi;
	}

	@PutMapping("/{id}")
	@Transactional
	public void update(@PathVariable int id, @RequestBody LineItem li) {
		if (id != li.getId()) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lineitem id mismatch vs URL.");
	    }
		else if (!lineItemRepo.existsById(id)) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "LineItem not found by id " + id);
	    }
		else {
	    lineItemRepo.save(li);
	    recalculateTotal(li.getRequest().getId());
		}
	}

	@DeleteMapping("/{id}")
	@Transactional
	public void delete(@PathVariable int id) {
	    if (!lineItemRepo.existsById(id)) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "LineItem not found by id " + id);
	    }

	    LineItem li = lineItemRepo.findById(id).orElse(null);
	    lineItemRepo.deleteById(id);
	    if (li != null) {
	        recalculateTotal(li.getRequest().getId());
	    }
	}
	
	//CUSTOM ACTIONS BEYOND CRUD

	@GetMapping("/lines-for-req/{reqId}")
	public List<LineItem> getLinesForRequest(@PathVariable int reqId) {
		Optional<Request> r = reqRepo.findById(reqId);
		if (r.isPresent()) {
			return lineItemRepo.findByRequestId(reqId);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found by id" + reqId);
		}
	}
	
	
	
	@Transactional
	public void recalculateTotal(int reqId) {
	    Request request = reqRepo.findById(reqId).orElse(null);
	    if (request == null) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found by id " + reqId);
	    }

	    List<LineItem> lineItems = lineItemRepo.findByRequestId(reqId);
	    double sum = 0;

	    for (LineItem lineItem : lineItems) {
	        if (lineItem.getProduct() == null) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product is null for LineItem ID " + lineItem.getId());
	        }
	        sum += lineItem.getProduct().getPrice() * lineItem.getQuantity();
	    }

	    request.setTotal(sum);
	    reqRepo.save(request);
	}
}
