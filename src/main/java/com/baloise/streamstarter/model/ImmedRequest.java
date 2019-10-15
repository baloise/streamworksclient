package com.baloise.streamstarter.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImmedRequest {
	public String StartDate;
	public String StartTime;
	public String TimeZone;
	
	
	 @Override
	    public String toString() {
	    	try {
				return new ObjectMapper().writeValueAsString(this);
			} catch (JsonProcessingException e) {
				return super.toString();
			}
	    }
	
}
