package com.athena.meerkat.controller.web.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * json convert util class.
 *
 * @author BongJin Kwon
 */
public abstract class JSONUtil {

	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	
	public static <T> T jsonToList(String json, Class<?> parametrized, Class<?>... parameterClasses) {
		try {
			TypeFactory typeFactory = MAPPER.getTypeFactory();
			
			return MAPPER.readValue(json, typeFactory.constructParametricType(parametrized, parameterClasses));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * <pre>
	 * convert json string to class Object.
	 * </pre>
	 *
	 * @param <T>
	 * @param json
	 * @param valueType
	 * @return
	 */
	public static <T> T jsonToObj(String json, Class<T> valueType) {

		try {
			return MAPPER.readValue(json, valueType);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}


	}

	/**
	 * <pre>
	 * convert Object to json String.
	 * </pre>
	 *
	 * @param obj
	 * @return
	 */
	public static String objToJson(Object obj) throws IOException {
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		JsonGenerator generator =
				MAPPER.getJsonFactory().createJsonGenerator(outputStream, JsonEncoding.UTF8);
		
		MAPPER.writeValue(generator, obj);
		
		return outputStream.toString(JsonEncoding.UTF8.getJavaName());
	}
	
	/**
	 * Method to deserialize JSON content as tree expressed using set of JsonNode instances. 
	 * @param json JSON content
	 * @return
	 */
	public static JsonNode readTree(String json){
		
		try {
			return MAPPER.readTree(json);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
