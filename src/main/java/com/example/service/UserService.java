package com.example.service;

import com.example.model.User;

public interface UserService {
	public User findUserByName(String name);
	public User findUserById(String id);
	public void saveUser(User user);
}
