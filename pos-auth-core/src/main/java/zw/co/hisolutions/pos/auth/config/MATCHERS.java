package zw.co.hisolutions.pos.auth.config;

import java.util.Map;
import org.springframework.http.HttpMethod;

/**
 *
 * @author dgumbo
 */
//@Service
public class MATCHERS {

    public static final String[] PUBLIC_UNPROTECTED = { 
        "/init/**",
        "/users/**",
        "/storage/**",
        "/views/rest/**",
        "/login",
        "/logout", 
        "/**.css",
        "/**.eot",
        "/**.svg",
        "/**.woff2",
        "/**.ttf",
        "/**.woff", 
        "/**.js",
        "/**.ico",
        "/assets/**", 
        "/",
        "/auth/**",
        "/jwtauth", 
        "/jwtauth/**",
        "/index",  
        "/index.html"
    };

    public static final String[] REST_PROTECTED = {
        "/rest/**",
        "/storage/upload"
    };

    public static final String[] OTHER_PROTECTED = { 
        "/admin/**",
        "/storage/upload"
    };
    
    public static Map<HttpMethod, String>[] roleMenu;

}
