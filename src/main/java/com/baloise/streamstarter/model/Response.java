package com.baloise.streamstarter.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Response {
    public String Mandator;
    public String PlanDate;
    public int RunNumber;
    
    
    
    @Override
    public String toString() {
    	try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return super.toString();
		}
    }
    
    
}
