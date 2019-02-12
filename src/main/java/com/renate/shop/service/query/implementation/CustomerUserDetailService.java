package com.renate.shop.service.query.implementation;

import java.util.ArrayList;

import com.renate.shop.model.User;
import com.renate.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.userRepository.findByUsername(username);
		if (user != null && user.getEnabled()) {
			return new org.springframework.security.core.userdetails.User(
					user.getUsername(), user.getPassword(), user.getEnabled(), true,
					true, true, new ArrayList<GrantedAuthority>()
			);
		}
		throw new UsernameNotFoundException("Username does not exist");
	}
}
