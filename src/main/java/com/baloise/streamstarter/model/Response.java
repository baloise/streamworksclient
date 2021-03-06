package com.baloise.streamstarter.model;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Response {
    public String Mandator;
    public String PlanDate;
    public List<Job> Jobs;
    public String StreamName;
    public String Status;
    public int RunNumber;
    public String PreparationIds;
   
    

	public String getStatus() {
		return Status;
	}



	@Override
    public String toString() {
    	try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return super.toString();
		}
    }    
    
}
