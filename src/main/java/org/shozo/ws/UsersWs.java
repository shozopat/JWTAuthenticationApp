package org.shozo.ws;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.shozo.util.KeyGenerator;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.shozo.entities.User;
import org.shozo.util.PasswordUtils;
import org.shozo.util.SimpleKeyGenerator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

@Path("/users")
@Transactional
public class UsersWs {
	
	Logger logger = Logger.getLogger(UsersWs.class.getName());
	
    @Context
    private UriInfo uriInfo;

    @Inject
    private KeyGenerator keyGenerator;

    @PersistenceContext
    private EntityManager em;

	   	@POST
	    @Path("/login")
	    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	    public Response authenticateUser(@FormParam("login") String login,
	                                     @FormParam("password") String password) {

	        try {

	            logger.info("#### login/password : " + login + "/" + password);

	            // Authenticate the user using the credentials provided
	            authenticate(login, password);

	            // Issue a token for the user
	            String token = issueToken(login);

	            // Return the token on the response
	            return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();

	        } catch (Exception e) {
	            return Response.status(UNAUTHORIZED).build();
	        }
	    }

	    private void authenticate(String login, String password) throws Exception {
	        TypedQuery<User> query = em.createNamedQuery(User.FIND_BY_LOGIN_PASSWORD, User.class);
	        query.setParameter("login", login);
	        query.setParameter("password", PasswordUtils.digestPassword(password));
	        User user = query.getSingleResult();

	        if (user == null)
	            throw new SecurityException("Invalid user/password");
	    }

	    private String issueToken(String login) {
	        Key key = keyGenerator.generateKey();
	        String jwtToken = Jwts.builder()
	                .setSubject(login)
	                .setIssuer(uriInfo.getAbsolutePath().toString())
	                .setIssuedAt(new Date())
	                .setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
	                .signWith(SignatureAlgorithm.HS512, key)
	                .compact();
	        logger.info("#### generating token for a key : " + jwtToken + " - " + key);
	        return jwtToken;

	    }

	    @POST
	    public Response create(User user) {	        
	    	System.out.println(user);
	    	String digestPassword = PasswordUtils.digestPassword(user.getPassword());
	        user.setPassword(digestPassword);
	        System.out.println(user);
	        em.persist(user);
	        return Response.created(uriInfo.getAbsolutePathBuilder().path(user.getId()).build()).build();
	    }

	    @GET
	    @Path("/{id}")
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response findById(@PathParam("id") String id) {
	        User user = em.find(User.class, id);

	        if (user == null)
	            return Response.status(NOT_FOUND).build();

	        return Response.ok(user).build();
	    }

	    @GET
	    @Produces(MediaType.APPLICATION_JSON)
	    public Response findAllUsers() {
	        TypedQuery<User> query = em.createNamedQuery(User.FIND_ALL, User.class);
	        List<User> allUsers = query.getResultList();

	        if (allUsers == null)
	            return Response.status(NOT_FOUND).build();

	        return Response.ok(allUsers).build();
	    }

	    @DELETE
	    @Path("/{id}")
	    public Response remove(@PathParam("id") String id) {
	        em.remove(em.getReference(User.class, id));
	        return Response.noContent().build();
	    }

	    // ======================================
	    // =          Private methods           =
	    // ======================================

	    private Date toDate(LocalDateTime localDateTime) {
	        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	    }
}
