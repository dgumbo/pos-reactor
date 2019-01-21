package zw.co.hisolutions.pos.auth.jwt.util;

//package zw.co.hisolutions.auth.jwt.util;
// 
//import zw.co.hisolutions.auth.jwt.exceptions.JwtBadSignatureException;
//import zw.co.hisolutions.auth.jwt.exceptions.JwtExpirationException;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//
//import java.text.ParseException;
//import java.util.Collection;
//import java.util.Date;
//import java.util.Set;
// 
//import java.util.ArrayList;
//import java.util.List;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import static zw.co.hisolutions.auth.entity.CONSTANTS.ACCESS_TOKEN_VALIDITY_SECONDS;
//import static zw.co.hisolutions.auth.entity.CONSTANTS.SIGNING_KEY;
//import zw.co.hisolutions.auth.entity.User;
// 
//import io.jsonwebtoken.Claims; 
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import java.util.function.Function;
//import zw.co.hisolutions.auth.entity.UserRole;
//
//@Component
//public class JwtUtils {
//
//    private final String AUDIENCE_UNKNOWN = "unknown";
//    private final String AUDIENCE_WEB = "web";
//    private final String AUDIENCE_MOBILE = "mobile";
//    private final String AUDIENCE_TABLET = "tablet";
//    private final String ROLES_CLAIM = "roles";
//
//    private Date getExpirationTime() {
//        return new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000);
//    }
//
////    public String generateHMACToken(User user) throws JOSEException {
////        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
////        return generateHMACToken(user.getUsername(), authorities);
////    }
//
////    public String generateHMACToken(String subject, Collection<? extends GrantedAuthority> roles) throws JOSEException {
////        return generateHMACToken(subject, AuthorityListToCommaSeparatedString(roles));
////    }
//
////    public String generateHMACToken(String subject, String roles) throws JOSEException {
////        JWSSigner signer = new MACSigner(SIGNING_KEY);
////        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
////                .subject(subject)
////                .issueTime(currentDate())
////                .expirationTime(getExpirationTime())
////                .claim(ROLES_CLAIM, roles)
////                .audience(AUDIENCE_WEB)
////                .build();
////
////        SignedJWT signedJWT = new SignedJWT(new JWSHeader(HS256), claimsSet);
////        signedJWT.sign(signer);
////        return signedJWT.serialize();
////    }
//
//    private Date currentDate() {
//        return new Date(System.currentTimeMillis());
//    }
//
////    public void assertNotExpired(SignedJWT jwt) throws ParseException {
////        if (DateUtils.isBefore(jwt.getJWTClaimsSet().getExpirationTime(), currentDate(), 60)) {
////            throw new JwtExpirationException("Token has expired");
////        }
////    }
//
////    public void assertValidSignature(SignedJWT jwt, String SIGNING_KEY) throws ParseException, JOSEException {
////        if (!verifyHMACToken(jwt, SIGNING_KEY)) {
////            throw new JwtBadSignatureException("Signature is not valid");
////        }
////    }
//
////    public SignedJWT parse(String token) throws ParseException {
////        return SignedJWT.parse(token);
////    }
//
////    public boolean verifyHMACToken(SignedJWT jwt, String SIGNING_KEY) throws ParseException, JOSEException {
////        JWSVerifier verifier = new MACVerifier(SIGNING_KEY);
////        return jwt.verify(verifier);
////    }
//
//    private String AuthorityListToCommaSeparatedString(Collection<? extends GrantedAuthority> authorities) {
//        Set<String> authoritiesAsSetOfString = AuthorityUtils.authorityListToSet(authorities);
//        return StringUtils.join(authoritiesAsSetOfString, ", ");
//    }
//
////    public String getUsername(SignedJWT jwt) throws ParseException {
////        return jwt.getJWTClaimsSet().getSubject();
////    }
//
////    public Collection<? extends GrantedAuthority> getRoles(SignedJWT jwt) throws ParseException {
////        Collection<? extends GrantedAuthority> authorities;
////        String roles = jwt.getJWTClaimsSet().getStringClaim(ROLES_CLAIM);
////        authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
////        return authorities;
////    }
//
////    public Date getIssueTime(SignedJWT jwt) throws ParseException {
////        return jwt.getJWTClaimsSet().getIssueTime();
////    }
//
//    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
//        final Date created = getIssuedAtDateFromToken(token);
//        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
//                && (!isTokenExpired(token) || ignoreTokenExpiration(token));
//    }
//
//    public String refreshToken(String token) {
//        final Date createdDate = new Date();
//        final Date expirationDate = calculateExpirationDate(createdDate);
//
//        final Claims claims = getAllClaimsFromToken(token);
//        claims.setIssuedAt(createdDate);
//        claims.setExpiration(expirationDate);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .signWith(SignatureAlgorithm.HS512, SIGNING_KEY)
//                .compact();
//    }
//
//    public String getUsernameFromToken(String token) {
//        return getClaimFromToken(token, Claims::getSubject);
//    }
//
//    public List<UserRole> getUserRoles(String token) {
//        return getClaimFromToken(token, Claims::get("roles", List<UserRole> ) );
//    }
//
//    public Date getExpirationDateFromToken(String token) {
//        return getClaimFromToken(token, Claims::getExpiration);
//    }
//
//    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = getAllClaimsFromToken(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims getAllClaimsFromToken(String token) {
//        return Jwts.parser()
//                .setSigningKey(SIGNING_KEY)
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    private Boolean isTokenExpired(String token) {
//        final Date expiration = getExpirationDateFromToken(token);
//        return expiration.before(new Date());
//    }
//
//    public String generateToken(User user) {
//        return doGenerateToken(user.getUsername(), user.getAuthorities());
//    }
//
//    public String doGenerateToken(String username, Collection<? extends GrantedAuthority> authorities) {
//
//        Claims claims = Jwts.claims().setSubject(username);
//
//        List<String> roles = new ArrayList();
//        authorities.forEach(role -> {
//            roles.add(role.getAuthority());
//        });
//        claims.put("roles", roles);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuer("http://hisolutions.co.zw")
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
//                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
//                .compact();
//    }
//
//    public Boolean validateToken(String token, UserDetails userDetails) {
//        final String username = getUsernameFromToken(token);
//        return (username.equals(userDetails.getUsername())
//                && !isTokenExpired(token));
//    }
//
//    public Date getIssuedAtDateFromToken(String token) {
//        return getClaimFromToken(token, Claims::getIssuedAt);
//    }
//
//    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
//        return (lastPasswordReset != null && created.before(lastPasswordReset));
//    }
//
//    private Boolean ignoreTokenExpiration(String token) {
////        here you specify tokens, for that the  expiration is  {
////            ignored//        
////        }
//        return false;
//    }
//
//    private Date calculateExpirationDate(Date createdDate) {
//        return new Date(createdDate.getTime() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000);
//    }
//
//}
