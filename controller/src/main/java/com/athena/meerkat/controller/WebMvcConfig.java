package com.athena.meerkat.controller;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.athena.meerkat.controller.web.common.converter.HandlerMethodEntityReturnValueHandler;
import com.athena.meerkat.controller.web.common.converter.StringToNumberConverterFactory;

/**
 * Spring MVC Configuration
 * @author BongJin Kwon
 * 
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		
		// 'yyyy-MM-dd' format string to Date converting.
		registry.addFormatter(new DateFormatter("yyyy-MM-dd"));
		
		registry.removeConvertible(String.class, Number.class);
		registry.addConverterFactory(new StringToNumberConverterFactory());
	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
		returnValueHandlers.add(new HandlerMethodEntityReturnValueHandler());
	}
	
	

}
