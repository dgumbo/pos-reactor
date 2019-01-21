package zw.co.hisolutions.pos.auth.entity; 

public class CONSTANTS {

    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5 * 60 * 60 ;
    //public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 30 ;
    public static final String SIGNING_KEY = "chitova";
    public static final String TOKEN_PREFIX = "Token ";
    
    //@Value("${jwt.header}") 
    public static final String HEADER_STRING = "Authorization";
}
