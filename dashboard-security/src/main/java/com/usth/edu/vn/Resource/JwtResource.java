package com.usth.edu.vn.Resource;

import com.usth.edu.vn.Jwt.JwtIssuerType;
import com.usth.edu.vn.Jwt.JwtService;
import com.usth.edu.vn.repository.UserDaoRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("jwt")
@ApplicationScoped
@Produces(MediaType.TEXT_PLAIN)
public class JwtResource {

    @Inject
    UserDaoRepository userDaoRepository;

    @Inject
    JwtService jwtService;

    @GET
    @RolesAllowed({"admin", "user"})
    public Response getToken(@Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        String role = userDaoRepository.find(
                "username",
                securityContext.getUserPrincipal().getName()
        ).singleResult().getRoles();
        String jwt = jwtService.generateJwt(JwtIssuerType.DASHBOARD_USER, username, role);
        return Response.ok(jwt).build();
    }
}