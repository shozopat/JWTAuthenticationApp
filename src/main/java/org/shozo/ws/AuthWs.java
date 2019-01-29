package org.shozo.ws;

import java.security.Key;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.shozo.util.KeyGenerator;

import io.jsonwebtoken.Jwts;

@Path("auth")
@Produces("text/plain")
public class AuthWs {

    @Inject
    private KeyGenerator keyGenerator;
    
	@GET
	@Path("{token}")
	public String authenticateRequest(@PathParam("token") String token) {
		
		System.out.println("in authenticateRequest "+token);
        try {
            // Validate the token
            Key key = keyGenerator.generateKey();
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return "authorized";
        
        } catch (Exception e) {
            System.out.println("reached exception in authenticateRequest "+token);
            return "unAuthorized";
        }
	
	}
	
}
