package zw.co.hisolutions.pos.auth.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException; 
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import static zw.co.hisolutions.pos.auth.entity.CONSTANTS.ACCESS_TOKEN_VALIDITY_SECONDS;
import static zw.co.hisolutions.pos.auth.entity.CONSTANTS.SIGNING_KEY;
import static zw.co.hisolutions.pos.auth.entity.CONSTANTS.TOKEN_PREFIX;
import zw.co.hisolutions.pos.auth.entity.User;
import zw.co.hisolutions.pos.auth.jwt.entity.JwtAuthenticationToken;
import zw.co.hisolutions.pos.auth.service.UsersService;

@Component
public class JwtTokenUtil implements Serializable {

    @Autowired
    private UsersService userDetailsService;

    protected User getUserFromToken(String token) { 
        if (token != null) {
            if (token.startsWith(TOKEN_PREFIX)){                  
                token = token.replace(TOKEN_PREFIX,""); 
            }
            
            String username = getUsernameFromToken(token); 
            User user = userDetailsService.findUserByUsername(username);

            if (validateToken(token, user)) {
                return user;
                // SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        return null;
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token)  throws ExpiredJwtException {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws ExpiredJwtException {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token)  throws ExpiredJwtException {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public boolean assertNotExpired(JwtAuthenticationToken jwt)  throws ExpiredJwtException {
//        System.out.println("\n\nzw.co.hisolutions.auth.jwt.util.JwtTokenUtil.assertNotExpired()");
//        System.out.println("jwt : " + jwt.getToken());
//        System.out.println("\n");
        return isTokenExpired(jwt.getToken());
    }

    public String generateToken(User user) {
        return doGenerateToken(user.getUsername(), user.getAuthorities());
    }

    public String doGenerateToken(String username, Collection<? extends GrantedAuthority> authorities) {

        Claims claims = Jwts.claims().setSubject(username);

        List<String> roles = new ArrayList();
        authorities.forEach(role -> {
            roles.add(role.getAuthority());
        });
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("http://hisolutions.co.zw")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();
    }

    //assertValidSignature(jwt, secretKey)
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())
                && !isTokenExpired(token));
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return false;
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getIssuedAtDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public String refreshToken(String token) {
        final Date createdDate = new Date();
        final Date expirationDate = calculateExpirationDate(createdDate);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SIGNING_KEY)
                .compact();
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000);
    }

    public Collection<? extends GrantedAuthority> getUserRoles(String token) {
        User user = getUserFromToken(token);
        return user.getAuthorities();
    }
}
