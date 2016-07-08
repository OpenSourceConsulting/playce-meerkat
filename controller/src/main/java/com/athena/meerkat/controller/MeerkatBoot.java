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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
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
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import com.athena.meerkat.controller.web.user.services.UserService;

/**
 * Main class with Spring Boot
 * 
 * - required java option : -Dspring.config.name=meerkat -Dspring.profiles.active=[local|dev|prd|try]
 * 
 * @author BongJin Kwon
 * 
 */
//@formatter:off
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.athena.meerkat" })
@PropertySource(value = { "classpath:meerkat-${spring.profiles.active:local}.properties" })
@EnableScheduling
public class MeerkatBoot implements SchedulingConfigurer{

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
	// @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	@EnableWebSecurity
	protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

		@Autowired
		private SecurityProperties security;

		@Autowired
		private UserService userService;

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers(
					"/", 
					"/*.html", 
					"/app.js",
					"/app.json",
					"/resources/**", 
					"/error", 
					
					"/monitor/endpoint", 
					"/monitor/init", 
					"/monitor/*/create", 
					"/domain/down/**",
					
					"/auth/notLogin*", 
					"/auth/loginFail*",
					"/auth/accessDenied*", 
					"/auth/onAfterLogout*");
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.anonymous()
					.disable()
					.authorizeRequests()

					.expressionHandler(webExpressionHandler())
					
					.antMatchers("/auth/onAfterLogin").fullyAuthenticated()
					.antMatchers("/dashboard/**").fullyAuthenticated()
					.antMatchers("/menu/**").fullyAuthenticated()
					.antMatchers("/code/**").fullyAuthenticated()

					.antMatchers(HttpMethod.POST, "/domain/**").access("hasRole('ROLE_TOMCAT_ADMIN')")
					.antMatchers(HttpMethod.POST, "/tomcat/**").access("hasRole('ROLE_TOMCAT_ADMIN')")
					.antMatchers(HttpMethod.POST, "/application/**").access("hasRole('ROLE_TOMCAT_ADMIN')")
					.antMatchers(HttpMethod.POST, "/task/**").access("hasRole('ROLE_TOMCAT_ADMIN')")
					.antMatchers(HttpMethod.POST, "/configfile/**").access("hasRole('ROLE_TOMCAT_ADMIN')")
					.antMatchers(HttpMethod.POST, "/provi/**").access("hasRole('ROLE_TOMCAT_ADMIN')")
					
					.antMatchers(HttpMethod.GET, "/domain/**").access("hasRole('ROLE_TOMCAT_USER')")
					.antMatchers(HttpMethod.GET, "/tomcat/**").access("hasRole('ROLE_TOMCAT_USER')")
					.antMatchers(HttpMethod.GET, "/application/**").access("hasRole('ROLE_TOMCAT_USER')")
					.antMatchers(HttpMethod.GET, "/task/**").access("hasRole('ROLE_TOMCAT_USER')")
					.antMatchers(HttpMethod.GET, "/configfile/**").access("hasRole('ROLE_TOMCAT_USER')")
					.antMatchers(HttpMethod.GET, "/provi/**").access("hasRole('ROLE_TOMCAT_USER')")

					.antMatchers("/monitor/**").access("hasRole('ROLE_MONITOR_ADMIN')")

					.antMatchers(HttpMethod.POST, "/res/**").access("hasRole('ROLE_RES_ADMIN')")
					.antMatchers(HttpMethod.GET, "/res/**").access("hasRole('ROLE_RES_USER')")

					.antMatchers(HttpMethod.POST, "/user/**").access("hasRole('ROLE_USER_ADMIN')")
					.antMatchers(HttpMethod.GET, "/user/**").access("hasRole('ROLE_USER_USER')")

					.anyRequest()// other request
					.access("hasRole('ROLE_ADMIN')")
					
					.and().exceptionHandling().accessDeniedPage("/auth/accessDenied")
					.and().formLogin()
						.loginPage("/auth/notLogin")
						.loginProcessingUrl("/auth/login")
						.defaultSuccessUrl("/auth/onAfterLogin", true)
						.failureUrl("/auth/loginFail")
					.and().logout()
						.logoutUrl("/auth/logout")
						.logoutSuccessUrl("/auth/onAfterLogout")
					.and().csrf()
						.disable();
			// http.headers().frameOptions().disable();
			http.headers()
					.addHeaderWriter(
							(HeaderWriter) new XFrameOptionsHeaderWriter(
									XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
		}

		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth)
				throws Exception {
			auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
			// auth.inMemoryAuthentication().withUser("dolly").password("dolly").roles("USER_USER");
		}

		@Bean
		public RoleHierarchy roleHierarchy() {
			RoleHierarchyImpl r = new RoleHierarchyImpl();
			r.setHierarchy("ROLE_TOMCAT_ADMIN > ROLE_TOMCAT_USER | ROLE_MONITOR_ADMIN > ROLE_MONITOR_DB | ROLE_RES_ADMIN > ROLE_RES_USER | ROLE_USER_ADMIN > ROLE_USER_USER "
					+ "ROLE_ADMIN > ROLE_TOMCAT_ADMIN | ROLE_ADMIN > ROLE_MONITOR_ADMIN | ROLE_ADMIN > ROLE_RES_ADMIN | ROLE_ADMIN > ROLE_USER_ADMIN");
			return r;
		}

		private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
			DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
			defaultWebSecurityExpressionHandler
					.setRoleHierarchy(roleHierarchy());
			return defaultWebSecurityExpressionHandler;
		}

		/**
		 * <pre>
		 * configure user password encoder
		 * </pre>
		 * 
		 * @return
		 */
		@Bean
		public PasswordEncoder passwordEncoder() {
			return NoOpPasswordEncoder.getInstance();
			// return new BCryptPasswordEncoder();
		}

	}
	
	@Bean(name = "monDataRemoverTaskScheduler")
	public ThreadPoolTaskScheduler taskScheduler() {
	    return new ThreadPoolTaskScheduler();
	}
	
	@Autowired
	@Qualifier(value = "monDataRemoverTaskScheduler")
	private TaskScheduler taskScheduler;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setTaskScheduler(taskScheduler);
		
	}
	
}
//@formatter:on