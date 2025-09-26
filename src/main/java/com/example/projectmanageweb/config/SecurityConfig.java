package com.example.projectmanageweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
	}
	
	@SuppressWarnings("deprecation")
	@Bean
	org.springframework.security.authentication.dao.DaoAuthenticationProvider authProvider(
			com.example.projectmanageweb.service.CustomUserDetailsService uds, PasswordEncoder enc){
		var p = new org.springframework.security.authentication.dao.DaoAuthenticationProvider();
		p.setUserDetailsService(uds);
		p.setPasswordEncoder(enc);
		return p;
		
	}
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,org.springframework.security.authentication.dao.DaoAuthenticationProvider provider
			) throws Exception {
		
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(
					"/",
					"/index",
					"/login",
					"/register",
					"/reset/**",
					"/favicon.ico",
					"/css/**",
					"/js/**",
					"/images/**",
					"/vendors/**",
					"/assets/**",
					"/webjars/**"
				).permitAll()
				.anyRequest().permitAll()
			)
		.formLogin(form -> form                     
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")               
                .passwordParameter("password")
                .defaultSuccessUrl("/user/home", true)
                .failureUrl("/login?error")
        )
		.rememberMe(rm -> rm
	            .rememberMeParameter("remember-me")
	            .tokenValiditySeconds(86400)      
	        )
		.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
        )
			.csrf(csrf -> csrf.disable())
			.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
		return http.build();
	}
} 