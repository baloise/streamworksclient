package com.baloise.streamstarter.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Job {
	 public String JobName;
	 public String Status;
	 public String RunningStatus;
	 
	 
	 @Override
	    public String toString() {
	    	try {
				return new ObjectMapper().writeValueAsString(this);
			} catch (JsonProcessingException e) {
				return super.toString();
			}
	    }	 
}
