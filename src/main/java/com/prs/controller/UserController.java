package com.prs.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.prs.db.UserRepo;
import com.prs.model.User;
import com.prs.model.UserLogin;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserRepo userRepo;

	@GetMapping("/")
	public List<User> getAll() {
		return userRepo.findAll();
	}

	@GetMapping("/{id}")
	public Optional<User> getById(@PathVariable int id) {
		Optional<User> u = userRepo.findById(id);
				if (u.isPresent()) {
					return u;
				}
				else {
					throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found by id" + id);
				}
	}

	@PostMapping("")
	public User post(@RequestBody User user) {
		return userRepo.save(user);
	}

	@PutMapping ("/{id}")
	public void update(@PathVariable int id, @RequestBody User user) {
		if (id != user.getId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id mismatch vs URL.");
		}
		else if (userRepo.existsById(user.getId())) {
			userRepo.save(user);
		}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found by id" + id);
		}
	}

	@DeleteMapping
	public void delete(@PathVariable int id) {
		if (userRepo.existsById(id)) {
			userRepo.deleteById(id);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found by id" + id);
		}
	}
	
	@PostMapping("/login")
	public User login(@RequestBody UserLogin ulog)
	{
		User u = userRepo.findByUsernameAndPassword(ulog.getUserName(), ulog.getPassword());
		if (u == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found by username/password");
		}
		else {
			return u;
		}
	//if u is null --> not found
		//return u 
	}
}
//final}
