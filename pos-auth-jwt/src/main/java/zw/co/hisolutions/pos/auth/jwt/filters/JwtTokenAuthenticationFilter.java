package zw.co.hisolutions.pos.auth.jwt.filters;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import org.springframework.stereotype.Component;
import zw.co.hisolutions.pos.auth.config.MATCHERS;
import static zw.co.hisolutions.pos.auth.entity.CONSTANTS.*;
import zw.co.hisolutions.pos.auth.entity.User;
import zw.co.hisolutions.pos.auth.jwt.entity.JwtAuthenticationToken;
import zw.co.hisolutions.pos.auth.jwt.util.JwtTokenUtil; 

@Component
public class JwtTokenAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenUtil jwtUtils;
    private final List<RequestMatcher> requestMatchers;

    @Autowired
    public JwtTokenAuthenticationFilter(JwtTokenUtil jwtUtils) {
        this.jwtUtils = jwtUtils;

        List<RequestMatcher> requestMatcherList = new ArrayList();
        for (String matcher : MATCHERS.REST_PROTECTED) {
            AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(matcher);
            requestMatcherList.add(requestMatcher);
        }
        requestMatchers = requestMatcherList;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (!requiresAuthentication(request)) {
            /*
            if the URL requested doesn't match the URL handled by the filter, then we chain to the next filters.
             */
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(HEADER_STRING);
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            /*
            If there's not authentication information, then we chain to the next filters.
             The SecurityContext will be analyzed by the chained filter that will throw AuthenticationExceptions if necessary
             */
            chain.doFilter(request, response);
            return;
        }

        try {
            /*
            The token is extracted from the header. It's then checked (signature and expiration)
            An Authentication is then created and registered in the SecurityContext.
            The SecurityContext will be analyzed by chained filters that will throw Exceptions if necessary
            (like if authorizations are incorrect).
             */
            JwtAuthenticationToken jwtToken = extractAndDecodeJwt(request);
//            System.out.println("\njwtToken : " + jwtToken.getToken() + "\n"); 

            checkAuthenticationAndValidity(jwtToken);

            Authentication auth = buildAuthenticationFromJwt(jwtToken, request);
            SecurityContextHolder.getContext().setAuthentication(auth);

            chain.doFilter(request, response);

        } catch ( JwtException ex) { 
            System.out.println("\nJwtException : " + ex.getMessage() + "\n");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);       
            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
            chain.doFilter(request, response);
            //throw new AccountExpiredException("Token is not valid anymore");
        } catch ( ParseException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new JwtException("Token is malformed");
        }

        /* SecurityContext is then cleared since we are stateless.*/
        SecurityContextHolder.clearContext();
    }

    private boolean requiresAuthentication(HttpServletRequest request) {
        return requestMatchers.stream().anyMatch(
                (requestMatcher) -> (requestMatcher.matches(request)));
    }

    private JwtAuthenticationToken extractAndDecodeJwt(HttpServletRequest request) throws ParseException {
        String authHeader = request.getHeader(AUTHORIZATION);
        String token = authHeader.replace(TOKEN_PREFIX, "");

        JwtAuthenticationToken jwtToken = new JwtAuthenticationToken(token);
        return jwtToken;
    }

    private boolean checkAuthenticationAndValidity(JwtAuthenticationToken jwt) throws ExpiredJwtException {
//        System.out.println("zw.co.hisolutions.auth.jwt.filter.JwtTokenAuthenticationFilter.checkAuthenticationAndValidity()");
//        System.out.println("jwt.getToken() : " + jwt.getToken() + "\n"); 
        return jwtUtils.assertNotExpired(jwt);
    }

    private Authentication buildAuthenticationFromJwt(JwtAuthenticationToken jwt, HttpServletRequest request) throws ParseException {
        String username = jwtUtils.getUsernameFromToken(jwt.getToken());
        Collection<? extends GrantedAuthority> authorities = jwtUtils.getUserRoles(jwt.getToken());
        Date creationDate = jwtUtils.getIssuedAtDateFromToken(jwt.getToken());
        User userDetails = new User(username, creationDate, authorities);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }

}
