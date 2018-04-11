package com.test.metro.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;


/***
 * Handles all the Metro APIs
 * Handle the deserialization of Json to its corresponding class Object
 */
public class MetroApi {
    
    private static final Logger log = LoggerFactory.getLogger(MetroApi.class);
    private final ObjectMapper objectMapper;
    
    public MetroApi() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }
    
    /**
     * Perform GET Rest Api call for the given url
     * @param url - Metro Transit Url
     * @return - Json Response
     */
    public String doGet(String url) {
        log.info("\n URI:" + url);
        HttpEntity<String> entity = new HttpEntity<String>("", new HttpHeaders());
        ResponseEntity<String> responseEntity = new RestTemplate().exchange(url, HttpMethod.GET,
            entity, String.class);
        if(!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            throw new IllegalStateException("Invalid Response from Metro API !!!");
        }
        return responseEntity.getBody().toString();
    }
    
    
    
    public <T> T deserializeJson(final Class<T> clazz, String url) {
        String response = doGet(url);
        
        try {
            return objectMapper.readValue(response, clazz);
        } catch (IOException e) {
            log.info("Before Exception jsonString {}" + response);
            e.printStackTrace();
            throw new IllegalStateException("unable to serialize json", e);
        }
    }
}
