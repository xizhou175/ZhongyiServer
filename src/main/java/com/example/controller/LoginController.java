package com.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.model.User;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
	
	@Autowired
	private UserService userService;

	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

	@PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("username") String username, @RequestParam("password") String password) {
		username = username.trim();
		password = password.trim();
	    System.out.println("user login: " + username + " "  + password);
        User userExists = userService.findUserByName(username);
        if (userExists != null) {
            if (bCryptPasswordEncoder.matches(password, userExists.getPassword())) {
				Map<String, String> user = new HashMap<>();
				user.put("id", userExists.getId());
				user.put("name", userExists.getName());
				user.put("gender", userExists.getGender());
				ObjectMapper mapper = new ObjectMapper();
				try {
					String userStr = mapper.writeValueAsString(user);
					return new ResponseEntity<>(userStr, HttpStatus.OK);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
        }
	    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/userName/{name}")
	public ResponseEntity<String> getUserByName(@PathVariable String name) {
		name = name.trim();
		System.out.println("getUser");
		User userExists = userService.findUserByName(name);
		if (userExists != null) {
			System.out.println("userExists");
			return new ResponseEntity<>(userExists.getId(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/userId/{id}")
	public ResponseEntity<String> getUserById(@PathVariable String id) {
		id = id.trim();
		System.out.println("getUser");
		User userExists = userService.findUserById(id);
		if (userExists != null) {
			System.out.println("userExists");
			return new ResponseEntity<>(userExists.getName(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ResponseEntity createNewUser(@RequestBody String value) {
	    System.out.println("user registration");
		User user = new User();
		try{
		    System.out.println(value);
		    ObjectMapper mapper = new ObjectMapper();
		    JsonNode jsonUserNode = mapper.readTree(value);
		    user.setName(jsonUserNode.get("username").asText());
		    user.setPassword(jsonUserNode.get("password").asText());
		    user.setAge(jsonUserNode.get("age").asInt());
		    user.setGender(jsonUserNode.get("gender").asText());
		}catch(Exception e){
		    System.out.println(e.getMessage());
		    e.printStackTrace();
		    return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		User userExists = userService.findUserByName(user.getName());
		if (userExists == null) {
			userService.saveUser(user);
			return new ResponseEntity(HttpStatus.OK);
		}
		
		return new ResponseEntity(HttpStatus.CONFLICT);
	}
}
