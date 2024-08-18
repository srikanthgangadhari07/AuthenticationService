package com.mybank.AuthenticationService.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mybank.AuthenticationService.Model.AuthRequest;
import com.mybank.AuthenticationService.Model.User;
import com.mybank.AuthenticationService.Repository.UserRepository;
import com.mybank.AuthenticationService.Service.JwtUtil;
import com.mybank.AuthenticationService.Service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Autowired
	UserService userService;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtUtil jwtUtil;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;



	@GetMapping("/ok")
	@PreAuthorize("hasRole('ROLE_USER')")
	public String ok() {
		return "ok";
	}

	@GetMapping("/welcome")
	public String welcome() {
		return "welcome";
	}

	@PostMapping("/name")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String name() {
		return "name";
	}

	@PostMapping("/addUser")
	public User addUser(@RequestBody User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userService.addUser(user);
	}
	@PostMapping("/authenticate")
	public String authenticateAndGetToken(@RequestBody AuthRequest request) {
		Authentication authentication =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(),request.getPassword()));
		if(authentication.isAuthenticated())
		{
			Optional<User> user =userRepository.findByuserName(request.getUserName());
			 String roles = user.get().getRoles();
			 List<String> rolesList=Arrays.asList(roles.split(","));
			return jwtUtil.generateToken(request.getUserName(),rolesList);
		}
		else {
			throw new UsernameNotFoundException("username or password is invalid");

		}

	}
}
