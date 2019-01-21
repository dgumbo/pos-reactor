package zw.co.hisolutions.pos.auth.jwt.todelete;
 
import zw.co.hisolutions.pos.auth.jwt.util.JwtTokenUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; 
import zw.co.hisolutions.pos.auth.entity.User;
import zw.co.hisolutions.pos.auth.service.UsersService;

/**
 *
 * @author dgumbo
 */
@RestController
@RequestMapping("/token")
public class TestTokenController {
    
    private final UsersService userService;
    private final JwtTokenUtil util;

    @Autowired
    public TestTokenController(UsersService userService, JwtTokenUtil util) {
        this.userService = userService;
        this.util = util ;
    }  
     
    @GetMapping("/test")
    public List<User> testToken(){
        System.out.println("zw.co.hisolutions.auth.jwtsecurity.controllers.TokenController.generate()");
        //User user = userService.getUserByUsername(username);
        
        return userService.findAll();        
    } 
}
