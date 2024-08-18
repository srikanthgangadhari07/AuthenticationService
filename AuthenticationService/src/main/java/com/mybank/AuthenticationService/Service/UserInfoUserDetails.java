package com.mybank.AuthenticationService.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.mybank.AuthenticationService.Model.User;

public class UserInfoUserDetails implements UserDetails {
	private  String userName;
	private String password;
	private List<GrantedAuthority> authorities;
	UserInfoUserDetails(User user){
		this.userName=user.getUserName();
		this.password =user.getPassword();
		authorities = Arrays.stream(user.getRoles().split(","))
			    .map(x-> new SimpleGrantedAuthority(x))
			    .collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		
		return userName;
	}

}
