package com.baloise.streamstarter;

import java.io.IOException;

import org.apache.http.auth.UsernamePasswordCredentials;

import com.baloise.streamstarter.model.ImmedRequest;
import com.baloise.streamstarter.model.PrepareRequest;
import com.baloise.streamstarter.model.PrepareResponse;
import com.baloise.streamstarter.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.BaseRequest;
import com.mashape.unirest.request.HttpRequest;

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

	/**
	 * Init Unirest API
	 */
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

	/**
	 * Get Stream informations with streamName, planDate and runNumber
	 * 
	 * @param streamName
	 * @param planDate
	 * @param runNo
	 * @return
	 * @throws UnirestException
	 */
	public Response getStreamRuns(String streamName, String planDate, int runNo) throws UnirestException {
		
		String query = "streamruns";
		
		String url = baseURL+"{query}/{mandator}/{plandat}/{stream}/{runNo}";
		HttpRequest req = Unirest.get(url)
				  .routeParam("query", query)
				  .routeParam("mandator", mandator)
				  .routeParam("plandat", planDate)
				  .routeParam("stream", streamName)
				  .routeParam("runNo", String.valueOf(runNo));
		return request(req, Response.class);

	}
	
	/**
	 * Get stream informations with preparatioonId 
	 * 
	 * @param id
	 * @return
	 * @throws UnirestException
	 */
	public Response getStreamRunsPrepared(String id) throws UnirestException {
		
		String query = "streamruns";
		
		String url = baseURL+"{query}/{mandator}/{id}";
		HttpRequest req = Unirest.get(url)
				  .routeParam("query", query)
				  .routeParam("mandator", mandator)
				  .routeParam("id", id);			  
				 
		return request(req, Response.class);
	}
	
	/**
	 * Get Job informations with streamname planDate runNumber
	 * 
	 * @param streamName
	 * @param planDate
	 * @param runNo
	 * @return
	 * @throws UnirestException
	 */
	public Response getStreamRunJobs(String streamName, String planDate, int runNo) throws UnirestException {
		
		String query = "streamrunjobs";
		
		String url = baseURL+"{query}/{mandator}/{plandat}/{stream}/{runNo}";
		HttpRequest req = Unirest.get(url)
				  .routeParam("query", query)
				  .routeParam("mandator", mandator)
				  .routeParam("plandat", planDate)
				  .routeParam("stream", streamName)
				  .routeParam("runNo", String.valueOf(runNo));
		
		return request(req, Response.class);
	}
	
	/**
	 * Get Job informations from Stream with preparationId
	 * 
	 * @param id
	 * @return 
	 * @throws UnirestException
	 */
	public Response getStreamRunJobsPrepared(String id) throws UnirestException {
		
		String query = "streamrunjobs";
		
		String url = baseURL+"{query}/{mandator}/{id}";
		HttpRequest req = Unirest.get(url)
				  .routeParam("query", query)
				  .routeParam("mandator", mandator)
				  .routeParam("id", id);
		
		return request(req, Response.class);
	}
	/**
	 * Schedule Stream
	 * 
	 * @param streamName
	 * @param planDate
	 * @return Status 
	 * @throws UnirestException
	 */
	public int schedule(String streamName, String planDate) throws UnirestException {
		
		String query = "schedules";
		
		String url = baseURL+"{query}/{mandator}/{plandat}/{stream}";
		HttpRequest req = Unirest.post(url)
				  .routeParam("query", query)
				  .routeParam("mandator", mandator)
				  .routeParam("plandat", planDate)
				  .routeParam("stream", streamName)
				  .header("accept", "application/json")
				  .basicAuth(credentials.getUserName(), credentials.getPassword()); 
		
		// System.out.println(req.getBody());
		HttpResponse<String> response = req.asString();
		
		return response.getStatus();				
	}
	
	/**
	 * Start Stream immediately 
	 * if start-time not set by streamworks  or start-time is later than current time
	 *     
 	 * @return
	 * @throws UnirestException
	 */
	public int streamrunstartsPrepared(String prepId, ImmedRequest request) throws UnirestException {
		
		String query = "streamrunstarts";		 
			
		String url = baseURL+"{query}/{mandator}/{prepId}";
		
		String body = request.toString();
		// System.out.println(body);
		BaseRequest req = Unirest.post(url)
				  .routeParam("query", query)
				  .routeParam("mandator", mandator)
				  .routeParam("prepId", prepId)
				  .basicAuth(credentials.getUserName(), credentials.getPassword())
				  .header("accept", "text/plain")
				  .header("Content-Type" , "application/json") 
				  .body(body);

		System.err.println(req.asString().getBody());		
		HttpResponse<String> response = req.asString();
		
		return response.getStatus();
	}
	
	/**
	 * Prepare Stream next run with Parameters
	 * 
	 * @return the preparationId
	 */
	public String prepareNextWithParms(String streamName, String planDate, PrepareRequest request) throws UnirestException {
		
		String query = "preparations";
		String runNo = "P";
			
		String url = baseURL+"{query}/{mandator}/{plandat}/{stream}/{runNo}";
		
		String body = request.toString();
		System.out.println(body);
		BaseRequest req = Unirest.post(url)
				  .routeParam("query", query)
				  .routeParam("mandator", mandator)
				  .routeParam("plandat", planDate)
				  .routeParam("stream", streamName)
				  .routeParam("runNo", runNo)
				  .basicAuth(credentials.getUserName(), credentials.getPassword())
				  .header("accept", "text/plain")
				  .header("Content-Type" , "application/json" )
				  .body(body);

		// System.err.println(req.asString().getBody());
		HttpResponse<PrepareResponse> response = req.asObject(PrepareResponse.class);		 	    
		
		return response.getBody().PreparationIds.P;
	}

	/**
	 * Prepare Stream
	 * 
	 * @return the preparationId
	 */
	public String prepareNext(String streamName, String planDate) throws UnirestException {
		
		String query = "preparations";
		String runNo = "P";
			
		String url = baseURL+"{query}/{mandator}/{plandat}/{stream}/{runNo}";
		
		// String body = request.toString();
		// System.out.println(body);
		BaseRequest req = Unirest.post(url)
				  .routeParam("query", query)
				  .routeParam("mandator", mandator)
				  .routeParam("plandat", planDate)
				  .routeParam("stream", streamName)
				  .routeParam("runNo", runNo)
				  .basicAuth(credentials.getUserName(), credentials.getPassword())
				  .header("accept", "text/plain")
				  .header("Content-Type" , "application/json");

		System.err.println(req.asString().getBody());
		HttpResponse<PrepareResponse> response = req.asObject(PrepareResponse.class);		 	    
		
		return response.getBody().PreparationIds.P;
	}
	
	/**
	 * Response
	 * 
	 * @param req
	 * @param responseClass
	 * @return
	 * @throws UnirestException
	 */
	private Response request(HttpRequest req, Class<Response> responseClass) throws UnirestException {
		req.header("accept", "application/json")
		.basicAuth(credentials.getUserName(), credentials.getPassword());
		// for debug
		//	System.err.println(req.asString().getBody());
		return req.asObject(responseClass).getBody();
	}

}
