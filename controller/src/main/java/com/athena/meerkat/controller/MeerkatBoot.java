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
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.athena.meerkat.controller.web.user.services.UserService;

/**
 * Main class with Spring Boot
 * 
 * - required java option : -Dspring.config.name=dolly -Dspring.profiles.active=[local|dev|prd]
 * 
 * @author BongJin Kwon
 * 
 */
@Configuration
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
	@Configuration
	//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	@EnableWebSecurity
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
					"/monitor/**",
					"/tomcat/**",

					"/getServerList", 
					"/auth/notLogin*",
					"/auth/loginFail*", 
					"/auth/accessDenied*",
					"/auth/onAfterLogout*"
					);
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.anonymous()
					.disable()
				.authorizeRequests()
					
					.expressionHandler(webExpressionHandler())
					.antMatchers("/auth/onAfterLogin").fullyAuthenticated()
					
                    .antMatchers(HttpMethod.POST, "/domain/**").access("hasRole('ROLE_TOMCAT_ADMIN')")
                    .antMatchers(HttpMethod.POST, "/tomcat/**").access("hasRole('ROLE_TOMCAT_ADMIN')")
                    .antMatchers(HttpMethod.GET, "/domain/**").access("hasRole('ROLE_TOMCAT_USER')")
                    .antMatchers(HttpMethod.GET, "/tomcat/**").access("hasRole('ROLE_TOMCAT_USER')")
                    
                    //.antMatchers("/monitor/**").access("hasRole('ROLE_MONITOR_ADMIN')")
                    
                    .antMatchers("/dbmonitor/**").access("hasRole('ROLE_MONITOR_DB')")
                    
                    .antMatchers(HttpMethod.POST, "/res/**").access("hasRole('ROLE_RES_ADMIN')")
                    .antMatchers(HttpMethod.GET, "/res/**").access("hasRole('ROLE_RES_USER')")
                    
                    .antMatchers(HttpMethod.POST, "/user/**").access("hasRole('ROLE_USER_ADMIN')")
                    .antMatchers(HttpMethod.GET, "/user/**").access("hasRole('ROLE_USER_USER')")
                    
                    .anyRequest() // other request
                    .access("hasRole('ROLE_ADMIN')")
					.and()
				.exceptionHandling()
					.accessDeniedPage("/auth/accessDenied")
					.and()
				.formLogin()
					.loginPage("/auth/notLogin")
					.loginProcessingUrl("/auth/login")
					.defaultSuccessUrl("/auth/onAfterLogin", true)
					.failureUrl("/auth/loginFail")
					.and()
				.logout()
					.logoutUrl("/auth/logout")
					.logoutSuccessUrl("/auth/onAfterLogout")
					.and()
				.csrf()
					.disable();
		}
		
		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		    auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
		    //auth.inMemoryAuthentication().withUser("dolly").password("dolly").roles("USER_USER");
		}
		
		@Bean
		public RoleHierarchy roleHierarchy() {
		  RoleHierarchyImpl r = new RoleHierarchyImpl();
		  r.setHierarchy("ROLE_TOMCAT_ADMIN > ROLE_TOMCAT_USER | ROLE_MONITOR_ADMIN > ROLE_MONITOR_DB | ROLE_RES_ADMIN > ROLE_RES_USER | ROLE_USER_ADMIN > ROLE_USER_USER " + 
				  		 "ROLE_ADMIN > ROLE_TOMCAT_ADMIN | ROLE_ADMIN > ROLE_MONITOR_ADMIN | ROLE_ADMIN > ROLE_RES_ADMIN | ROLE_ADMIN > ROLE_USER_ADMIN");
		  return r;
		}
		
		private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
		    DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
		    defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
		    return defaultWebSecurityExpressionHandler;
		}

		/**
		 * <pre>
		 * configure user password encoder 
		 * </pre>
		 * @return
		 */
		@Bean
		public PasswordEncoder passwordEncoder() {
			return NoOpPasswordEncoder.getInstance();
			//return new BCryptPasswordEncoder();
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
