package com.springboot.electronicstore.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * Defines a bean for password encoding using the BCrypt hashing function. The
	 * password encoder is used by Spring Security to securely hash and verify
	 * passwords.
	 *
	 * @return an instance of BCryptPasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Defines a DAO authentication provider bean.
	 * 
	 * @return an instance of DaoAuthenticationProvider configured with
	 *         UserDetailsService and PasswordEncoder
	 */
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		// Create a new instance of DaoAuthenticationProvider
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

		// Set the custom UserDetailsService implementation to the
		// DaoAuthenticationProvider
		// This will be used to load user-specific data during authentication
		daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);

		// Set the password encoder to be used for encoding and matching passwords
		// BCryptPasswordEncoder is a strong and secure hashing algorithm
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

		// Return the configured DaoAuthenticationProvider bean
		return daoAuthenticationProvider;
	}

	/**
	 * Configures HTTP security settings.
	 * 
	 * @param http HttpSecurity object to configure
	 * @return DefaultSecurityFilterChain instance
	 * @throws Exception if an error occurs while configuring HTTP security
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()) // Disable CSRF protection
				.authorizeHttpRequests(auth -> auth.anyRequest().authenticated()) // Authenticate all requests
				.httpBasic(Customizer.withDefaults()); // Use basic authentication

		return http.build(); // Build and return the security filter chain
	}

	/**
	 * Defines an in-memory user details service with two users: adminUser and
	 * normalUser. The user details service is used by Spring Security to
	 * authenticate and authorize users.
	 *
	 * @return an instance of InMemoryUserDetailsManager containing the defined
	 *         users
	 */
//	@Bean
//	public UserDetailsService userDetailService() {
//
//		// Create an admin user with the username "Muskaan", password "muskaan", and
//		// role "ADMIN"
//		UserDetails adminUser = User.builder().username("Muskaan").password(passwordEncoder().encode("muskaan")) // Encode
//																													// the
//																													// password
//																													// using
//																													// BCrypt
//				.roles("ADMIN").build();
//
//		// Create a normal user with the username "User", password "user", and role
//		// "NORMAL"
//		UserDetails normalUser = User.builder().username("User").password(passwordEncoder().encode("user")) // Encode
//																											// the
//																											// password
//																											// using
//																											// BCrypt
//				.roles("NORMAL").build();
//
//		// Return an in-memory user details manager containing both users
//		return new InMemoryUserDetailsManager(adminUser, normalUser);
//	}

}
