package com.example.httpclientstarter.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

/**
 * JSON工具类
 */
public class ObjectMapperUtils {
    /**
     * ObjectMapper
     */
    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = newObjectMapper();
    }

    /**
     * 初始化ObjectMapper
     *
     * @return
     */
    public static ObjectMapper newObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(Feature.AUTO_CLOSE_TARGET, false);
        objectMapper.configure(Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        return objectMapper;
    }

    /**
     * 解析json对象
     *
     * @param data
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T parseJson(String data, Type type) {
        try {
            return OBJECT_MAPPER.readValue(data, OBJECT_MAPPER.constructType(type));
        } catch (Exception e) {
            throw new RuntimeException("invalid data:" + data, e);
        }
    }

    /**
     * 解析json对象
     *
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseJson(String data, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(data, clazz);
        } catch (Exception e) {
            throw new RuntimeException("invalid data:" + data, e);
        }
    }

    /**
     * 解析json对象
     *
     * @param data
     * @param valueTypeRef
     * @param <T>
     * @return
     */
    public static <T> T parseJson(String data, TypeReference<T> valueTypeRef) {
        try {
            return OBJECT_MAPPER.readValue(data, valueTypeRef);
        } catch (Exception e) {
            throw new RuntimeException("invalid data:" + data, e);
        }
    }

    /**
     * 解析json对象
     *
     * @param reader
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseJson(Reader reader, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(reader, clazz);
        } catch (Exception e) {
            throw new RuntimeException("invalid data", e);
        }
    }

    /**
     * 解析json对象
     *
     * @param reader
     * @param valueTypeRef
     * @param <T>
     * @return
     */
    public static <T> T parseJson(Reader reader, TypeReference<T> valueTypeRef) {
        try {
            return OBJECT_MAPPER.readValue(reader, valueTypeRef);
        } catch (Exception e) {
            throw new RuntimeException("invalid data(json)", e);
        }
    }

    /**
     * 解析json对象
     *
     * @param o
     * @return
     */
    public static String toJsonStr(Object o) {
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("when to json:" + o, e);
        }
    }

    /**
     * 将对象以json形式输出到Writer
     *
     * @param w
     * @param o
     * @throws IOException
     */
    public static void writeJson(Writer w, Object o) throws IOException {
        OBJECT_MAPPER.writeValue(w, o);
    }
}
