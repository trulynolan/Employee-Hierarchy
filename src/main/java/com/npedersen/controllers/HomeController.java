package com.npedersen.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.npedersen.models.LoginUser;
import com.npedersen.models.User;
import com.npedersen.services.UserService;


@Controller
public class HomeController {

	@Autowired
	private UserService userServ;

//	This route renders the forms for login and reg.
	@GetMapping("/")
	public String index(Model model) {
		String[] levels = { "Manager", "Supervisor", "Associate" };
		model.addAttribute("levels", levels);
		model.addAttribute("newUser", new User());
		model.addAttribute("newLogin", new LoginUser());
		return "index.jsp";
	}

//	This route is the action for submitting the login and registration form.
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("newUser") User newUser, BindingResult result, Model model,
			HttpSession session) {
//		This calls on the register method in service - sends in the newUser info and the results from the binding result.
		userServ.register(newUser, result);
//		If we have any errors, we stay on that page and display the errors. 
		if (result.hasErrors()) {
			model.addAttribute("newLogin", new LoginUser());
			return "index.jsp";
		}
//		If everything is good, set the userId in session.
		session.setAttribute("user_id", newUser.getId());
		return "redirect:/home";
	}

//	This route is the action for submitting the login form.
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin, BindingResult result, Model model,
			HttpSession session) {

		User user = userServ.login(newLogin, result);
//		If we have any errors we stay on the page and display errors.
		if (result.hasErrors()) {
			model.addAttribute("newUser", new User());
			return "index.jsp";
		}
		session.setAttribute("user_id", user.getId());
		return "redirect:/home";
	}

	@GetMapping("/home")
	public String dashboard(HttpSession session, Model model) {
		if (session.getAttribute("user_id") == null) {
			return "redirect:/";
		}
		Long userId = (Long) session.getAttribute("user_id");
		User user = userServ.getUserInfo(userId);

		List<User> allUsers = userServ.getAllUsers();
		List<User> allSupervisors = userServ.getByLevel("Supervisor");
		List<User> allAssociates = userServ.getByLevel("Associate");
		System.out.println("allSupervisors " + allSupervisors);
		ArrayList<User> team = new ArrayList<User>();
		if (user.getLevel().equals("Manager")) {
			for (User u : allUsers) {
				team.add(u);
			}
		}

		else if (user.getLevel().equals("Supervisor")) {
			for (User u : allSupervisors) {
				team.add(u);
			}
			for (User u : allAssociates) {
				team.add(u);
			}
		} else {
			for (User u : allAssociates) {
				team.add(u);
			}
		}

		model.addAttribute("user", user);

		model.addAttribute("team", team);

		return "home.jsp";

	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("user_id");
		return "redirect:/";
	}

}
