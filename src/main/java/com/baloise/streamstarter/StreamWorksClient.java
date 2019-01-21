package com.baloise.streamstarter;

import java.io.IOException;

import org.apache.http.auth.UsernamePasswordCredentials;

import com.baloise.streamstarter.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.BaseRequest;

public class StreamWorksClient {

	private final UsernamePasswordCredentials credentials;

	private final String baseURL;			

	private final String mandator;
	
	public StreamWorksClient(String baseURL, String mandator, UsernamePasswordCredentials credentials) {
		this.baseURL = baseURL;
		this.mandator = mandator;
		this.credentials = credentials;
		initUniRest();
	}


	private void initUniRest() {
		Unirest.setObjectMapper(new ObjectMapper() {
		    private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
		                = new com.fasterxml.jackson.databind.ObjectMapper();
		    
		    {
		    	jacksonObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		    }
		    
		    public <T> T readValue(String value, Class<T> valueType) {
		        try {
		            return jacksonObjectMapper.readValue(value, valueType);
		        } catch (IOException e) {
		            throw new RuntimeException(e);
		        }
		    }

		    public String writeValue(Object value) {
		        try {
		            return jacksonObjectMapper.writeValueAsString(value);
		        } catch (JsonProcessingException e) {
		            throw new RuntimeException(e);
		        }
		    }
		});
		
	}

	public Response getStreamRuns(String streamName, String planDate, int runNo) throws UnirestException {
		
		String query = "streamruns";
		
		String url = baseURL+"{query}/{mandator}/{plandat}/{stream}/{runNo}";
		BaseRequest req = Unirest.get(url)
				  .routeParam("query", query)
				  .routeParam("mandator", mandator)
				  .routeParam("plandat", planDate)
				  .routeParam("stream", streamName)
				  .routeParam("runNo", String.valueOf(runNo))
				.header("accept", "application/json")
				.basicAuth(credentials.getUserName(), credentials.getPassword());
		
		return req.asObject(Response.class).getBody();
	}

}
