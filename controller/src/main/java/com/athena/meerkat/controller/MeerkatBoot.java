/* 
 * Athena Peacock Dolly - DataGrid based Clustering 
 * 
 * Copyright (C) 2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Bong-Jin Kwon	2015. 1. 6.			First Draft.
 */
package com.athena.meerkat.controller;

import io.netty.channel.nio.NioEventLoopGroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.athena.meerkat.controller.web.user.UserService;

/**
 * Main class with Spring Boot
 * 
 * - required java option : -Dspring.config.name=dolly -Dspring.profiles.active=[local|dev|prd]
 * 
 * @author BongJin Kwon
 * 
 */
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.athena.meerkat.controller" })
@PropertySource(value={"classpath:dolly.properties", "classpath:dolly-${spring.profiles.active:local}.properties"})
public class MeerkatBoot extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(MeerkatBoot.class, args);
	}
	

	/**
	 * <pre>
	 * Spring Security Java Config
	 * </pre>
	 * 
	 * @author Bong-Jin Kwon
	 * @version 2.0
	 */
	@SuppressWarnings("deprecation")
	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	@EnableGlobalMethodSecurity(securedEnabled = true)
	protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

		@Autowired
		private SecurityProperties security;
		
		@Autowired
		private UserService userService;
		

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/", 
					"/index.html", 
					"/app.js",
					"/resources/**",

					"/getServerList", 
					"/user/notLogin*",
					"/user/loginFail*", 
					"/user/accessDenied*",
					"/user/onAfterLogout*"
					);
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.anonymous().disable().authorizeRequests().anyRequest()
					.fullyAuthenticated().and().exceptionHandling()
					.accessDeniedPage("/user/accessDenied").and().formLogin()
					.loginPage("/user/notLogin")
					.loginProcessingUrl("/user/login")
					.defaultSuccessUrl("/user/onAfterLogin", true)
					.failureUrl("/user/loginFail").and().logout()
					.logoutUrl("/user/logout")
					.logoutSuccessUrl("/user/onAfterLogout").and().csrf()
					.disable();
			// http.anonymous().and().csrf().disable();

		}
		

		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		    auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
		    //auth.inMemoryAuthentication().withUser("dolly").password("dolly").roles("ADMIN");
		}

		
		@Bean
		public PasswordEncoder passwordEncoder() {
			return NoOpPasswordEncoder.getInstance();
		}
		
		/*
		 * for netty below
		 */
		@Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
		public NioEventLoopGroup getBossGroup() {
			NioEventLoopGroup group = new NioEventLoopGroup();
			return group;
		}

		@Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
		public NioEventLoopGroup getWorkerGroup() {
			NioEventLoopGroup group = new NioEventLoopGroup();
			return group;
		}

	}

}
