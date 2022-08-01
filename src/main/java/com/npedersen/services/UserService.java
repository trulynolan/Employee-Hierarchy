package com.npedersen.services;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.npedersen.models.LoginUser;
import com.npedersen.models.User;
import com.npedersen.repositories.UserRepository;



@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

//	This is our register method.
	public User register(User newUser, BindingResult result) {
//		If check to see if the email from our new user is already present in the database.
		if (userRepo.findByEmail(newUser.getEmail()).isPresent()) {
//			If the email is already in the database, displays this message.
			result.rejectValue("email", "Unique", "This email is already in use!");
		}

//		This checks to make sure the password and confirm password match each other.
		if (!newUser.getPassword().equals(newUser.getConfirm())) {
			result.rejectValue("confirm", "Matches", "The Confirm Password must match Password!");
		}

//		If we have errors in the BindingResult return null -> you get nothing.
		if (result.hasErrors()) {
			return null;
		} else {
//			If everything is good, we save the password encrypt via Bcrypt.
			String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
//			Save the hashed password in the database.
			newUser.setPassword(hashed);
//			Save the entire User in the database.
			return userRepo.save(newUser);
		}
	}

	public User oneUser(Long id) {
		Optional<User> optionalUser = userRepo.findById(id);
		if (optionalUser.isPresent()) {
			return optionalUser.get();
		} else {
			return null;
		}
	}

//	This is for logging in.
	public User login(LoginUser newLogin, BindingResult result) {
//		If there are any errors -> You get nothing
		if (result.hasErrors()) {
			return null;
		}
//		This will get the email that the logging in User has submitted.
		Optional<User> potentialUser = userRepo.findByEmail(newLogin.getEmail());
//		If that email does not exist in the database -> they get nothing.
		if (!potentialUser.isPresent()) {
			result.rejectValue("email", "Unique", "Unknown email!");
			return null;
		}

//		If the email exists in the database - we get that user based on the emai.
		User user = potentialUser.get();
//		If the password does not match the password in the database -> custom message sent to the front end.
		if (!BCrypt.checkpw(newLogin.getPassword(), user.getPassword())) {
			result.rejectValue("password", "Matches", "Invalid Password!");
		}

//		If ther are any errors at this point in Binding Result - > they get nothing.
		if (result.hasErrors()) {
			return null;
		} else {
//			But is all this is good, then we return to User.
			return user;
		}
	}
//	====================
//	Find One User based on Id
//	====================

	public User getUserInfo(Long id) {
		Optional<User> optionalUser = userRepo.findById(id);
		if (optionalUser.isPresent()) {
			return optionalUser.get();
		} else {
			return null;
		}
	}

	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	public List<User> getByLevel(String level) {
		return userRepo.findByLevel(level);
	}
}
