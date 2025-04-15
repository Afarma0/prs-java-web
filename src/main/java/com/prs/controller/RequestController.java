package com.prs.controller;

import java.time.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.db.RequestRepo;
import com.prs.db.UserRepo;
import com.prs.model.Request;
import com.prs.model.RequestCreate;
import com.prs.model.User;

@CrossOrigin
@RestController
@RequestMapping("/api/requests")
public class RequestController {

	@Autowired
	public RequestRepo reqRep;
	@Autowired
	public UserRepo userRepo;

	@GetMapping("/")
	public List<Request> getAll() {
		return reqRep.findAll();
	}

	@GetMapping("/{id}")
	public Optional<Request> getById(@PathVariable int id) {
		Optional<Request> r = reqRep.findById(id);
		if (r.isPresent()) {
			return r;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found by id" + id);
		}
	}

	@PostMapping("")
	public Request add(@RequestBody RequestCreate rc) {
		Request r = new Request();
		Optional<User> u = userRepo.findById(rc.getUserId());
		r.setStatus("NEW");
		r.setSubmittedDate(LocalDateTime.now());
		r.setTotal(0.0);
		r.setRequestNumber(generateNextRequestNumber());
		r.setDateNeeded(rc.getDateNeeded());
		r.setDescription(rc.getDescription());
		r.setJustification(rc.getJustification());
		r.setDeliveryMode(rc.getDeliveryMode());
		r.setUser(u.get());
		return reqRep.save(r);
	}

	@PutMapping("/{id}")
	public void putRequest(@PathVariable int id, @RequestBody Request request) {
		if (id != request.getId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request id mismatch vs URL.");
		} else if (reqRep.existsById(request.getId())) {
			reqRep.save(request);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found by id" + id);
		}
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		if (reqRep.existsById(id)) {
			reqRep.deleteById(id);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found by id" + id);
		}
	}

	// ONTO CUSTOM ACTIONS BEYONG CRUD

	public String generateNextRequestNumber() {
	    Optional<String> latestOpt = reqRep.findTopRequestNumberCustomMethod();
	    int nextSequence = 1;

	    if (latestOpt.isPresent()) {
	        String latest = latestOpt.get();
	        if (latest.length() >= 11) { // "R" + yyMMdd + 0001 (11 chars total)
	            String numberPart = latest.substring(7); // get the 0001 part
	            try {
	                nextSequence = Integer.parseInt(numberPart) + 1;
	            } catch (NumberFormatException e) {
	                // handle gracefully if malformed number
	            }
	        }
	    }

	    String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
	    return String.format("R%s%04d", datePart, nextSequence);
	}

	@PutMapping("/submit-review/{id}")
	public void submitForReview(@PathVariable int id) {

		if (reqRep.existsById(id)) {
			Request r = reqRep.findById(id).get();
			if (r.getStatus().equals("NEW") && r.getTotal() <= 50) {
				r.setStatus("APPROVED");
				reqRep.save(r);
			} else if (r.getStatus().equals("NEW") && r.getTotal() > 50) {
				r.setStatus("REVIEW");
				reqRep.save(r);
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is not in NEW status.");
			}
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found by id" + id);
		}
	}

	@GetMapping("/list-review/{userId}")
	public List<Request> listReview(@PathVariable int userId) {
		return reqRep.findByStatusAndUserIdNot("REVIEW", userId);
	}

	@PutMapping("/approve/{id}")
	public void approve(@PathVariable int id) {

		if (reqRep.existsById(id)) {
			Request r = reqRep.findById(id).get();
			if (r.getStatus().equals("REVIEW")) {
				r.setStatus("APPROVED");
				reqRep.save(r);
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is not in REVIEW status.");
			}
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found by id" + id);
		}
	}

	@PutMapping("/reject/{id}")
	public void reject(@PathVariable int id, @RequestBody Request request) {

		if (reqRep.existsById(id)) {
		
			if (request.getStatus().equals("REVIEW") && request.getReasonForRejection() != null) {
				request.setStatus("REJECTED");
				reqRep.save(request);
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is not in REVIEW status.");
			}
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found by id" + id);
		}
	}

}
//last}
