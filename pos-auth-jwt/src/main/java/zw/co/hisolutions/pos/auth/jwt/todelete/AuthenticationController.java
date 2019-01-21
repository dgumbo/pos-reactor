package zw.co.hisolutions.pos.auth.jwt.todelete;

//package zw.co.hisolutions.auth.jwt.controller;
// 
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
// 
//import static org.springframework.web.bind.annotation.RequestMethod.POST;
//import zw.co.hisolutions.auth.entity.User;
//import zw.co.hisolutions.auth.jwt.util.JwtTokenUtil;
//import zw.co.hisolutions.auth.jwt.entity.JwtAuthenticationToken;
//
//@RestController
//@RequestMapping("auth")
//public class AuthenticationController {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    
//    @Autowired
//    private JwtTokenUtil jwtUtils ;
//    
//    @RequestMapping(method = POST)
//    public ResponseEntity<?> authenticationRequest(@RequestBody User authenticationRequest)
//            throws AuthenticationException, IOException  {
//        
////        System.out.println("zw.co.hisolutions.jwt_auth.security.controllers.AuthenticationController.authenticationRequest()");
////        
//        String username = authenticationRequest.getUsername();
//        String password = authenticationRequest.getPassword();
//
//        // throws authenticationException if it fails !
//        Authentication authentication = this.authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(username, password)
//        );
//        
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//  
//        String token = jwtUtils.doGenerateToken(username, authentication.getAuthorities());
//
//        // Return the token
//        return ResponseEntity.ok(new JwtAuthenticationToken(token));
//    }
//}
