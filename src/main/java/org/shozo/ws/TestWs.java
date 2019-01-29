package org.shozo.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.shozo.filter.JWTTokenNeeded;


@Path("echo")
@Produces("text/plain")
public class TestWs {
	
	
	List<String> myList = new ArrayList<String>(Arrays.asList("shozo","pozo"));
	
	@GET
	@Path("NoAuth")
	@Produces("text/plain")
	public String noAuthMethod(@QueryParam("message") String message) {
		
		return "I dont need authentication";
	}
	
	
	@GET
	@Path("jwt")
	@Produces("text/plain")
	@JWTTokenNeeded
	public String authRequired(@QueryParam("message") String message) {
		
		return "I  need authentication";
	}
	
	//testing concurrency on class variables like myList
	
	@GET
	@Path("modify")
	@Produces("application/json")
	public List<String> modifyValue() {
		
		myList.add("shahrukh");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myList;
	}
	
	@GET
	@Path("read")
	@Produces("application/json")
	public List<String> readValue() {
		
		return myList;
	}

	
}
