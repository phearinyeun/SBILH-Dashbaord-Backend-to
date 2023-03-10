package com.sbilhbank.insur.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbilhbank.insur.entity.primary.User;
import com.sbilhbank.insur.extras.response.Response;
import com.sbilhbank.insur.security.JwtTokenUtil;
import com.sbilhbank.insur.security.JwtUserDetailsService;
import com.sbilhbank.insur.service.user.security.SecuredUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter
{
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private void response401(AuthenticationException e,HttpServletResponse response) throws IOException {
        Response res = Response.failed(e);
        ObjectMapper objectMapper= new ObjectMapper();
        String json = objectMapper.writeValueAsString(res);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
        response.flushBuffer();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException
    {
        final String requestTokenHeader =  request.getHeader("authorization");

        String username = null;
        String jwtToken = null;

        // JWT Token is in the form "Bearer token".
        //Remove Bearer word and get only the Token
        if (requestTokenHeader != null)
        {
            if(requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7);
                try {
                    username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                } catch (ExpiredJwtException e) {
                    logger.error("JWT Token has expired");
//                    throw new InsufficientAuthenticationException("Invalid token");
                    response401(new InsufficientAuthenticationException("Invalid token"),response);
                    return;
                } catch (SignatureException e){
                    logger.error("Signature invalid");
//                    throw new InsufficientAuthenticationException("Invalid token");
                    response401(new InsufficientAuthenticationException("Invalid token"),response);
                    return;
                } catch (Exception e) {
                    logger.error("Unable to get JWT Token");
//                    throw new InsufficientAuthenticationException("Invalid token");
                    response401(new InsufficientAuthenticationException("Invalid token"),response);
                    return;
                }
            }else logger.warn("JWT Token does not begin with Bearer String");
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            User user =    this.jwtUserDetailsService.loadUserByUsername(username);
            if(user == null) throw new InsufficientAuthenticationException("Invalid token");
            UserDetails userDetails = new SecuredUserDetails(this.jwtUserDetailsService.loadUserByUsername(username));
            // if token is valid configure Spring Security to manually set
            // authentication

            if (jwtTokenUtil.validateToken(jwtToken, user))
            {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails.getUsername(),null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                //
                //// After setting the Authentication in the context, we specify
                //// that the current user is authenticated. So it passes the
                //// Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }else{
//                throw new InsufficientAuthenticationException("Invalid token");
                response401(new InsufficientAuthenticationException("Invalid token"),response);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}