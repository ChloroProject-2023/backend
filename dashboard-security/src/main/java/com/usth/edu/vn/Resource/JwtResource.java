package com.usth.edu.vn.Resource;

import com.usth.edu.vn.Jwt.JwtIssuerType;
import com.usth.edu.vn.Jwt.JwtService;
import com.usth.edu.vn.repository.UserDaoRepository;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Set;

@Path("jwt")
@ApplicationScoped
@Produces(MediaType.TEXT_PLAIN)
public class JwtResource {

    @Inject
    UserDaoRepository userDaoRepository;

    @Inject
    JwtService jwtService;

    @Inject
    SecurityIdentity securityIdentity;

    @GET
    @RolesAllowed({"admin", "user"})
    public Response getToken() {
        String username = securityIdentity.getPrincipal().getName();
        Set<String> roles = securityIdentity.getRoles();
        String jwt = jwtService.generateJwt(JwtIssuerType.DASHBOARD_USER, username, roles);
        return Response.ok(jwt).build();
    }
}